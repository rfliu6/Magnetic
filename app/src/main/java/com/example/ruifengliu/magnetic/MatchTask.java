package com.example.ruifengliu.magnetic;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ruifengliu on 29/9/2016.
 */
public class MatchTask extends AsyncTask<Void, Void, MatchTask.Point> {
    private CircularFifoQueue<MagElement> earthMagQueue;
    private ArrayList<Path> pathList;
    private WeakReference<Activity> mWeakActivity;

   MatchTask(Activity activity, CircularFifoQueue<MagElement> earthMagQueue, ArrayList<Path> pathList){
       mWeakActivity = new WeakReference<>(activity);
       this.earthMagQueue = earthMagQueue;
       this.pathList = pathList;
    }

    protected MatchTask.Point doInBackground(Void... params) {
        double ratio = -1, minDistance = Float.MAX_VALUE;
        int pathId = -1;

        for (int i=0; i<pathList.size(); i++){
            if(pathList.get(i).getSize() > earthMagQueue.size()){
                /*for(int j=0; j<pathList.get(i).getSize()-earthMagQueue.size(); j++){
                    float sum = 0;
                    for(int k=0; k<earthMagQueue.size(); k++){
                    sum +=    Math.pow(pathList.get(i).getEarthMagList().get(j+k).getX()-earthMagQueue.get(k).getX(), 2)
                            + Math.pow(pathList.get(i).getEarthMagList().get(j+k).getY()-earthMagQueue.get(k).getY(), 2)
                            + Math.pow(pathList.get(i).getEarthMagList().get(j+k).getZ()-earthMagQueue.get(k).getZ(), 2);
                    }
                    if(sum < minDistance){
                        pathId = pathList.get(i).getId();
                       // pathId = i;
                        ratio = (float)(j+earthMagQueue.size())/pathList.get(i).getSize();
                        minDistance = sum;
                    }
                }*/

                double[][] D = new double[earthMagQueue.size()][pathList.get(i).getSize()];
                for(int j=0; j<earthMagQueue.size(); j++){
                    if(j == 0)
                        D[j][0] = getDistance(i,j,0);
                    else
                        D[j][0] = getDistance(i,j,0) + D[j-1][0];
                }
                for(int j=1; j<pathList.get(i).getSize(); j++){
                    D[0][j] = getDistance(i,0,j);
                }
                for(int j=1; j<earthMagQueue.size(); j++){
                    for(int k=1; k<pathList.get(i).getSize(); k++){
                        D[j][k] = Math.min(Math.min(D[j-1][k-1],D[j-1][k]),D[j][k-1])+getDistance(i,j,k);
                    }
                }
                for(int j=0; j<pathList.get(i).getSize(); j++){
                    if(D[earthMagQueue.size()-1][j] < minDistance){
                        minDistance = D[earthMagQueue.size()-1][j];
                        ratio = (double)(j)/pathList.get(i).getSize();
                        pathId = pathList.get(i).getId();
                    }
                }
            }
        }
        return new Point(pathId, (float)ratio);
    }


    public double getDistance(int i, int j,int k){
        return Math.pow(earthMagQueue.get(j).getX()-pathList.get(i).getEarthMagList().get(k).getX(), 2)
                +Math.pow(earthMagQueue.get(j).getY()-pathList.get(i).getEarthMagList().get(k).getY(), 2)
                +Math.pow(earthMagQueue.get(j).getZ()-pathList.get(i).getEarthMagList().get(k).getZ(), 2);
    }


    protected void onPostExecute(MatchTask.Point p) {
        Activity activity = mWeakActivity.get();
        if (activity != null) {
            TextView mapTextView = (TextView) activity.findViewById(R.id.magxyz);
            mapTextView.setText(String.format(Locale.US,"pathId: %d, ratio: %f, time: %d\n", p.pathId, p.ratio, System.currentTimeMillis()));
        }

        super.onPostExecute(p);
    }

    public class Point
    {
        public int pathId;
        public float ratio;

        Point(int pathId, float ratio){
            this.pathId = pathId;
            this.ratio = ratio;
        }
    }
}


