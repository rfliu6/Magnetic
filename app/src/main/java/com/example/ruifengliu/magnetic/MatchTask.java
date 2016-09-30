package com.example.ruifengliu.magnetic;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ruifengliu on 29/9/2016.
 */
public class MatchTask extends AsyncTask<Void, Void, MatchTask.Point> {
    CircularFifoQueue<MagElement> earthMagQueue;
    ArrayList<Path> pathList;
    WeakReference<Activity> mWeakActivity;

   MatchTask(Activity activity, CircularFifoQueue<MagElement> earthMagQueue, ArrayList<Path> pathList){
       mWeakActivity = new WeakReference<>(activity);
       this.earthMagQueue = earthMagQueue;
       this.pathList = pathList;
    }


    protected MatchTask.Point doInBackground(Void... params) {
        float ratio = -1, distance = Float.MAX_VALUE;
        int pathId = -1;

        for (int i=0; i<pathList.size(); i++){
            if(pathList.get(i).getSize() > earthMagQueue.size()){
                for(int j=0; j<pathList.get(i).getSize()-earthMagQueue.size(); j++){
                    float sum = 0;
                    for(int k=0; k<earthMagQueue.size(); k++){
                    sum +=    Math.pow(pathList.get(i).getEarthMagList().get(j+k).getX()-earthMagQueue.get(k).getX(), 2)
                            + Math.pow(pathList.get(i).getEarthMagList().get(j+k).getY()-earthMagQueue.get(k).getY(), 2)
                            + Math.pow(pathList.get(i).getEarthMagList().get(j+k).getZ()-earthMagQueue.get(k).getZ(), 2);
                    }
                    if(sum < distance){
                        pathId = i;
                        ratio = (float)j/pathList.get(i).getSize();
                        distance = sum;
                    }
                }
            }
        }

        return new Point(pathId, ratio);
    }

    protected void onPostExecute(MatchTask.Point p) {
        Activity activity = mWeakActivity.get();
        if (activity != null) {
            TextView mapTextView = (TextView) activity.findViewById(R.id.magxyz);
            mapTextView.setText(String.format(Locale.US,"%d, %f\n", p.pathId, p.ratio));
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


