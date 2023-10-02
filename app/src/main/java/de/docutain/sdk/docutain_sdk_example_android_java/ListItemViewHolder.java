package de.docutain.sdk.docutain_sdk_example_android_java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView title;
    public TextView secondary;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.list_item_icon);
        title = itemView.findViewById(R.id.list_item_text);
        secondary = itemView.findViewById(R.id.list_item_secondary_text);
    }
    public static ListItemViewHolder create(ViewGroup parent) {
        return new ListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false)
        );
    }
}