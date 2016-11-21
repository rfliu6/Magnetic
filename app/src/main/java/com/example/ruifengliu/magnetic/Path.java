package com.example.ruifengliu.magnetic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ruifengliu on 28/9/2016.
 */
public class Path {
    private int id;
    private ArrayList<MagElement> earthMagList;
    private String magString;
    private int magSize;
    private int stepLength;

    public Path(int id, ArrayList<MagElement> earthMagList, int length){
        this.id = id;
        this.earthMagList = earthMagList;
        this.stepLength = length;
        magSize = earthMagList.size();
        Gson gson = new Gson();
        magString = gson.toJson(earthMagList);
    }

    public Path(String id, String size, String length, String earthMagList ){
        this.id = Integer.parseInt(id);
        this.magSize =  Integer.parseInt(size);
        this.stepLength = Integer.parseInt(length);
        Type type = new TypeToken<ArrayList<MagElement>>() {}.getType();
        Gson gson = new Gson();
        this.earthMagList = gson.fromJson(earthMagList, type);
    }



    public boolean isPointIn(double point){
        if(point>=0 && point <=magSize-1)
            return true;
        else
            return false;
    }

    public String getMagString(){
        return magString;
    }

    public int getSize(){
        return magSize;
    }

    public int getId(){
        return id;
    }

    public  ArrayList<MagElement> getEarthMagList(){
        return earthMagList;
    }

    public int getLength(){return stepLength;}
}
