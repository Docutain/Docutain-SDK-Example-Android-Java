package de.docutain.sdk.docutain_sdk_example_android_java.utils;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public class ColorUtils {

    public static String getModeColor(@ColorRes int colorResId, Context context, boolean isNight) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.uiMode = isNight ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        Context nightModeContext = context.createConfigurationContext(configuration);
        int color = ContextCompat.getColor(nightModeContext, colorResId);
        return "#" + Integer.toHexString(color).toUpperCase();
    }
}
