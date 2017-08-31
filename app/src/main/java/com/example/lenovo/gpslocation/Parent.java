package com.example.lenovo.gpslocation;

/**
 * Created by Lenovo on 6/16/2017.
 */

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parent {
    private String mTitle;
    private ArrayList<String> mArrayChildren;
    private ArrayList<Float> mArrayRate;
    private ArrayList<String> mArrayName;
    private ArrayList<String> mArrayImage;
    private int cat_color, ID;


    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public ArrayList<String> getArrayChildren() {
        return mArrayChildren;
    }
    public void setArrayChildren(ArrayList<String> arrayChildren) {
        mArrayChildren = arrayChildren;
    }
    public int getMarker_Color() {
        return cat_color;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setMarker_Color(int cat_color) {
        this.cat_color = cat_color;
    }
    public ArrayList<Float> getArrayRate(){
        return mArrayRate;
    }
    public void setArrayRate (ArrayList<Float> arrayRate){
        mArrayRate = arrayRate;
    }
    public ArrayList<String> getArrayName(){
        return mArrayName;
    }
    public void setArrayName (ArrayList<String> arrayName){
        mArrayName = arrayName;
    }
    public ArrayList<String> getArrayImage(){
        return mArrayImage;
    }
    public void setArrayImage (ArrayList<String> arrayImage){
        mArrayImage = arrayImage;
    }
}
