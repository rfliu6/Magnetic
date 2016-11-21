package com.example.ruifengliu.magnetic;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

/**
 * Created by ruifengliu on 29/9/2016.
 */
public class MatchTask extends AsyncTask<Void, Void, Integer> {

    private ArrayList<Path> pathList;
    private ArrayList<Particle> particleList;
    private WeakReference<Activity> mWeakActivity;
    private ArrayList<BarEntry> BARENTRY;
    private ArrayList<String> BarEntryLabels;

    MatchTask(Activity activity, ArrayList<Path> pathList,  ArrayList<Particle> particleList){
        mWeakActivity = new WeakReference<>(activity);
        this.particleList = particleList;
        this.pathList = pathList;
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();
    }

    protected void onPreExecute() {
      /*  String filePath = null;
        try {
            filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/data/log.csv";
            File outputfile = new File(filePath);
            outputfile.createNewFile(); // if file already exists will do nothing
            FileWriter f = new FileWriter(filePath, true);
            f.write(System.currentTimeMillis()+",");
            f.flush();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    protected Integer doInBackground(Void... params) {

        return 0;
    }



    @Override
    protected void onPostExecute(Integer v) {
        Activity activity = mWeakActivity.get();
        if (activity != null) {
            BarChart chart ;
            BarDataSet Bardataset ;
            BarData BARDATA ;
            chart = (BarChart)activity.findViewById(R.id.chart1);
            chart.clear();

            AddValuesToBARENTRY();

            AddValuesToBarEntryLabels();

            Bardataset = new BarDataSet(BARENTRY, "");

            BARDATA = new BarData(BarEntryLabels, Bardataset);

            Bardataset.setColor(R.color.black);
            Bardataset.setDrawValues(false);
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawLabels(true);
            YAxis leftAxis = chart.getAxisLeft();
            YAxis rightAxis = chart.getAxisRight();
            leftAxis.setSpaceBottom(0);
            rightAxis.setSpaceBottom(0);
            leftAxis.setLabelCount(5, false);
            rightAxis.setLabelCount(5, false);
            chart.setDescription(Long.toString(System.currentTimeMillis()));
            chart.setData(BARDATA);
        }
        super.onPostExecute(v);
    }

    public void AddValuesToBARENTRY(){
        int[] histogram = new int[pathList.get(0).getSize()];
        for(int i = 0; i<pathList.get(0).getSize(); i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < particleList.size(); i++) {
            histogram[(int)particleList.get(i).getX()]++;
        }
        histogram[pathList.get(0).getSize()-1]++;

        for(int i = 0; i<pathList.get(0).getSize(); i++) {
            BARENTRY.add(new BarEntry(histogram[i], i));
        }
    }

    private void AddValuesToBarEntryLabels(){
        for(int i=0; i<pathList.get(0).getSize(); i++) {
            BarEntryLabels.add(Integer.toString(i));
        }

    }

}


