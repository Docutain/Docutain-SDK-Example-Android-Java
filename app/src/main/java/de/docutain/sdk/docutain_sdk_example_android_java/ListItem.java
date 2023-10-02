package de.docutain.sdk.docutain_sdk_example_android_java;

public class ListItem {
    private int title;
    private int subtitle;
    private int icon;
    private ListAdapter.ItemType type;

    public ListItem(int title, int subtitle, int icon, ListAdapter.ItemType type) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
        this.type = type;
    }

    public int getTitle() {
        return title;
    }

    public int getSubtitle() {
        return subtitle;
    }

    public int getIcon() {
        return icon;
    }

    public ListAdapter.ItemType getType() {
        return type;
    }
}
