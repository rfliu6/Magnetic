package com.example.ruifengliu.magnetic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ruifengliu on 19/9/2016.
 */
public class MFCollection implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor msensor, gsensor;
    private ArrayList<MagElement> deviceMagList, earthMagList;
    private boolean started = false;
    private String fileName;
    private Context context;
    private Activity activity;
    private TextView sensorTextView;
    private float[] gravityValues = null;
    private DataBaseHelper dbHelper;

    public MFCollection(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorTextView = (TextView) activity.findViewById(R.id.magxyz);
    }



    public void start(){
        Log.d("magneticlog","start");
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_NORMAL);
        deviceMagList = new ArrayList<>();
        earthMagList = new ArrayList<>();
        started = true;
    }

    public void stop(){
        Log.d("magneticlog","stop");
        sensorManager.unregisterListener(this);
        started = false;
    }

    public void save() {
        Log.d("magneticlog","save");
      /*  LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                fileName = userInput.getText().toString();
                                TextFile file = new TextFile(context,fileName, deviceMagList,earthMagList);

                                file.write();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
        dbHelper = new DataBaseHelper(context, null);
        Path path = new Path(0, earthMagList);
        dbHelper.onInsert(path);
        dbHelper.close();
        Toast.makeText(context, "Path Saved", Toast.LENGTH_SHORT).show();
    }
        @Override
    public void onSensorChanged(SensorEvent event) {
            if (started){
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        if ((gravityValues != null)) {
                            DeviceToEarth element = new DeviceToEarth(event, sensorTextView, gravityValues);
                            element.compute();
                            MagElement deviceMagElement = element.getDeviceMagElement();
                            MagElement earthMagMagElement = element.getEarthMagElement();
                            deviceMagList.add(deviceMagElement);
                            earthMagList.add(earthMagMagElement);
                        }
                        break;

                    case Sensor.TYPE_GRAVITY:
                        if(gravityValues == null)
                            gravityValues = new float[4];
                        gravityValues[0] = event.values[0];
                        gravityValues[1] = event.values[1];
                        gravityValues[2] = event.values[2];
                        break;
                }
            }
        }

                @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

}
