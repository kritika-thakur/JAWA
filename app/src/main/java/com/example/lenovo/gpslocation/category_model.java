package com.example.lenovo.gpslocation;

/**
 * Created by ideafoundation on 06/07/17.
 */

public class category_model {
    public String category_name,cat_type;
    public int  cat_image;
    boolean checked;

    public category_model(String category_name, String cat_type, boolean checked ){
        this.category_name = category_name;
       // this.cat_image = cat_image;
        this.cat_type = cat_type;
        this.checked = checked;

    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public boolean getCat_checked() {
        return checked;
    }

    public void setCat_checked(boolean checked) {
        this.checked= checked;
    }

    public int getCat_image() {
        return cat_image;
    }

    public void setCat_image(int cat_image) {
        this.cat_image = cat_image;
    }
}