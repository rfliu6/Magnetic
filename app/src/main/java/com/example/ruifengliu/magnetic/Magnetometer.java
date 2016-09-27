package com.example.ruifengliu.magnetic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ruifengliu on 19/9/2016.
 */
public class Magnetometer implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor msensor, gsensor, asensor;
    private ArrayList<MagData> deviceMagList, earthMagList;
    private boolean started = false;
    private String fileName;
    private Context context;
    private Activity activity;
    private TextView sensorTextView;
    private float[] gravityValues = null;

    public Magnetometer(Context context, Activity activity) {
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
        sensorTextView.setText("x: \ny: \nz: ");
        started = false;
    }

    public void save() {
        Log.d("magneticlog","save");

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                fileName = userInput.getText().toString();
                                writeToFile();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

    }


    public void writeToFile(){
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                File folder = new File(Environment.getExternalStorageDirectory(), "data");
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String filePath0 = Environment.getExternalStorageDirectory().getCanonicalPath() + "/data/" + fileName + "_device.csv";
                String filePath1 = Environment.getExternalStorageDirectory().getCanonicalPath() + "/data/" + fileName + "_earth.csv";
                FileOutputStream output0 = new FileOutputStream(filePath0);
                for(MagData tmp: deviceMagList) {
                    output0.write(tmp.toString().getBytes());
                    output0.write("\n\r".getBytes());
                }
                output0.close();
                Toast.makeText(context, filePath0, Toast.LENGTH_SHORT).show();

                FileOutputStream output1 = new FileOutputStream(filePath1);
                for(MagData tmp: earthMagList) {
                    output1.write(tmp.toString().getBytes());
                    output1.write("\n\r".getBytes());
                }
                output0.close();
                Toast.makeText(context, filePath1, Toast.LENGTH_SHORT).show();


            } //else Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        @Override
    public void onSensorChanged(SensorEvent event) {
            if (started){
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        if ((gravityValues != null)) {
                            float[] deviceMag = new float[4];
                            deviceMag[0] = event.values[0];
                            deviceMag[1] = event.values[1];
                            deviceMag[2] = event.values[2];
                            deviceMag[3] = 0;

                            // Change the device relative magnetic values to earth relative values
                            // X axis -> East
                            // Y axis -> North Pole
                            // Z axis -> Sky

                            float[] R = new float[16], I = new float[16], earthMag = new float[16];

                            SensorManager.getRotationMatrix(R, I, gravityValues, deviceMag);

                            float[] inv = new float[16];

                            android.opengl.Matrix.invertM(inv, 0, R, 0);
                            android.opengl.Matrix.multiplyMV(earthMag, 0, inv, 0, deviceMag, 0);

                            long timestamp = System.currentTimeMillis();
                            MagData deviceMagData = new MagData(timestamp, deviceMag[0], deviceMag[1], deviceMag[2]);
                            MagData earthMagData = new MagData(timestamp, earthMag[0], earthMag[1], earthMag[2]);

                            sensorTextView.setText(String.format(Locale.US,"Device\nx: %f\ny: %f\nz: %f\nEarth\nx: %f\n" +
                                    "y: %f\nz: %f", deviceMag[0], deviceMag[1], deviceMag[2], earthMag[0], earthMag[1], earthMag[2]));
                            deviceMagList.add(deviceMagData);
                            earthMagList.add(earthMagData);
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
