package com.example.ruifengliu.magnetic;

/**
 * Created by Administrator on 8/11/2016.
 */

public class Util {
    private static Integer particleNum = 1000;

    public final static int getParticleNum() {
        return particleNum;
    }

    /**
     * Gaussian probabilty density function
     *

     */

    public final static double gaussian(double x, double mu, double sigma) {
        return (1 / (sigma * Math.sqrt(2.0 * Math.PI))) * Math.exp(-0.5 * Math.pow(((x - mu) / sigma), 2));
       // return (Math.exp(-0.5 * Math.pow(((x - mu) / sigma), 2)));
        //return 1/Math.sqrt(Math.pow(x,2)- Math.pow(mu,2));
    }

    /**
     * Normalizes the doubles in the array by their sum.(from Weka)
     *
     * @param doubles
     *            the array of double
     * @exception IllegalArgumentException
     *                if sum is Zero or NaN
     */
    public final static void normalize(double[] doubles) {

        double sum = 0;
        for (int i = 0; i < doubles.length; i++) {
            sum += doubles[i];
        }
        normalize(doubles, sum);
    }

    public final static double getMagtitude(MagElement m){
        return Math.sqrt(Math.pow(m.getX(),2) + Math.pow(m.getY(),2) + Math.pow(m.getZ(),2));
    }

    /**
     * Normalizes the doubles in the array using the given value.(from Weka)
     *
     * @param doubles
     *            the array of double
     * @param sum
     *            the value by which the doubles are to be normalized
     * @exception IllegalArgumentException
     *                if sum is zero or NaN
     */
    public final static void normalize(double[] doubles, double sum) {

        if (Double.isNaN(sum)) {
            throw new IllegalArgumentException("Can't normalize array. Sum is NaN.");
        }
        if (sum != 0) {
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] /= sum;
            }
        }

    }

}
