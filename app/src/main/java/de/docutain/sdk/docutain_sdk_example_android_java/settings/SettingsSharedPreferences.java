package de.docutain.sdk.docutain_sdk_example_android_java.settings;


import static de.docutain.sdk.docutain_sdk_example_android_java.utils.ColorUtils.getModeColor;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import de.docutain.sdk.docutain_sdk_example_android_java.R;
import de.docutain.sdk.docutain_sdk_example_android_java.settings.SettingsMultiItems;
import de.docutain.sdk.ui.ScanFilter;

public class SettingsSharedPreferences {
    private Context context;

    public SettingsSharedPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private void saveColorItem(ColorItem key, String lightColor, String darkColor) {
        sharedPreferences.edit()
                .putString(key.toString().toLowerCase() + LIGHT_COLOR_KEY, lightColor)
                .putString(key.toString().toLowerCase() + DARK_COLOR_KEY, darkColor)
                .apply();
    }

    public void saveColorItemLight(String key, String color) {
        sharedPreferences.edit()
                .putString(key.toLowerCase() + LIGHT_COLOR_KEY, color)
                .apply();
    }

    public void saveColorItemDark(String key, String color) {
        sharedPreferences.edit()
                .putString(key.toLowerCase() + DARK_COLOR_KEY, color)
                .apply();
    }

    public void saveEditItem(EditSettings key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key.toString().toLowerCase() + EDIT_VALUE_KEY, value)
                .apply();
    }

    public void saveScanItem(ScanSettings key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key.toString().toLowerCase() + SCAN_VALUE_KEY, value)
                .apply();
    }

    public void saveScanFilterItem(ScanSettings key, int value) {
        sharedPreferences.edit()
                .putInt(key.toString().toLowerCase() + FILTER_VALUE_KEY, value)
                .apply();
    }

    public SettingsMultiItems.ColorItem getColorItem(ColorItem key) {
        String color1 = sharedPreferences.getString(key.toString().toLowerCase() + LIGHT_COLOR_KEY, "");
        String color2 = sharedPreferences.getString(key.toString().toLowerCase() + DARK_COLOR_KEY, "");
        return new SettingsMultiItems.ColorItem(0, 0, color1, color2, key);
    }

    public SettingsMultiItems.EditItem getEditItem(EditSettings key) {
        boolean checkValue = sharedPreferences.getBoolean(key.toString().toLowerCase() + EDIT_VALUE_KEY, false);
        return new SettingsMultiItems.EditItem(0, 0, checkValue, key);
    }

    public SettingsMultiItems.ScanSettingsItem getScanItem(ScanSettings key) {
        boolean scanValue = sharedPreferences.getBoolean(key.toString().toLowerCase() + SCAN_VALUE_KEY, false);
        return new SettingsMultiItems.ScanSettingsItem(0, 0, scanValue, key);
    }

    public SettingsMultiItems.ScanFilterItem getScanFilterItem(ScanSettings key) {
        int scanValue = sharedPreferences.getInt(key.toString().toLowerCase() + FILTER_VALUE_KEY, ScanFilter.ILLUSTRATION.ordinal());
        return new SettingsMultiItems.ScanFilterItem(0, 0, ScanFilter.values()[scanValue], key);
    }

    public boolean isEmpty() {
        return sharedPreferences.getAll().isEmpty();
    }


    private void defaultColorPrimary(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorPrimary, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorPrimary, context, true);
        }
        saveColorItem(ColorItem.ColorPrimary, lightColor, darkColor);
    }

    private void defaultColorSecondary(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorSecondary, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorSecondary, context, true);
        }
        saveColorItem(ColorItem.ColorSecondary, lightColor, darkColor);
    }

    private void defaultColorOnSecondary(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorOnSecondary, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorOnSecondary, context, true);
        }
        saveColorItem(ColorItem.ColorOnSecondary, lightColor, darkColor);
    }

    private void defaultColorScanButtonsLayoutBackground(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorScanButtonsLayoutBackground, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorScanButtonsLayoutBackground, context, true);
        }
        saveColorItem(ColorItem.ColorScanButtonsLayoutBackground, lightColor, darkColor);
    }

    private void defaultColorScanButtonsForeground(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorScanButtonsForeground, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorScanButtonsForeground, context, true);
        }
        saveColorItem(ColorItem.ColorScanButtonsForeground, lightColor, darkColor);
    }

    private void defaultColorScanPolygon(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorScanPolygon, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorScanPolygon, context, true);
        }
        saveColorItem(ColorItem.ColorScanPolygon, lightColor, darkColor);
    }

    private void defaultColorBottomBarBackground(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorBottomBarBackground, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorBottomBarBackground, context, true);
        }
        saveColorItem(ColorItem.ColorBottomBarBackground, lightColor, darkColor);
    }

    private void defaultColorBottomBarForeground(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorBottomBarForeground, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorBottomBarForeground, context, true);
        }
        saveColorItem(ColorItem.ColorBottomBarForeground, lightColor, darkColor);
    }

    private void defaultColorTopBarBackground(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorTopBarBackground, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorTopBarBackground, context, true);
        }
        saveColorItem(ColorItem.ColorTopBarBackground, lightColor, darkColor);
    }

    private void defaultColorTopBarForeground(String lightColor, String darkColor) {
        if (lightColor == null || lightColor.isEmpty()) {
            lightColor = getModeColor(R.color.docutain_colorTopBarForeground, context, false);
        }
        if (darkColor == null || darkColor.isEmpty()) {
            darkColor = getModeColor(R.color.docutain_colorTopBarForeground, context, true);
        }
        saveColorItem(ColorItem.ColorTopBarForeground, lightColor, darkColor);
    }


    public void defaultSettings() {

        defaultColorPrimary("","");
        defaultColorSecondary("","");
        defaultColorOnSecondary("","");
        defaultColorScanButtonsLayoutBackground("","");
        defaultColorScanButtonsForeground("","");
        defaultColorScanPolygon("","");
        defaultColorBottomBarBackground("","");
        defaultColorBottomBarForeground("","");
        defaultColorTopBarBackground("","");
        defaultColorTopBarForeground("","");

        saveScanItem(ScanSettings.AllowCaptureModeSetting, false);
        saveScanItem(ScanSettings.AutoCapture, true);
        saveScanItem(ScanSettings.AutoCrop, true);
        saveScanItem(ScanSettings.MultiPage, true);
        saveScanItem(ScanSettings.PreCaptureFocus, true);
        saveScanFilterItem(ScanSettings.DefaultScanFilter, ScanFilter.ILLUSTRATION.ordinal());

        saveEditItem(EditSettings.AllowPageFilter, true);
        saveEditItem(EditSettings.AllowPageRotation, true);
        saveEditItem(EditSettings.AllowPageArrangement, true);
        saveEditItem(EditSettings.AllowPageCropping, true);
        saveEditItem(EditSettings.PageArrangementShowDeleteButton, false);
        saveEditItem(EditSettings.PageArrangementShowPageNumber, true);
    }

    private String getColor(int color) {
        return "#" + Integer.toHexString(ContextCompat.getColor(context, color)).toUpperCase();
    }

    public enum ColorItem {
        ColorPrimary,
        ColorSecondary,
        ColorOnSecondary,
        ColorScanButtonsLayoutBackground,
        ColorScanButtonsForeground,
        ColorScanPolygon,
        ColorBottomBarBackground,
        ColorBottomBarForeground,
        ColorTopBarBackground,
        ColorTopBarForeground
    }

    public enum ColorType {
        Light,
        Dark
    }

    public enum ScanSettings {
        AllowCaptureModeSetting,
        AutoCapture,
        AutoCrop,
        MultiPage,
        PreCaptureFocus,
        DefaultScanFilter
    }

    public enum EditSettings {
        AllowPageFilter,
        AllowPageRotation,
        AllowPageArrangement,
        AllowPageCropping,
        PageArrangementShowDeleteButton,
        PageArrangementShowPageNumber
    }

    private final SharedPreferences sharedPreferences;
    private static final String PREF_FILE_NAME = "settings_prefs";
    public static final String LIGHT_COLOR_KEY = "_light_color";
    public static final String DARK_COLOR_KEY = "_dark_color";
    public static final String EDIT_VALUE_KEY = "_edit_value";
    public static final String SCAN_VALUE_KEY = "_scan_value";
    public static final String FILTER_VALUE_KEY = "_filter_value";
}
