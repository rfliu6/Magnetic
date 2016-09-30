package com.example.ruifengliu.magnetic;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ruifengliu on 28/9/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

   // private Context context;
   // SQLitemfmatch db;
    private static final String dbName = "/storage/emulated/0/data/magnetic.db";
    private static final String dbTable= "Path";
    private static final String keyRowId = "RowId";
    private static final String keyPathId = "PathId";
    private static final String keyMag = "Mag";
    private static final String keySize = "Size";
    private static int version=1;


    public DataBaseHelper(Context context, SQLiteDatabase.CursorFactory factory){
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + dbTable + " ("
                + keyRowId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + keyPathId + " INTEGER, " + keySize + " INTEGER, " + keyMag + " TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    public void onDelete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ dbTable);
    }

    public void onInsert(Path path){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(keyPathId,0);
        cv.put(keySize, path.getSize());
        cv.put(keyMag, path.getMagString());
        db.insert(dbTable, null, cv);
    }

    public ArrayList<Path> onLoad(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + dbTable;
        ArrayList<Path> pathList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
               // Path path = new Path(cursor.getString(1),cursor.getString(2),cursor.getString(3));
                Path path = new Path(cursor.getString(0),cursor.getString(2),cursor.getString(3));
                pathList.add(path);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pathList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
