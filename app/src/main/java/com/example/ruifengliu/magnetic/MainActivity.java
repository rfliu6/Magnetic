package com.example.ruifengliu.magnetic;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import static android.R.attr.targetSdkVersion;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private MFCollection mfCollection;

    private MFMatch mfMatch;
    private static final String TAG = "magnet";
    private Button btnStart, btnSave, btnFinish, btnLoad, btnMap, btnStop, btnDelete;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private int targetSdkVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            final PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                if(PermissionChecker.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        }

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

       /* if(Build.Version.SDK_INT >= Build.VERSION_CODES.MARSHMALLOW) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }*/

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
