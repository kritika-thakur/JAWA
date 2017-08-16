package com.example.lenovo.gpslocation;
/**
 * Created by Lenovo on 6/4/2017.
 */
public class ListItem {
    //    private int Id;
    private int id;
    private String category;
    private String place_name;
    private String place_add;
    private String place_icon;
    private String rating;
    private String phoneNo;
    private int marker_color;
    private int favourites;
    private double latitude;
    private double longitude;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getPlace_name() {
        return place_name;
    }
    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
    public String getPlace_add() {
        return place_add;
    }
    public void setPlace_add(String place_add) {
        this.place_add = place_add;
    }
    public String getPlace_icon() {
        return place_icon;
    }
    public void setPlace_icon(String place_icon) {
        this.place_icon = place_icon;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public int getFavourites() {
        return favourites;
    }
    public void setFavourites(int favourites) {
        this.favourites = favourites;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public int getMarkerColor() {
        return marker_color;
    }
    public void setMarkerColor(int marker_color) {
        this.marker_color = marker_color;
    }
}