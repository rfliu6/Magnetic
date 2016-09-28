package com.example.ruifengliu.magnetic;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by ruifengliu on 28/9/2016.
 */
public class DataSet {
    private DataBaseHelper dbHelper;
    private Context context;
    private ArrayList<Path> pathList;

    public DataSet(Context context){
        this.context = context;

    }

    public void Load(){
        dbHelper = new DataBaseHelper(context, null);
        pathList = dbHelper.onLoad();
        dbHelper.close();
    }

}
