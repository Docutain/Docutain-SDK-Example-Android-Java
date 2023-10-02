package de.docutain.sdk.docutain_sdk_example_android_java;

import static de.docutain.sdk.dataextraction.DocumentDataReader.analyze;
import static de.docutain.sdk.docutain_sdk_example_android_java.Extensions.parcelable;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.docutain.sdk.DocutainSDK;
import de.docutain.sdk.dataextraction.DocumentDataReader;

public class DataResultActivity extends AppCompatActivity {

    private TextInputLayout textViewName1;
    private TextInputLayout textViewName2;
    private TextInputLayout textViewName3;
    private TextInputLayout textViewZipcode;
    private TextInputLayout textViewCity;
    private TextInputLayout textViewStreet;
    private TextInputLayout textViewPhone;
    private TextInputLayout textViewCustomerID;
    private TextInputLayout textViewIBAN;
    private TextInputLayout textViewBIC;
    private TextInputLayout textViewDate;
    private TextInputLayout textViewAmount;
    private TextInputLayout textViewInvoiceId;
    private TextInputLayout textViewReference;
    private TextInputLayout textViewPaymentState;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<Void> dataFuture;
    private final String logTag = "DocutainSDK";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_result);

        // Initialize the textViews which display the detected data
        textViewName1 = findViewById(R.id.textField_name1);
        textViewName2 = findViewById(R.id.textField_name2);
        textViewName3 = findViewById(R.id.textField_name3);
        textViewZipcode = findViewById(R.id.textField_zipcode);
        textViewCity = findViewById(R.id.textField_city);
        textViewStreet = findViewById(R.id.textField_street);
        textViewPhone = findViewById(R.id.textField_phone);
        textViewCustomerID = findViewById(R.id.textField_customerId);
        textViewIBAN = findViewById(R.id.textField_IBAN);
        textViewBIC = findViewById(R.id.textField_BIC);
        textViewDate = findViewById(R.id.textField_Date);
        textViewAmount = findViewById(R.id.textField_Amount);
        textViewInvoiceId = findViewById(R.id.textField_InvoiceId);
        textViewReference = findViewById(R.id.textField_reference);
        textViewPaymentState = findViewById(R.id.textField_paymentState);

        // Analyze the document and load the detected data
        loadData();
    }


    private void loadData() {
        dataFuture = executorService.submit(() -> {
            Uri uri = parcelable(getIntent(), "uri");
            if (uri != null) {
                // If a URI is available, it means we have imported a file.
                // If so, we need to load it into the SDK first.
                if (!DocumentDataReader.loadFile(uri)) {
                    // An error occurred, get the latest error message.
                    Log.e(logTag, "DocumentDataReader.loadFile failed, last error: " + DocutainSDK.getLastError());
                    return null;
                }
            }

            // Analyze the currently loaded document and get the detected data
            String analyzeData = DocumentDataReader.analyze();
            if (analyzeData.isEmpty()) {
                // No data detected
                return null;
            }

            // Detected data is returned as JSON, so serialize the data in order to extract the key-value pairs
            //see [https://docs.docutain.com/docs/Android/dataExtraction] for more information
            JSONObject jsonArray = new JSONObject(analyzeData);
            JSONObject address = jsonArray.getJSONObject("Address");
            String name1 = address.getString("Name1");
            String name2 = address.getString("Name2");
            String name3 = address.getString("Name3");
            String zipcode = address.getString("Zipcode");
            String city = address.getString("City");
            String street = address.getString("Street");
            String phone = address.getString("Phone");
            String customerId = address.getString("CustomerId");
            JSONArray bank = address.getJSONArray("Bank");
            String IBAN = "";
            String BIC = "";
            // TODO: Handle multiple bank accounts
            if (bank.length() > 0) {
                JSONObject object1 = bank.getJSONObject(0);
                IBAN = object1.getString("IBAN");
                BIC = object1.getString("BIC");
            }
            String date = jsonArray.getString("Date");
            String amount = jsonArray.getString("Amount");
            String invoiceId = jsonArray.getString("InvoiceId");
            String reference = jsonArray.getString("Reference");
            String paid = jsonArray.optString("PaymentState");

            // Load the text into the text fields if a value is detected
            String finalIBAN = IBAN;
            String finalBIC = BIC;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!name1.isEmpty()) {
                        textViewName1.getEditText().setText(name1);
                        textViewName1.setVisibility(View.VISIBLE);
                    }
                    if (!name2.isEmpty()) {
                        textViewName2.getEditText().setText(name2);
                        textViewName2.setVisibility(View.VISIBLE);
                    }
                    if (!name3.isEmpty()) {
                        textViewName3.getEditText().setText(name3);
                        textViewName3.setVisibility(View.VISIBLE);
                    }
                    if (!zipcode.isEmpty()) {
                        textViewZipcode.getEditText().setText(zipcode);
                        textViewZipcode.setVisibility(View.VISIBLE);
                    }
                    if (!city.isEmpty()) {
                        textViewCity.getEditText().setText(city);
                        textViewCity.setVisibility(View.VISIBLE);
                    }
                    if (!street.isEmpty()) {
                        textViewStreet.getEditText().setText(street);
                        textViewStreet.setVisibility(View.VISIBLE);
                    }
                    if (!phone.isEmpty()) {
                        textViewPhone.getEditText().setText(phone);
                        textViewPhone.setVisibility(View.VISIBLE);
                    }
                    if (!customerId.isEmpty()) {
                        textViewCustomerID.getEditText().setText(customerId);
                        textViewCustomerID.setVisibility(View.VISIBLE);
                    }
                    if (!finalIBAN.isEmpty()) {
                        String formattedIBAN = finalIBAN.replaceAll(".{4}", "$0 ");
                        textViewIBAN.getEditText().setText(formattedIBAN);
                        textViewIBAN.setVisibility(View.VISIBLE);
                    }
                    if (!finalBIC.isEmpty()) {
                        textViewBIC.getEditText().setText(finalBIC);
                        textViewBIC.setVisibility(View.VISIBLE);
                    }
                    if (!date.isEmpty()) {
                        textViewDate.getEditText().setText(date);
                        textViewDate.setVisibility(View.VISIBLE);
                    }
                    if (!amount.isEmpty() && (!amount.equals("0.00"))) {
                        textViewAmount.getEditText().setText(amount);
                        textViewAmount.setVisibility(View.VISIBLE);
                    }
                    if (!invoiceId.isEmpty()) {
                        textViewInvoiceId.getEditText().setText(invoiceId);
                        textViewInvoiceId.setVisibility(View.VISIBLE);
                    }
                    if (!reference.isEmpty()) {
                        textViewReference.getEditText().setText(reference);
                        textViewReference.setVisibility(View.VISIBLE);
                    }
                    if (!paid.isEmpty()) {
                        textViewPaymentState.getEditText().setText(paid);
                        textViewPaymentState.setVisibility(View.VISIBLE);
                    }
                    findViewById(R.id.activity_indicator).setVisibility(View.GONE);
                }
            });

            return null;
        });

        findViewById(R.id.activity_indicator).setVisibility(View.VISIBLE); // Show loading indicator

        // Handle errors in a separate try-catch block, as Future.get() may throw exceptions
        executorService.execute(() -> {
            try {
                dataFuture.get(); // Blocking call to wait for the task to complete
            } catch (Exception e) {
                // Handle any errors here.
                Log.e(logTag, "Error loading data: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataFuture != null && !dataFuture.isDone()) {
            dataFuture.cancel(true); // Cancel the task if it's still running
        }
        executorService.shutdown();
    }
}
