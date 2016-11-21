package com.example.ruifengliu.magnetic;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;

import java.util.Locale;


/**
 * device coordinate to earth coordinate
 *
 * event: get device
 */
public class DeviceToEarth {
    private TextView sensorTextView;
    private float[] gravityValues, deviceMag, earthMag;
    private MagElement deviceMagElement, earthMagElement;
    private int accuracy;

    public DeviceToEarth(SensorEvent event, TextView sensorTextView, float[] gravityValues, int accuracy){
        this.sensorTextView = sensorTextView;
        this.gravityValues = gravityValues;
        this.accuracy = accuracy;
        deviceMag = new float[4];
        earthMag = new float[16];
        deviceMag[0] = event.values[0];
        deviceMag[1] = event.values[1];
        deviceMag[2] = event.values[2];
        deviceMag[3] = 0;
    }

    public void compute(){
        // Change the device relative magnetic values to earth relative values
        // X axis -> East
        // Y axis -> North Pole
        // Z axis -> Sky
        float[] R = new float[16], I = new float[16];
        SensorManager.getRotationMatrix(R, I, gravityValues, deviceMag);
        float[] inv = new float[16];
        android.opengl.Matrix.invertM(inv, 0, R, 0);
        android.opengl.Matrix.multiplyMV(earthMag, 0, inv, 0, deviceMag, 0);
        long timestamp = System.currentTimeMillis();
        deviceMagElement = new MagElement(timestamp, deviceMag[0], deviceMag[1], deviceMag[2]);
        earthMagElement = new MagElement(timestamp, earthMag[0], earthMag[1], earthMag[2]);
        if(sensorTextView != null)
            show();
    }

    public void show(){
       // sensorTextView.setText(String.format(Locale.US,"Accuracy: %d\nDevice\nx: %f\ny: %f\nz: %f size: %f\n", accuracy, deviceMag[0], deviceMag[1], deviceMag[2], (float)(Math.pow(deviceMag[0],2) + Math.pow(deviceMag[1],2) +Math.pow(deviceMag[2],2))));
      //  sensorTextView.append(String.format(Locale.US,"Earth\nx: %f\n" +"y: %f\nz: %f", earthMag[0], earthMag[1], earthMag[2]));
    }

    public MagElement getDeviceMagElement(){
        return deviceMagElement;
    }

    public MagElement getEarthMagElement(){
        return earthMagElement;
    }
}
