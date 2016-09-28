package com.example.ruifengliu.magnetic;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ruifengliu on 27/9/2016.
 */
public class TextFile {
    private ArrayList<MagElement> deviceMagList, earthMagList;
    private String fileName;
    private Context context;

    public TextFile(Context context, String fileName, ArrayList<MagElement> deviceMagList, ArrayList<MagElement> earthMagList){
        this.fileName = fileName;
        this.deviceMagList = deviceMagList;
        this.earthMagList = earthMagList;
        this.context = context;
    }

    public void write(){
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                File folder = new File(Environment.getExternalStorageDirectory(), "data");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                //String filePath0 = Environment.getExternalStorageDirectory().getCanonicalPath() + "/data/" + fileName + "_device.csv";
                String filePath1 = Environment.getExternalStorageDirectory().getCanonicalPath() + "/data/" + fileName + "_earth.csv";
                /*FileOutputStream output0 = new FileOutputStream(filePath0);
                Toast.makeText(context, filePath0, Toast.LENGTH_SHORT).show();
                for(MagElement tmp: deviceMagList) {
                    output0.write(tmp.toString().getBytes());
                    output0.write("\n\r".getBytes());
                }
                output0.close();*/

                FileOutputStream output1 = new FileOutputStream(filePath1);
                for(MagElement tmp: earthMagList) {
                    output1.write(tmp.toString().getBytes());
                    output1.write("\n\r".getBytes());
                }
                //output0.close();
                output1.close();
            }  //else Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
