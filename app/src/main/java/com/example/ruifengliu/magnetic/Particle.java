package com.example.ruifengliu.magnetic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 8/11/2016.
 */

public class Particle implements Cloneable{
    private double orientation, x, y;
    private double weight;
    private int pathId;

    public Particle(int pathId, double x, double y, double orientation, double weight) {
        this.pathId = pathId;
        this.orientation = orientation;
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public int getPathId(){
        return pathId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getOrientation() {
        return orientation;
    }

    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }



}
