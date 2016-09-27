package com.example.ruifengliu.magnetic;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Magnetometer magnetometer;
    private static final String TAG = "magnet";
    private Button btnStart, btnSave, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnSave.setEnabled(false);

        magnetometer = new Magnetometer(this, this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG,"onStart");
    }

    protected void onPause(){
        super.onPause();
        Log.d(TAG,"onPause");
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnStart:
                Log.d(TAG,"btnStart");
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                btnSave.setEnabled(false);
                magnetometer.start();
                break;

            case R.id.btnStop:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnSave.setEnabled(true);
                magnetometer.stop();
                break;

            case R.id.btnSave:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnSave.setEnabled(false);
                magnetometer.save();
                break;

            case R.id.btnLoad:

                break;

            case R.id.btnMap:

                break;
        }
    }

}
