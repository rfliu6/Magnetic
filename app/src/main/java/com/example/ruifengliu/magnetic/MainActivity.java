package com.example.ruifengliu.magnetic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private MFCollection mfCollection;

    private MFMatch mfMatch;
    private static final String TAG = "magnet";
    private Button btnStart, btnSave, btnFinish, btnLoad, btnMap, btnStop, btnDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnFinish = (Button) findViewById(R.id.btnFinish);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnStart.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnLoad.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnFinish.setEnabled(false);
        btnSave.setEnabled(false);
        btnLoad.setEnabled(true);
        btnMap.setEnabled(true);
        btnStop.setEnabled(false);
        btnDelete.setEnabled(true);
        btnStop.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        mfCollection = new MFCollection(this, this);
        mfMatch = new MFMatch(this, this);
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
                btnFinish.setEnabled(true);
                btnSave.setEnabled(false);
                mfCollection.start();
                break;

            case R.id.btnFinish:
                btnStart.setEnabled(true);
                btnFinish.setEnabled(false);
                btnSave.setEnabled(true);
                mfCollection.stop();
                break;

            case R.id.btnSave:
                btnStart.setEnabled(true);
                btnFinish.setEnabled(false);
                btnSave.setEnabled(false);
                btnLoad.setEnabled(true);
                mfCollection.save();
                break;

            case R.id.btnLoad:
                mfMatch.load();
                btnLoad.setEnabled(false);
                btnMap.setEnabled(true);
                break;

            case R.id.btnMap:
                mfMatch.map();
                btnMap.setEnabled(false);
                btnStop.setEnabled(true);
                break;

            case R.id.btnStop:
                mfMatch.stop();
                btnMap.setEnabled(true);
                btnStop.setEnabled(false);
                break;

            case R.id.btnDelete:
                mfMatch.delete();
                btnLoad.setEnabled(true);
                break;
        }
    }

}
