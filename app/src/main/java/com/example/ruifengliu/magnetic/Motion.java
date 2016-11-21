package com.example.ruifengliu.magnetic;

/**
 * Created by Administrator on 8/11/2016.
 */

public class Motion {
    private float prevAngle;
    private int X;
    private int Y;
    private int step = 0;
    private int prevStep = 0;

    public Motion(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public void setStep(int step){
        this.step = step;
    }

    public int getStep(){
        return step;
    }

    public int getPrevStep() {
        return prevStep;
    }

    public void setPrevStep(int prevStep){
        this.prevStep = prevStep;
    }

    public float getPrevAngle() {
        return prevAngle;
    }

    public void setPrevAngle(float prevAngle) {
        this.prevAngle = prevAngle;
    }


}
