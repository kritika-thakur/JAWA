package com.example.lenovo.gpslocation;

/**
 * Created by Administrator on 6/26/2017.
 */

public class Fav {
    private String title, genre, year;
    int id;
    public Fav() {
    }

    public Fav(int id,String title, String genre, String year) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
