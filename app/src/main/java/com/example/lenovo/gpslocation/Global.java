package com.example.lenovo.gpslocation;

/**
 * Created by ideafoundation on 21/07/17.
 */

public class Global {
    private static Global instance;

    // Global variable
    private float data;

    // Restrict the constructor from being instantiated
    private Global(){}

    public void setData(float d){
        this.data=d;
    }
    public float getData(){
        return this.data;
    }

    public static synchronized Global getInstance(){
        if(instance==null){
            instance=new Global();
        }
        return instance;
    }
}