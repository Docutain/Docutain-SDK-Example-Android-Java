package de.docutain.sdk.docutain_sdk_example_android_java;

import android.app.Application;
import android.util.Log;
import de.docutain.sdk.Logger;
import de.docutain.sdk.DocutainSDK;
import de.docutain.sdk.dataextraction.AnalyzeConfiguration;
import de.docutain.sdk.dataextraction.DocumentDataReader;

public class App extends Application {
    private final String logTag = "DocutainSDK";
    private final String licenseKey = "YOUR_LICENSE_KEY_HERE";
    public static boolean licenseKeyMissing = false;

    @Override
    public void onCreate() {
        super.onCreate();

        // The Docutain SDK needs to be initialized prior to using any functionality of it.
        // A valid license key is required.
        // Visit https://sdk.docutain.com/TrialLicense?Source=786945 to get a trial license key for free
        if (!DocutainSDK.initSDK(this, licenseKey)) {
            // Initialization of the Docutain SDK failed, get the last error message
            Log.e(logTag, "Initialization of the Docutain SDK failed: " + DocutainSDK.getLastError());
            // Your logic to deactivate access to SDK functionality
            if (licenseKey.equals("YOUR_LICENSE_KEY_HERE")) {
                licenseKeyMissing = true;
            }
        }

        // If you want to use text detection (OCR) and/or data extraction features, you need to set the AnalyzeConfiguration
        // in order to start all the necessary processes
        AnalyzeConfiguration analyzeConfig = new AnalyzeConfiguration();
        analyzeConfig.setReadBIC(true);
        analyzeConfig.setReadPaymentState(true);
        if (!DocumentDataReader.setAnalyzeConfiguration(analyzeConfig)) {
            Log.e(logTag, "Setting AnalyzeConfiguration failed: " + DocutainSDK.getLastError());
        }

        // Depending on your needs, you can set the Logger's level
        Logger.setLogLevel(Logger.Level.VERBOSE);

        // Depending on the log level that you have set, some temporary files get written on the filesystem
        // You can delete all temporary files by using the following method
        DocutainSDK.deleteTempFiles(true);
    }
}