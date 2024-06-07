package de.docutain.sdk.docutain_sdk_example_android_java.settings;

import static de.docutain.sdk.docutain_sdk_example_android_java.settings.SettingsSharedPreferences.ColorType.Light;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import de.docutain.sdk.docutain_sdk_example_android_java.R;

public class SettingsMultiViewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<SettingsMultiItems> items;
    private static SettingsSharedPreferences settingsSharedPreferences ;

    public SettingsMultiViewsAdapter(List<SettingsMultiItems> items, SettingsSharedPreferences settingsSharedPreferences) {
        this.items = items;
        SettingsMultiViewsAdapter.settingsSharedPreferences = settingsSharedPreferences;
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TitleViewHolder(View itemView) {
            super(itemView);
        }

        void bind(SettingsMultiItems.TitleItem item) {
            TextView textView = itemView.findViewById(R.id.title_view_settings);
            textView.setText(item.title);
        }
    }

    class ColorSettingsViewHolder extends RecyclerView.ViewHolder {
        ColorSettingsViewHolder(View itemView) {
            super(itemView);
        }

        void bind(SettingsMultiItems.ColorItem item) {
            SettingsMultiItems.ColorItem cachedItem = settingsSharedPreferences.getColorItem(item.colorKey);

            TextView title = itemView.findViewById(R.id.title_settings_item);
            TextView subtitle = itemView.findViewById(R.id.subtitle_settings_item);
            View lightView = itemView.findViewById(R.id.light_circle_view);
            View darkView = itemView.findViewById(R.id.dark_circle_view);


            circleView(lightView, cachedItem.lightCircle);
            circleView(darkView, cachedItem.darkCircle);

            lightView.setOnClickListener(v -> colorPickerDialog(itemView.getContext(), lightView, item,
                    Light, cachedItem.lightCircle));
            darkView.setOnClickListener(v -> colorPickerDialog(itemView.getContext(), darkView, item,
                    SettingsSharedPreferences.ColorType.Dark, cachedItem.darkCircle));
            title.setText(item.title);
            subtitle.setText(item.subtitle);
        }
    }

    static class ScanSettingsViewHolder extends RecyclerView.ViewHolder {
        ScanSettingsViewHolder(View itemView) {
            super(itemView);
        }

        void bind(SettingsMultiItems.ScanSettingsItem item) {
            SettingsMultiItems.ScanSettingsItem cachedItem = settingsSharedPreferences.getScanItem(item.scanKey);

            TextView title = itemView.findViewById(R.id.title_edit_settings_item);
            TextView subtitle = itemView.findViewById(R.id.subtitle_edit_settings_item);
            SwitchCompat switchButton = itemView.findViewById(R.id.switch_edit_settings_item);

            title.setText(item.title);
            subtitle.setText(item.subtitle);

            switchButton.setOnCheckedChangeListener(null);
            switchButton.setChecked(cachedItem.checkValue);
            switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> settingsSharedPreferences.saveScanItem(item.scanKey, isChecked));
        }
    }

    class ScanFilterViewHolder extends RecyclerView.ViewHolder {
        ScanFilterViewHolder(View itemView) {
            super(itemView);
        }

        void bind(SettingsMultiItems.ScanFilterItem item) {
            SettingsMultiItems.ScanFilterItem cachedItem = settingsSharedPreferences.getScanFilterItem(item.filterKey);

            TextView title = itemView.findViewById(R.id.title_scan_settings_item);
            TextView subtitle = itemView.findViewById(R.id.subtitle_scan_settings_item);
            TextInputEditText inputScanFilter = itemView.findViewById(R.id.input_scan_filter_dialog);

            title.setText(item.title);
            subtitle.setText(item.subtitle);

            inputScanFilter.setText(cachedItem.scanValue.toString());
            inputScanFilter.setOnClickListener(v -> scanFilterDialog(itemView.getContext(), inputScanFilter, item));
        }
    }

    static class EditSettingsViewHolder extends RecyclerView.ViewHolder {
        EditSettingsViewHolder(View itemView) {
            super(itemView);
        }

        void bind(SettingsMultiItems.EditItem item) {
            SettingsMultiItems.EditItem cachedItem = settingsSharedPreferences.getEditItem(item.editKey);

            TextView title = itemView.findViewById(R.id.title_edit_settings_item);
            TextView subtitle = itemView.findViewById(R.id.subtitle_edit_settings_item);
            SwitchCompat switchButton = itemView.findViewById(R.id.switch_edit_settings_item);

            title.setText(item.title);
            subtitle.setText(item.subtitle);

            switchButton.setOnCheckedChangeListener(null);
            switchButton.setChecked(cachedItem.checkValue);
            switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> settingsSharedPreferences.saveEditItem(item.editKey, isChecked));
        }
    }

//    static class RestSettingsViewHolder extends RecyclerView.ViewHolder {
//        RestSettingsViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        void bind(SettingsMultiItems.RestItem item) {
//            Button restButton = itemView.findViewById(R.id.rest_button_settings);
//            restButton.setOnClickListener(v -> item.onClicked.run());
//        }
//    }

    @Override
    public int getItemViewType(int position) {
        SettingsMultiItems item = items.get(position);
        if (item instanceof SettingsMultiItems.TitleItem) {
            return TYPE_TITLE;
        } else if (item instanceof SettingsMultiItems.ColorItem) {
            return TYPE_COLOR;
        } else if (item instanceof SettingsMultiItems.ScanSettingsItem) {
            return TYPE_SCAN_SETTINGS;
        } else if (item instanceof SettingsMultiItems.ScanFilterItem) {
            return TYPE_SCAN_FILTER;
        } else if (item instanceof SettingsMultiItems.EditItem) {
            return TYPE_EDIT;
        } else if (item instanceof SettingsMultiItems.RestItem) {
            return TYPE_REST;
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_TITLE:
                return new TitleViewHolder(inflater.inflate(R.layout.title_view_settings_item, parent, false));
            case TYPE_COLOR:
                return new ColorSettingsViewHolder(inflater.inflate(R.layout.color_settings_item, parent, false));
            case TYPE_SCAN_SETTINGS:
                return new ScanSettingsViewHolder(inflater.inflate(R.layout.edit_view_settings_item, parent, false));
            case TYPE_SCAN_FILTER:
                return new ScanFilterViewHolder(inflater.inflate(R.layout.scan_filter_settings, parent, false));
            case TYPE_EDIT:
                return new EditSettingsViewHolder(inflater.inflate(R.layout.edit_view_settings_item, parent, false));
//            case TYPE_REST:
//                return new RestSettingsViewHolder(inflater.inflate(R.layout.rest_view_settings_item, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SettingsMultiItems item = items.get(position);
        if (item instanceof SettingsMultiItems.TitleItem) {
            ((TitleViewHolder) viewHolder).bind((SettingsMultiItems.TitleItem) item);
        } else if (item instanceof SettingsMultiItems.ColorItem) {
            ((ColorSettingsViewHolder) viewHolder).bind((SettingsMultiItems.ColorItem) item);
        } else if (item instanceof SettingsMultiItems.ScanSettingsItem) {
            ((ScanSettingsViewHolder) viewHolder).bind((SettingsMultiItems.ScanSettingsItem) item);
        } else if (item instanceof SettingsMultiItems.ScanFilterItem) {
            ((ScanFilterViewHolder) viewHolder).bind((SettingsMultiItems.ScanFilterItem) item);
        } else if (item instanceof SettingsMultiItems.EditItem) {
            ((EditSettingsViewHolder) viewHolder).bind((SettingsMultiItems.EditItem) item);
        }
//        else if (item instanceof SettingsMultiItems.RestItem) {
//            ((RestSettingsViewHolder) viewHolder).bind((SettingsMultiItems.RestItem) item);
//        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void scanFilterDialog(Context context, TextInputEditText inputText, SettingsMultiItems.ScanFilterItem item) {
        String[] options = new String[]{
                context.getString(R.string.auto_option),
                context.getString(R.string.gray_option),
                context.getString(R.string.black_and_white_option),
                context.getString(R.string.original_option),
                context.getString(R.string.text_option),
                context.getString(R.string.auto_2_option),
                context.getString(R.string.illustration_option)
        };
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(R.string.title_scan_dialog).setItems(options, (dialog, which) -> {
            String selectedOption = options[which];
            inputText.setText(selectedOption);
            settingsSharedPreferences.saveScanFilterItem(item.filterKey, which);
        }).setNegativeButton(context.getString(R.string.cancel_scan_dialog), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void colorPickerDialog(
            Context context,
            View view,
            SettingsMultiItems.ColorItem colorItem,
            SettingsSharedPreferences.ColorType colorType,
            String defaultColor
    ) {
        ColorPickerDialog.Builder colorBuilder = new ColorPickerDialog.Builder(context);
        colorBuilder.setTitle("Pick Theme")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(defaultColor)
                .setColorListener((color, colorHex) -> {
                    circleView(view, colorHex);
                    switch (colorType) {
                        case Light:
                            settingsSharedPreferences.saveColorItemLight(colorItem.colorKey.toString(), colorHex);
                            break;
                        case Dark:
                            settingsSharedPreferences.saveColorItemDark(colorItem.colorKey.toString(), colorHex);
                            break;
                    }
                    this.notifyDataSetChanged();
                }).show();
    }

    private void circleView(View view, String colorHex) {
        int color = Color.parseColor(colorHex);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(2, Color.GRAY);
        gradientDrawable.setColor(color);
        view.setBackground(gradientDrawable);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(List<SettingsMultiItems> newData) {
        items.clear(); // Clear existing data
        items.addAll(newData); // Add new data
        notifyDataSetChanged(); // Notify adapter about the changes
    }
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_COLOR = 1;
    private static final int TYPE_SCAN_SETTINGS = 2;
    private static final int TYPE_SCAN_FILTER = 3;
    private static final int TYPE_EDIT = 4;
    private static final int TYPE_REST = 5;
}
