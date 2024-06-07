package de.docutain.sdk.docutain_sdk_example_android_java;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.docutain.sdk.Document;
import de.docutain.sdk.DocutainSDK;
import de.docutain.sdk.Logger;
import de.docutain.sdk.dataextraction.AnalyzeConfiguration;
import de.docutain.sdk.dataextraction.DocumentDataReader;
import de.docutain.sdk.docutain_sdk_example_android_java.settings.SettingsActivity;
import de.docutain.sdk.docutain_sdk_example_android_java.settings.SettingsMultiItems;
import de.docutain.sdk.docutain_sdk_example_android_java.settings.SettingsSharedPreferences;
import de.docutain.sdk.ui.DocumentScannerConfiguration;
import de.docutain.sdk.ui.DocutainColor;
import de.docutain.sdk.ui.ScanResult;
import de.docutain.sdk.ui.Source;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private SettingsSharedPreferences settingsSharedPreferences;
    private Future<File> pdfFuture;
    private final String logTag = "DocutainSDK";
    private ListAdapter.ItemType selectedOption = ListAdapter.ItemType.NONE;

    //declare an ActivityResultLauncher which starts the document scanner and
    //returns once the user finished scanning or canceled the scan process
    //see [https://docs.docutain.com/docs/Android/docScan] for more information
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


    //declare a PDF picker activity launcher for importing PDF documents
    private final ActivityResultLauncher<String> pickPDF = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    // Proceed depending on the previously selected option
                    switch (selectedOption) {
                        case PDF_GENERATING:
                            generatePDF(uri);
                            break;
                        case DATA_EXTRACTION:
                            openDataResultActivity(uri);
                            break;
                        case TEXT_RECOGNITION:
                            openTextResultActivity(uri);
                            break;
                        default:
                            Log.i(logTag, "Select an input option first");
                            break;
                    }
                } else {
                    Log.i(logTag, "canceled PDF import");
                }
            }
    );

    //A valid license key is required, you can generate one on our website https://sdk.docutain.com/TrialLicense?Source=786945
    private final String licenseKey = "YOUR_LICENSE_KEY_HERE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //the Docutain SDK needs to be initialized prior to using any functionality of it
        //a valid license key is required, you can generate one on our website https://sdk.docutain.com/TrialLicense?Source=786945
        if(!DocutainSDK.initSDK(getApplication(), licenseKey)){
            //init of Docutain SDK failed, get the last error message
            Log.e(logTag,"Initialization of the Docutain SDK failed: " + DocutainSDK.getLastError());
            //your logic to deactivate access to SDK functionality
            if(licenseKey == "YOUR_LICENSE_KEY_HERE"){
                showLicenseEmptyInfo();
            }else{
                showLicenseErrorInfo();
            }
            return;
        }

        //If you want to use text detection (OCR) and/or data extraction features, you need to set the AnalyzeConfiguration
        //in order to start all the necessary processes
        AnalyzeConfiguration analyzeConfig = new AnalyzeConfiguration();
        analyzeConfig.setReadBIC(true);
        analyzeConfig.setReadPaymentState(true);
        if(!DocumentDataReader.setAnalyzeConfiguration(analyzeConfig)){
            Log.e(logTag,"Setting AnalyzeConfiguration failed: " + DocutainSDK.getLastError());
        }

        //Depending on your needs, you can set the Logger's level
        Logger.setLogLevel(Logger.Level.VERBOSE);

        //Depending on the log level that you have set, some temporary files get written on the filesystem
        //You can delete all temporary files by using the following method
        DocutainSDK.deleteTempFiles(true);

        settingsSharedPreferences = new SettingsSharedPreferences(this);
        if(settingsSharedPreferences.isEmpty())
            settingsSharedPreferences.defaultSettings();

        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(item -> {
            switch (item.getType()) {
                case DOCUMENT_SCAN:
                    selectedOption = ListAdapter.ItemType.DOCUMENT_SCAN;
                    startScan(false);
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
                case SETTINGS:
                    selectedOption = ListAdapter.ItemType.SETTINGS;
                    startActivity(new Intent(this, SettingsActivity.class));
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

    private void startScan(Boolean imageImport) {
        //There are a lot of settings to configure the scanner to match your specific needs
        //Check out the documentation to learn more https://docs.docutain.com/docs/Android/docScan#change-default-scan-behaviour
        DocumentScannerConfiguration scanConfig = new DocumentScannerConfiguration();

        if(imageImport){
            scanConfig.setSource(Source.GALLERY_MULTIPLE);
        }

        //In this sample app we provide a settings page which the user can use to alter the scan settings
        //The settings are stored in and read from SharedPreferences
        //This is supposed to be just an example, you do not need to implement it in that exact way
        //If you do not want to provide your users the possibility to alter the settings themselves at all
        //You can just set the settings according to the apps needs

        //set scan settings
        scanConfig.setAllowCaptureModeSetting(settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.AllowCaptureModeSetting).checkValue);
        scanConfig.setAutoCapture(settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.AutoCapture).checkValue);
        scanConfig.setAutoCrop(settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.AutoCrop).checkValue);
        scanConfig.setMultiPage(settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.MultiPage).checkValue);
        scanConfig.setPreCaptureFocus(settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.PreCaptureFocus).checkValue);
        scanConfig.setDefaultScanFilter(settingsSharedPreferences.getScanFilterItem(SettingsSharedPreferences.ScanSettings.DefaultScanFilter).scanValue);

        //set edit settings
        scanConfig.getPageEditConfig()
                .setAllowPageFilter(settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageFilter).checkValue);
        scanConfig.getPageEditConfig()
                .setAllowPageRotation(settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageRotation).checkValue);
        scanConfig.getPageEditConfig()
                .setAllowPageArrangement(settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageArrangement).checkValue);
        scanConfig.getPageEditConfig()
                .setAllowPageCropping(settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageCropping).checkValue);
        scanConfig.getPageEditConfig()
                .setPageArrangementShowDeleteButton(settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.PageArrangementShowDeleteButton).checkValue);
        scanConfig.getPageEditConfig()
                .setPageArrangementShowPageNumber(settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.PageArrangementShowPageNumber).checkValue);

        //set color settings
        SettingsMultiItems.ColorItem colorPrimary = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorPrimary);
        scanConfig.getColorConfig().setColorPrimary(new DocutainColor(colorPrimary.lightCircle, colorPrimary.darkCircle));
        SettingsMultiItems.ColorItem colorSecondary = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorSecondary);
        scanConfig.getColorConfig().setColorSecondary(new DocutainColor(colorSecondary.lightCircle, colorSecondary.darkCircle));
        SettingsMultiItems.ColorItem colorOnSecondary = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorOnSecondary);
        scanConfig.getColorConfig().setColorOnSecondary(new DocutainColor(colorOnSecondary.lightCircle, colorOnSecondary.darkCircle));
        SettingsMultiItems.ColorItem colorScanButtonsLayoutBackground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorScanButtonsLayoutBackground);
        scanConfig.getColorConfig().setColorScanButtonsLayoutBackground(new DocutainColor(colorScanButtonsLayoutBackground.lightCircle, colorScanButtonsLayoutBackground.darkCircle));
        SettingsMultiItems.ColorItem colorScanButtonsForeground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorScanButtonsForeground);
        scanConfig.getColorConfig().setColorScanButtonsForeground(new DocutainColor(colorScanButtonsForeground.lightCircle, colorScanButtonsForeground.darkCircle));
        SettingsMultiItems.ColorItem colorScanPolygon = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorScanPolygon);
        scanConfig.getColorConfig().setColorScanPolygon(new DocutainColor(colorScanPolygon.lightCircle, colorScanPolygon.darkCircle));
        SettingsMultiItems.ColorItem colorBottomBarBackground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorBottomBarBackground);
        scanConfig.getColorConfig().setColorBottomBarBackground(new DocutainColor(colorBottomBarBackground.lightCircle, colorBottomBarBackground.darkCircle));
        SettingsMultiItems.ColorItem colorBottomBarForeground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorBottomBarForeground);
        scanConfig.getColorConfig().setColorBottomBarForeground(new DocutainColor(colorBottomBarForeground.lightCircle, colorBottomBarForeground.darkCircle));
        SettingsMultiItems.ColorItem colorTopBarBackground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorTopBarBackground);
        scanConfig.getColorConfig().setColorTopBarBackground(new DocutainColor(colorTopBarBackground.lightCircle, colorTopBarBackground.darkCircle));
        SettingsMultiItems.ColorItem colorTopBarForeground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorTopBarForeground);
        scanConfig.getColorConfig().setColorTopBarForeground(new DocutainColor(colorTopBarForeground.lightCircle, colorTopBarForeground.darkCircle));

        // Alter the onboarding image source if you like
        // scanConfig.setOnboardingImageSource

        // Detailed information about theming possibilities can be found here [https://docs.docutain.com/docs/Android/theming]
        //scanConfig.setTheme

        documentScanResult.launch(scanConfig);
    }

    private void startPDFImport() {
        pickPDF.launch("application/pdf");
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
                                    startScan(false);
                                    break;
                                case 1:
                                    startPDFImport();
                                    break;
                                case 2:
                                    startScan(true);
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
        new MaterialAlertDialogBuilder(this).setTitle("License empty")
                .setMessage("A valid license key is required. Please click \"GET LICENSE\" in order to create a free trial license key on our website.")
                .setPositiveButton("Get License", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sdk.docutain.com/TrialLicense?Source=786945"));
                    startActivity(intent);
                    finish();
                }).setCancelable(false).show();
    }

    private void showLicenseErrorInfo() {
        new MaterialAlertDialogBuilder(this).setTitle("License error")
                .setMessage("A valid license key is required. Please contact our support to get an extended trial license.")
                .setPositiveButton("Contact Support", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support.sdk@Docutain.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Trial License Error");
                    intent.putExtra(Intent.EXTRA_TEXT, "Please keep your following trial license key in this e-mail: " + licenseKey);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e(
                            logTag,
                            "No Mail App available, please contact us manually via sdk@Docutain.com"
                        );
                    }
                }).setCancelable(false).show();
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