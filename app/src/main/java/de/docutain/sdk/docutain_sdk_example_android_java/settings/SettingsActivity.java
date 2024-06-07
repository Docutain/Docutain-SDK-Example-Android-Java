package de.docutain.sdk.docutain_sdk_example_android_java.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.docutain.sdk.docutain_sdk_example_android_java.R;
import de.docutain.sdk.ui.ScanFilter;

public class SettingsActivity extends AppCompatActivity {
    private SettingsMultiViewsAdapter settingsAdapter;
    private SettingsSharedPreferences settingsSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initToolbar();
        initRestButton();

        settingsSharedPreferences = new SettingsSharedPreferences(this);
        if (settingsSharedPreferences.isEmpty())
            settingsSharedPreferences.defaultSettings();

        settingsAdapter = new SettingsMultiViewsAdapter(preparingData(), settingsSharedPreferences);
        RecyclerView recyclerView = findViewById(R.id.settings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(settingsAdapter);
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private List<SettingsMultiItems> preparingData() {
        SettingsMultiItems.ColorItem colorPrimaryItem = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorPrimary);
        SettingsMultiItems.ColorItem colorSecondaryItem = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorSecondary);
        SettingsMultiItems.ColorItem colorOnSecondaryItem = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorOnSecondary);
        SettingsMultiItems.ColorItem colorScanButtonsLayoutBackground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorScanButtonsLayoutBackground);
        SettingsMultiItems.ColorItem colorScanButtonsForeground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorScanButtonsForeground);
        SettingsMultiItems.ColorItem colorScanPolygon = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorScanPolygon);
        SettingsMultiItems.ColorItem colorBottomBarBackground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorBottomBarBackground);
        SettingsMultiItems.ColorItem colorBottomBarForeground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorBottomBarForeground);
        SettingsMultiItems.ColorItem colorTopBarBackground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorTopBarBackground);
        SettingsMultiItems.ColorItem colorTopBarForeground = settingsSharedPreferences.getColorItem(SettingsSharedPreferences.ColorItem.ColorTopBarForeground);

        boolean allowCaptureModeSetting = settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.AllowCaptureModeSetting).checkValue;
        boolean autoCapture = settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.AutoCapture).checkValue;
        boolean autoCrop = settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.AutoCrop).checkValue;
        boolean multiPage = settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.MultiPage).checkValue;
        boolean preCaptureFocus = settingsSharedPreferences.getScanItem(SettingsSharedPreferences.ScanSettings.PreCaptureFocus).checkValue;
        ScanFilter defaultScanFilter = settingsSharedPreferences.getScanFilterItem(SettingsSharedPreferences.ScanSettings.DefaultScanFilter).scanValue;

        boolean allowPageFilter = settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageFilter).checkValue;
        boolean allowPageRotation = settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageRotation).checkValue;
        boolean allowPageArrangement = settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageArrangement).checkValue;
        boolean allowPageCropping = settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.AllowPageCropping).checkValue;
        boolean pageArrangementShowDeleteButton = settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.PageArrangementShowDeleteButton).checkValue;
        boolean pageArrangementShowPageNumber = settingsSharedPreferences.getEditItem(SettingsSharedPreferences.EditSettings.PageArrangementShowPageNumber).checkValue;

        List<SettingsMultiItems> items = new ArrayList<>();
        items.add(new SettingsMultiItems.TitleItem(R.string.color_settings));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_primary_title, R.string.color_primary_subtitle, colorPrimaryItem.lightCircle, colorPrimaryItem.darkCircle, SettingsSharedPreferences.ColorItem.ColorPrimary));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_secondary_title, R.string.color_secondary_subtitle, colorSecondaryItem.lightCircle, colorSecondaryItem.darkCircle, SettingsSharedPreferences.ColorItem.ColorSecondary));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_on_secondary_title, R.string.color_secondary_subtitle, colorOnSecondaryItem.lightCircle, colorOnSecondaryItem.darkCircle, SettingsSharedPreferences.ColorItem.ColorOnSecondary));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_scan_layout_title, R.string.color_scan_layout_subtitle, colorScanButtonsLayoutBackground.lightCircle, colorScanButtonsLayoutBackground.darkCircle, SettingsSharedPreferences.ColorItem.ColorScanButtonsLayoutBackground));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_scan_foreground_title, R.string.color_scan_foreground_subtitle, colorScanButtonsForeground.lightCircle, colorScanButtonsForeground.darkCircle, SettingsSharedPreferences.ColorItem.ColorScanButtonsForeground));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_scan_polygon_title, R.string.color_scan_polygon_subtitle, colorScanPolygon.lightCircle, colorScanPolygon.darkCircle, SettingsSharedPreferences.ColorItem.ColorScanPolygon));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_bottom_bar_background_title, R.string.color_bottom_bar_background_subtitle, colorBottomBarBackground.lightCircle, colorBottomBarBackground.darkCircle, SettingsSharedPreferences.ColorItem.ColorBottomBarBackground));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_bottom_bar_forground_title, R.string.color_bottom_bar_forground_subtitle, colorBottomBarForeground.lightCircle, colorBottomBarForeground.darkCircle, SettingsSharedPreferences.ColorItem.ColorBottomBarForeground));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_top_bar_background_title, R.string.color_top_bar_background_subtitle, colorTopBarBackground.lightCircle, colorTopBarBackground.darkCircle, SettingsSharedPreferences.ColorItem.ColorTopBarBackground));
        items.add(new SettingsMultiItems.ColorItem(R.string.color_top_bar_forground_title, R.string.color_top_bar_forground_subtitle, colorTopBarForeground.lightCircle, colorTopBarForeground.darkCircle, SettingsSharedPreferences.ColorItem.ColorTopBarForeground));
        items.add(new SettingsMultiItems.TitleItem(R.string.scan_settings));
        items.add(new SettingsMultiItems.ScanSettingsItem(R.string.capture_mode_setting_title, R.string.capture_mode_setting_subtitle, allowCaptureModeSetting, SettingsSharedPreferences.ScanSettings.AllowCaptureModeSetting));
        items.add(new SettingsMultiItems.ScanSettingsItem(R.string.auto_capture_setting_title, R.string.auto_capture_setting_subtitle, autoCapture, SettingsSharedPreferences.ScanSettings.AutoCapture));
        items.add(new SettingsMultiItems.ScanSettingsItem(R.string.auto_crop_setting_title, R.string.auto_crop_setting_subtitle, autoCrop, SettingsSharedPreferences.ScanSettings.AutoCrop));
        items.add(new SettingsMultiItems.ScanSettingsItem(R.string.multi_page_setting_title, R.string.multi_page_setting_subtitle, multiPage, SettingsSharedPreferences.ScanSettings.MultiPage));
        items.add(new SettingsMultiItems.ScanSettingsItem(R.string.pre_capture_setting_title, R.string.pre_capture_setting_subtitle, preCaptureFocus, SettingsSharedPreferences.ScanSettings.PreCaptureFocus));
        items.add(new SettingsMultiItems.ScanFilterItem(R.string.default_scan_setting_title, R.string.default_scan_setting_subtitle, defaultScanFilter, SettingsSharedPreferences.ScanSettings.DefaultScanFilter));
        items.add(new SettingsMultiItems.TitleItem(R.string.edit_settings));
        items.add(new SettingsMultiItems.EditItem(R.string.allow_page_filter_setting_title, R.string.allow_page_filter_setting_subtitle, allowPageFilter, SettingsSharedPreferences.EditSettings.AllowPageFilter));
        items.add(new SettingsMultiItems.EditItem(R.string.allow_page_rotation_setting_title, R.string.allow_page_rotation_setting_subtitle, allowPageRotation, SettingsSharedPreferences.EditSettings.AllowPageRotation));
        items.add(new SettingsMultiItems.EditItem(R.string.allow_page_arrangement_setting_title, R.string.allow_page_arrangement_setting_subtitle, allowPageArrangement, SettingsSharedPreferences.EditSettings.AllowPageArrangement));
        items.add(new SettingsMultiItems.EditItem(R.string.allow_page_cropping_setting_title, R.string.allow_page_cropping_setting_subtitle, allowPageCropping, SettingsSharedPreferences.EditSettings.AllowPageCropping));
        items.add(new SettingsMultiItems.EditItem(R.string.page_arrangement_delete_setting_title, R.string.page_arrangement_delete_setting_subtitle, pageArrangementShowDeleteButton, SettingsSharedPreferences.EditSettings.PageArrangementShowDeleteButton));
        items.add(new SettingsMultiItems.EditItem(R.string.page_arrangement_number_setting_title, R.string.page_arrangement_number_setting_subtitle, pageArrangementShowPageNumber, SettingsSharedPreferences.EditSettings.PageArrangementShowPageNumber));
//        items.add(new SettingsMultiItems.TitleItem(R.string.rest_settings));

//        Runnable onClicked = () -> {
//            settingsSharedPreferences.defaultSettings();
//            settingsAdapter.refresh(preparingData());
//        };

//        items.add(new SettingsMultiItems.RestItem(onClicked));
        return items;
    }


    private void initRestButton() {
        findViewById(R.id.rest_button_settings).setOnClickListener(v -> {
            settingsSharedPreferences.defaultSettings();
            settingsAdapter.refresh(preparingData());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}