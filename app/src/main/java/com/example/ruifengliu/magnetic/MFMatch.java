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

import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static android.R.attr.x;


/**
 * Created by ruifengliu on 28/9/2016.
 */
public class MFMatch implements SensorEventListener {

    private Context context;
    private Activity activity;
    private TextView textView;

    private SensorManager sensorManager;
    private Sensor msensor, gsensor, csensor;
    private boolean started = false;
    private float[] gravityValues = null;
    private MagElement earthMagElement;
    private int accuracy = 0;
    private int prediction = 0;
    private double prob_slow = -100;
    private double prob_fast = -100;

    private DataBaseHelper dbHelper;
    private ArrayList<Path> pathList = null;
    private MatchTask matchTask = null;

    private Motion motion;
    private ArrayList<Particle> particleList;
    private Random generator= new Random(System.currentTimeMillis());

    public MFMatch(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        csensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        textView = (TextView) activity.findViewById(R.id.magxyz);
    }

    public void load(){
        if(!started) {
            dbHelper = new DataBaseHelper(context, null);
            pathList = dbHelper.onLoad();

            String tmp = "path number " + pathList.size() + "\npath id";
            for(int i =0; i<pathList.size(); i++)
                tmp += " " + pathList.get(i).getId();

            tmp+= "\tpath size";
            for(int i =0; i<pathList.size(); i++)
                tmp += " " + pathList.get(i).getSize();

            tmp+= "\tpath length";
            for(int i =0; i<pathList.size(); i++)
                tmp += " " + pathList.get(i).getLength();

            textView.setText(tmp);
            dbHelper.close();
            Toast.makeText(context, "Load finish", Toast.LENGTH_SHORT).show();
        }
    }

    public void map(){
        particleList = new ArrayList<>();
        generateParticles(Util.getParticleNum(),pathList);
        motion = new Motion(0,0);
        sensorManager.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, csensor, SensorManager.SENSOR_DELAY_NORMAL);
        started = true;
    }

    public void generateParticles(int number, ArrayList<Path> pathList) {
        Random generator= new Random();
        for (int i = 0; i < number; i++) {
            int x = (int) (generator.nextDouble() * (pathList.get(0).getSize()-1));
            particleList.add(new Particle( 0, x, 0, generator.nextFloat() < 0.5 ? 0 : 180 , (1.0 / number)));
            //particleList.add(new Particle( 0, 0, 0, 0, (1.0 / number)));
        }
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
                    DeviceToEarth element = new DeviceToEarth(event, null, gravityValues, accuracy);
                    element.compute();
                    earthMagElement = element.getEarthMagElement();
                }
                break;

            case Sensor.TYPE_GRAVITY:
                if(gravityValues == null)
                    gravityValues = new float[4];
                gravityValues[0] = event.values[0];
                gravityValues[1] = event.values[1];
                gravityValues[2] = event.values[2];
                break;

            case Sensor.TYPE_STEP_COUNTER:
                if (motion.getPrevStep() < 1) {
                    motion.setPrevStep((int)event.values[0]);
                    break;
                }
                if(prediction == 0) {
                    prediction++;
                    motion.setStep((int) event.values[0] - motion.getPrevStep());
                    motion.setPrevStep((int) event.values[0]);
                   // textView.setText(Integer.toString(motion.getStep()) + " " +Integer.toString((int)event.values[0]));
                    if (motion.getStep() > 0) {
                        try {
                            monteCarlo(motion.getStep());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        matchTask = new MatchTask(activity, pathList, particleList);
                        matchTask.execute();
                    }
                    prediction--;
                }
                break;
        }
    }

    private void monteCarlo(int s) throws CloneNotSupportedException {
        double[] probs = new double[Util.getParticleNum()];
        double prob_avg = 0;
        for (int i = 0; i < particleList.size(); i++) {
            int direction = particleList.get(i).getOrientation() == 0 ? 1 : -1;
            //generator.nextGaussian()*0.175+1
            double distance = ((particleList.get(i).getX() + direction*(generator.nextGaussian()*0.175+1)*motion.getStep()* pathList.get(0).getSize()/pathList.get(0).getLength())+pathList.get(0).getSize())%pathList.get(0).getSize();
            particleList.get(i).setX(distance);
        }

        for(int i=0; i<particleList.size(); i++){
            if (!pathList.get(0).isPointIn(particleList.get(i).getX())) {
                generateParticle(i, Util.getParticleNum(), pathList);
            }
            probs[i] = Util.gaussian(Util.getMagtitude(earthMagElement), linear(0, particleList.get(i).getX()), 1);
            prob_avg +=  probs[i]/Util.getParticleNum();
        }

        if(prob_slow <0 && prob_fast <0){
            prob_slow = prob_fast = prob_avg;
        }else {
            prob_slow += 0.1 * (prob_avg - prob_slow);
            prob_fast += 0.8 * (prob_avg - prob_fast);
        }

        textView.setText(Double.toString((double)1-prob_fast/prob_slow));

        Util.normalize(probs);
        for (int i = 0; i < particleList.size(); i++) {
            particleList.get(i).setWeight((float) probs[i]);
        }
        generateNewParticles();
    }

    double linear(int pathid, double p){
        if((int)p == p){
            return Util.getMagtitude(pathList.get(pathid).getEarthMagList().get((int)p));
        }
        double left = Util.getMagtitude(pathList.get(pathid).getEarthMagList().get((int)p));
        double right = Util.getMagtitude(pathList.get(pathid).getEarthMagList().get((int)p+1));
        return left+(right-left)*(p-(int)p);
    }




    public void generateParticle(int i, int number, ArrayList<Path> pathList){
        int x = (int) (generator.nextDouble() * (pathList.get(0).getSize()-1));
        particleList.set(i,new Particle( 0, x, 0, generator.nextFloat() < 0.5 ? 0 : 180, (1.0 / number)));

    }

    public void generateNewParticles() throws CloneNotSupportedException {
        int N = particleList.size();
        ArrayList<Particle> oldList = new ArrayList<>(N);
        for (Particle p: particleList) {
            oldList.add((Particle)p.clone());
        }
        particleList.clear();
        double bestPro = Double.MIN_VALUE, B = 0;
        for (int i = 0; i < N; i++)
            if(oldList.get(i).getWeight() > bestPro)
                bestPro = oldList.get(i).getWeight();

        int index = (int) generator.nextFloat() * N;
        for (int i = 0; i < N; i++) {
            //(1-prob_fast/prob_slow)
            if(i<N*0.05) {
                double x = generator.nextDouble() * (pathList.get(0).getSize() - 1);
                particleList.add(new Particle(0, x, 0, generator.nextFloat() < 0.5 ? 0 : 180, 0));
            }else{
                B += generator.nextFloat() * 2f * bestPro;
                while (B > oldList.get(index).getWeight()) {
                    B -= oldList.get(index).getWeight();
                    index = circle(index + 1, N);
                }
                particleList.add((Particle) oldList.get(index).clone());
            }
        }
        oldList.clear();
    }

    private int circle(int num, int length) {
        while (num > length - 1) {
            num -= length;
        }
        while (num < 0) {
            num += length;
        }
        return num;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        this.accuracy = accuracy;
    }
}
