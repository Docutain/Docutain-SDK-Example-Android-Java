package de.docutain.sdk.docutain_sdk_example_android_java.settings;

import de.docutain.sdk.ui.ScanFilter;

public class SettingsMultiItems {

    public static final class TitleItem extends SettingsMultiItems {
        public int title;

        public TitleItem(int title) {
            this.title = title;
        }
    }

    public static final class ColorItem extends SettingsMultiItems {
        public int title;
        public int subtitle;
        public String lightCircle;
        public String darkCircle;
        public SettingsSharedPreferences.ColorItem colorKey;

        public ColorItem(int title, int subtitle, String lightCircle, String darkCircle, SettingsSharedPreferences.ColorItem colorKey) {
            this.title = title;
            this.subtitle = subtitle;
            this.lightCircle = lightCircle;
            this.darkCircle = darkCircle;
            this.colorKey = colorKey;
        }
    }

    public static final class ScanSettingsItem extends SettingsMultiItems {
        public int title;
        public int subtitle;
        public boolean checkValue;
        public SettingsSharedPreferences.ScanSettings scanKey;

        public ScanSettingsItem(int title, int subtitle, boolean checkValue, SettingsSharedPreferences.ScanSettings scanKey) {
            this.title = title;
            this.subtitle = subtitle;
            this.checkValue = checkValue;
            this.scanKey = scanKey;
        }
    }

    public static final class ScanFilterItem extends SettingsMultiItems {
        public int title;
        public int subtitle;
        public ScanFilter scanValue;
        public SettingsSharedPreferences.ScanSettings filterKey;

        public ScanFilterItem(int title, int subtitle, ScanFilter scanValue, SettingsSharedPreferences.ScanSettings filterKey) {
            this.title = title;
            this.subtitle = subtitle;
            this.scanValue = scanValue;
            this.filterKey = filterKey;
        }
    }

    public static final class EditItem extends SettingsMultiItems {
        public int title;
        public int subtitle;
        public boolean checkValue;
        public SettingsSharedPreferences.EditSettings editKey;

        public EditItem(int title, int subtitle, boolean checkValue, SettingsSharedPreferences.EditSettings editKey) {
            this.title = title;
            this.subtitle = subtitle;
            this.checkValue = checkValue;
            this.editKey = editKey;
        }
    }

    public static class RestItem extends SettingsMultiItems {
        public Runnable onClicked;
        public RestItem(Runnable onClicked) {
            this.onClicked = onClicked;
        }
    }

}