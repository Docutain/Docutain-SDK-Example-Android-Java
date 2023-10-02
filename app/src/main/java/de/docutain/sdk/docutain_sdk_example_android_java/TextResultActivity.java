package de.docutain.sdk.docutain_sdk_example_android_java;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.docutain.sdk.DocutainSDK;
import de.docutain.sdk.dataextraction.DocumentDataReader;

public class TextResultActivity extends AppCompatActivity {
    private final String logTag = "DocutainSDK";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<String> textFuture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_result);

        loadText();
    }

    private void loadText() {
        textFuture = executorService.submit(() -> {
            Uri uri = getIntent().getParcelableExtra("uri");
            if (uri != null) {
                // If a URI is available, it means we have imported a file.
                // If so, we need to load it into the SDK first.
                if (!DocumentDataReader.loadFile(uri)) {
                    // An error occurred, get the latest error message.
                    Log.e(logTag, "DocumentDataReader.loadFile failed, last error: " + DocutainSDK.getLastError());
                    return null;
                }
            }

            // Get the text of all currently loaded pages.
            // If you want text of just one specific page, define the page number.
            // See [https://docs.docutain.com/docs/Android/textDetection] for more details.
            return DocumentDataReader.getText();
        });

        findViewById(R.id.activity_indicator).setVisibility(View.VISIBLE); // Show loading indicator

        executorService.execute(() -> {
            try {
                final String text = textFuture.get(); // Blocking call to get the result
                runOnUiThread(() -> {
                    findViewById(R.id.activity_indicator).setVisibility(View.GONE); // Hide loading indicator
                    TextView textView = findViewById(R.id.textView);
                    textView.setText(text);
                });
            } catch (Exception e) {
                // Handle any errors here.
                Log.e(logTag, "Error loading text: " + e.getMessage());
            }
        });
    }

    // Don't forget to clean up the ExecutorService when it's no longer needed.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textFuture != null && !textFuture.isDone()) {
            textFuture.cancel(true); // Cancel the task if it's still running
        }
        executorService.shutdown();
    }
}
