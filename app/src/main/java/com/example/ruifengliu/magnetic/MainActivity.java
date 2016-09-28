package com.example.ruifengliu.magnetic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private MagCollection magcollection;
    private DataSet database;
    private static final String TAG = "magnet";
    private Button btnStart, btnSave, btnStop, btnLoad, btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnMap = (Button) findViewById(R.id.btnMap);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnSave.setEnabled(false);
        btnLoad.setEnabled(true);
        btnMap.setEnabled(false);

        magcollection = new MagCollection(this, this);
        database = new DataSet(this);
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
                magcollection.start();
                break;

            case R.id.btnStop:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnSave.setEnabled(true);
                magcollection.stop();
                break;

            case R.id.btnSave:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnStop.setEnabled(false);
                btnSave.setEnabled(false);
                magcollection.save();
                break;

            case R.id.btnLoad:
                database.Load();
                break;

            case R.id.btnMap:

                break;
        }
    }

}
