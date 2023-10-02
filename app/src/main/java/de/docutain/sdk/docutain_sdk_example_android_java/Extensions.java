package de.docutain.sdk.docutain_sdk_example_android_java;

import android.content.Intent;
import android.os.Build;

public class Extensions {

    public static android.net.Uri parcelable(Intent intent, String key) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getParcelableExtra(key, android.net.Uri.class);
        } else {
            return intent.getParcelableExtra(key);
        }
    }


//    public static <T extends Parcelable> T parcelable(Intent intent, String key) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            return intent.getParcelableExtra(key);
//        } else {
//            return (T) intent.getParcelableExtra(key);
//        }
//    }

//    public static <T extends Parcelable> T parcelable(Bundle bundle, String key) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            return bundle.getParcelable(key);
//        } else {
//            return (T) bundle.getParcelable(key);
//        }
//    }
}
