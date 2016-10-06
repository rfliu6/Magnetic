package com.example.ruifengliu.magnetic;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;


/**
 * Created by ruifengliu on 28/9/2016.
 */
public class MFMatch implements SensorEventListener {
    private DataBaseHelper dbHelper;
    private Context context;
    private Activity activity;
    private ArrayList<Path> pathList;
    private SensorManager sensorManager;
    private Sensor msensor, gsensor;
    private CircularFifoQueue<MagElement> earthMagQueue;
    private boolean started = false;
    private float[] gravityValues = null;
    private MatchTask matchTask = null;

    public MFMatch(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    public void load(){
        if(!started) {
            dbHelper = new DataBaseHelper(context, null);
            pathList = dbHelper.onLoad();
            TextView mapTextView = (TextView) activity.findViewById(R.id.magxyz);

            String tmp = "path number " + pathList.size() + "\npath id";
            for(int i =0; i<pathList.size(); i++){
                tmp += " " + pathList.get(i).getId();
            }
            tmp+= "\npath size";
            for(int i =0; i<pathList.size(); i++){
                tmp += " " + pathList.get(i).getSize();
            }
            mapTextView.setText(tmp);
           // mapTextView.setText(String.format(Locale.US, "%d\n", pathList.size()));
            dbHelper.close();
            Toast.makeText(context, "Load finish", Toast.LENGTH_SHORT).show();
        }
    }


    public void map(){
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_NORMAL);
        earthMagQueue = new CircularFifoQueue<>(30);
        started = true;
    }

    public void stop(){
        sensorManager.unregisterListener(this);
        started = false;
    }

    public void delete(){
        if(!started) {
            dbHelper = new DataBaseHelper(context, null);
            dbHelper.onDelete();
            dbHelper.close();
            Toast.makeText(context, "Delete finish", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                if ((gravityValues != null)) {
                    DeviceToEarth element = new DeviceToEarth(event, null, gravityValues);
                    element.compute();
                    MagElement earthMagMagElement = element.getEarthMagElement();
                    earthMagQueue.add(earthMagMagElement);
                }
                if (earthMagQueue.isAtFullCapacity()) {
                    if(matchTask == null || matchTask.getStatus() == AsyncTask.Status.FINISHED) {
                        matchTask = new MatchTask(activity, earthMagQueue, pathList);
                        matchTask.execute();
                    }
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
