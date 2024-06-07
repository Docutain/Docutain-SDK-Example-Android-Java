package de.docutain.sdk.docutain_sdk_example_android_java;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickedListener {
        void onItemClicked(ListItem item);
    }

    public enum ItemType {
        NONE,
        DOCUMENT_SCAN,
        DATA_EXTRACTION,
        TEXT_RECOGNITION,
        PDF_GENERATING,
        SETTINGS
    }

    private final ListItem[] items = {
            new ListItem(R.string.title_document_scan, R.string.subtitle_document_scan, R.drawable.document_scanner, ItemType.DOCUMENT_SCAN),
            new ListItem(R.string.title_data_extraction, R.string.subtitle_data_extraction, R.drawable.data_extraction, ItemType.DATA_EXTRACTION),
            new ListItem(R.string.title_text_recognition, R.string.subtitle_text_recognition, R.drawable.ocr, ItemType.TEXT_RECOGNITION),
            new ListItem(R.string.title_PDF_generating, R.string.subtitle_PDF_generating, R.drawable.pdf, ItemType.PDF_GENERATING),
            new ListItem(R.string.title_settings, R.string.subtitle_settings, R.drawable.settings, ItemType.SETTINGS)
    };

    private final OnItemClickedListener onItemClickedListener;

    public ListAdapter(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ListItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListItemViewHolder itemViewHolder = (ListItemViewHolder) holder;
        itemViewHolder.title.setText(items[position].getTitle());
        itemViewHolder.secondary.setText(items[position].getSubtitle());
        itemViewHolder.icon.setImageResource(items[position].getIcon());
        holder.itemView.setOnClickListener(v -> onItemClickedListener.onItemClicked(items[position]));
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}