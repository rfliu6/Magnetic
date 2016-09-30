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
    private int size;

    public Path(int id, ArrayList<MagElement> earthMagList){
        this.id = id;
        this.earthMagList = earthMagList;
        size = earthMagList.size();
        Gson gson = new Gson();
        magString = gson.toJson(earthMagList);
    }

    public Path(String id, String size, String earthMagList){
        this.id = Integer.parseInt(id);
        this.size =  Integer.parseInt(size);
        Type type = new TypeToken<ArrayList<MagElement>>() {}.getType();
        Gson gson = new Gson();
        this.earthMagList = gson.fromJson(earthMagList, type);
    }

    public String getMagString(){
        return magString;
    }

    public int getSize(){
        return size;
    }

    public int getId(){
        return id;
    }

    public  ArrayList<MagElement> getEarthMagList(){
        return earthMagList;
    }
}
