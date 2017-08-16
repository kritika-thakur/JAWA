package com.example.lenovo.gpslocation;

/**
 * Created by Lenovo on 6/26/2017.
 */

public class RecyclerViewListItems  {

    private String android_version_name;
    private int cat_color;
    int head, ID;

    public String getAndroid_version_name() {
        return android_version_name;
    }

    public void setAndroid_version_name(String android_version_name) {
        this.android_version_name = android_version_name;
    }

    /*public int getAndroid_image_url() {
        return android_image_url;
    }

    public void setAndroid_image_url(int android_image_url) {
        this.android_image_url = android_image_url;
    }*/
    public int getMarker_Color() {
        return cat_color;
    }

    public void setMarker_Color(int cat_color) {
        this.cat_color = cat_color;
    }
    public int getHead() {
        return head;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
