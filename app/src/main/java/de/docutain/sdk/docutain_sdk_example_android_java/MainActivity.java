package de.docutain.sdk.docutain_sdk_example_android_java;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.docutain.sdk.Document;
import de.docutain.sdk.DocutainSDK;
import de.docutain.sdk.dataextraction.DocumentDataReader;
import de.docutain.sdk.ui.DocumentScannerConfiguration;
import de.docutain.sdk.ui.ScanResult;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<File> pdfFuture;
    private final String logTag = "DocutainSDK";

    private ListAdapter.ItemType selectedOption = ListAdapter.ItemType.NONE;

    private final ActivityResultLauncher<DocumentScannerConfiguration> documentScanResult = registerForActivityResult(new ScanResult(),
            result -> {
                if (!result) {
                    Log.i(logTag, "canceled scan process");
                    return;
                }

                // Proceed depending on the previously selected option
                switch (selectedOption) {
                    case PDF_GENERATING:
                        generatePDF(null);
                        break;
                    case DATA_EXTRACTION:
                        openDataResultActivity(null);
                        break;
                    case TEXT_RECOGNITION:
                        openTextResultActivity(null);
                        break;
                    default:
                        Log.i(logTag, "Select an input option first");
                        break;
                }
            }
    );

    //declare an image picker activity launcher in single-select mode which is used to generate a pdf from the selected image
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageForPDFGenerating =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    generatePDF(uri);
                } else {
                    Log.i(logTag, "Canceled image import");
                }
            });

    //declare an image picker activity launcher in single-select mode which is used to extract data from the selected image
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageForDataExtraction =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    openDataResultActivity(uri);
                } else {
                    Log.i(logTag, "Canceled image import");
                }
            });

    //declare an image picker activity launcher in single-select mode which is used to recognize text from the selected image
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageForTextRecognition =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    openTextResultActivity(uri);
                } else {
                    Log.i(logTag, "Canceled image import");
                }
            });


    // Declare a PDF picker activity launcher for recognizing text from the selected PDF
    private final ActivityResultLauncher<String> pickPDFForTextRecognition = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    openTextResultActivity(uri);
                } else {
                    Log.i(logTag, "canceled PDF import");
                }
            }
    );

    // Declare a PDF picker activity launcher for extracting data from the selected PDF
    private final ActivityResultLauncher<String> pickPDFForDataExtraction = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    openDataResultActivity(uri);
                } else {
                    Log.i(logTag, "canceled PDF import");
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (App.licenseKeyMissing) {
            // The Docutain SDK needs to be initialized prior to using any functionality of it.
            // A valid license key is required.
            // Visit https://sdk.docutain.com/TrialLicense?Source=786945 to get a trial license key for free
            // Once you got a license, set it in App.java
            showLicenseEmptyInfo();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(item -> {
            switch (item.getType()) {
                case DOCUMENT_SCAN:
                    selectedOption = ListAdapter.ItemType.DOCUMENT_SCAN;
                    startScan();
                    break;
                case DATA_EXTRACTION:
                    selectedOption = ListAdapter.ItemType.DATA_EXTRACTION;
                    startDataExtraction();
                    break;
                case TEXT_RECOGNITION:
                    selectedOption = ListAdapter.ItemType.TEXT_RECOGNITION;
                    startTextRecognition();
                    break;
                case PDF_GENERATING:
                    selectedOption = ListAdapter.ItemType.PDF_GENERATING;
                    startPDFGenerating();
                    break;
                default:
                    selectedOption = ListAdapter.ItemType.NONE;
                    Log.i(logTag, "invalid item clicked");
                    break;
            }
        }));

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void startScan() {
        // Define a DocumentScannerConfiguration to alter the scan process and define a custom theme to match your branding
        DocumentScannerConfiguration scanConfig = new DocumentScannerConfiguration();
        scanConfig.setAllowCaptureModeSetting(true); // Defaults to false
        scanConfig.getPageEditConfig().setAllowPageFilter(true); // Defaults to true
        scanConfig.getPageEditConfig().setAllowPageRotation(true); // Defaults to true
        // Alter the onboarding image source if you like
        // scanConfig.setOnboardingImageSource = ...

        // Detailed information about theming possibilities can be found here [https://docs.docutain.com/docs/Android/theming]
        scanConfig.setTheme(R.style.Theme_DocutainSDK);
        documentScanResult.launch(scanConfig);
    }

    private void startPDFImport() {
        switch (selectedOption) {
            case PDF_GENERATING:
                Log.i(logTag, "Generating a PDF from a file which is already a PDF makes no sense, please scan a document or import an image.");
                break;
            case DATA_EXTRACTION:
                pickPDFForDataExtraction.launch("application/pdf");
                break;
            case TEXT_RECOGNITION:
                pickPDFForTextRecognition.launch("application/pdf");
                break;
            default:
                Log.i(logTag, "Select an input option first");
                break;
        }
    }

    private void startImageImport() {
        switch (selectedOption) {
            case PDF_GENERATING:
                // Launch the photo picker and let the user choose only images.
                pickImageForPDFGenerating.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

                break;
            case DATA_EXTRACTION:
                pickImageForDataExtraction.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
                break;
            case TEXT_RECOGNITION:
                pickImageForTextRecognition.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
                break;
            default:
                Log.i(logTag, "Select an input option first");
                break;
        }
    }

    private void startDataExtraction() {
        showInputOptionAlert();
    }

    private void startTextRecognition() {
        showInputOptionAlert();
    }

    private void startPDFGenerating() {
        showInputOptionAlert();
    }

    private void showInputOptionAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.title_input_option)
                .setItems(
                        new CharSequence[]{
                                getString(R.string.input_option_scan),
                                getString(R.string.input_option_PDF),
                                getString(R.string.input_option_image)
                        }, (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    startScan();
                                    break;
                                case 1:
                                    startPDFImport();
                                    break;
                                case 2:
                                    startImageImport();
                                    break;
                            }
                        });

        builder.create().show();
    }

    private void generatePDF(Uri uri) {
        pdfFuture = executorService.submit(() -> {
            if (uri != null) {
                //if an uri is available it means we have imported a file. If so, we need to load it into the SDK first
                if (!DocumentDataReader.loadFile(uri)) {
                    //an error occured, get the latest error message
                    Log.i(logTag, "DocumentDataReader.loadFile failed, last error: " + DocutainSDK.getLastError());
                    return null;
                }
            }
            //define the output file for the PDF
            File file = new File(getFilesDir(), "SamplePDF");
            //generate the PDF from the currently loaded document
            //the generated PDF also contains the detected text, making the PDF searchable
            //see [https://docs.docutain.com/docs/Android/pdfCreation] for more details
            File fileReturn = Document.writePDF(file, true, Document.PDFPageFormat.A4);
            if (fileReturn == null) {
                //an error occured, get the latest error message
                Log.e(logTag, "Document.writePDF failed, last error: " + DocutainSDK.getLastError());
                return null;
            }
            return fileReturn;
        });

        // Handle errors in a separate try-catch block, as Future.get() may throw exceptions
        executorService.execute(() -> {
            try {
                File pdfFile = pdfFuture.get(); // Blocking call to wait for the task to complete
                if (pdfFile != null) {
                    //display the PDF by using the system's default viewer for demonstration purposes
                    Uri pdfUri = FileProvider.getUriForFile(MainActivity.this, "de.docutain.sdk.docutain_sdk_example_android_java.attachments", pdfFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setData(pdfUri);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        Log.i(logTag, "No Activity available for displaying the PDF");
                    }
                }
            } catch (Exception e) {
                // Handle any errors here.
                Log.e(logTag, "Error generating PDF: " + e.getMessage());
            }
        });
    }

    private void openDataResultActivity(Uri uri) {
        Intent intent = new Intent(MainActivity.this, DataResultActivity.class);
        if (uri != null) {
            intent.putExtra("uri", uri);
        }
        startActivity(intent);
    }

    private void openTextResultActivity(Uri uri) {
        Intent intent = new Intent(MainActivity.this, TextResultActivity.class);
        if (uri != null) {
            intent.putExtra("uri", uri);
        }
        startActivity(intent);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void showLicenseEmptyInfo() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("License empty")
                .setMessage("A valid license key is required. Please visit our website in order to create a license key for free.")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel button click
                })
                .setPositiveButton("Get License", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sdk.docutain.com/TrialLicense?Source=786945"));
                    try {
                        startActivity(intent);
                    } catch(ActivityNotFoundException ex){
                        Log.e(logTag, "No Browser App available, please contact us manually via sdk@Docutain.com");
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pdfFuture != null && !pdfFuture.isDone()) {
            pdfFuture.cancel(true); // Cancel the task if it's still running
        }
        executorService.shutdown();
    }
}