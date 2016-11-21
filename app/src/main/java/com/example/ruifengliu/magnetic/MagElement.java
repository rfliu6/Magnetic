package com.example.ruifengliu.magnetic;

/**
 * Created by ruifengliu on 20/9/2016.
 */
public class MagElement {
    private double x;
    private double y;
    private double z;
    private long timestamp;
    private double probability;

    public MagElement(long timestamp, double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.timestamp = timestamp;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getX(){
        return this.x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getY(){
        return this.y;
    }

    public void setZ(double z){
        this.z = z;
    }

    public double getZ(){
        return this.z;
    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    public long getTimestamp(){
        return this.timestamp;
    }


    public void setProbability(double probability){
        this.probability = probability;
    }

    public double getProbability(){
        return this.probability;
    }

    public String toString(){
        return timestamp+","+x+","+y+","+z;
    }
}
