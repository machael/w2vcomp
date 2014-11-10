package common;

import org.ejml.simple.SimpleMatrix;

/**
 * This class contains a set of utility method for simple maths
 * (maybe should be replaced with utility method for SimpleMatrix class
 * @author pham
 *
 */
public class MathUtils {
    // TODO: use some linear algebra method
    
    /**
     * Cosine of two vectors
     * @param v1: 1st vector 
     * @param v2: 2nd vector
     * @return cosine value
     */
    public static double cosine(double[] v1, double[] v2) {
        double length1 = length(v1);
        double length2 = length(v2);
        if (length1 == 0 || length2 == 0) return 0.0;
        else return dot(v1, v2) / (length1 * length2);
    }

    
    
    /**
     * Cosine of two vectors
     * @param v1: 1st vector 
     * @param v2: 2nd vector
     * @return cosine value
     */
    public static double cosine(SimpleMatrix v1, SimpleMatrix v2) {
        double length1 = v1.normF();
        double length2 = v2.normF();
        if (length1 == 0 || length2 == 0) return 0.0;
        else return ((v1.mult(v2)).scale(1/(length1 * length2))).get(0,0);
        
    }

    /**
     * Length of a vector
     * @param v: input vector
     * @return length
     */
    public static double length(double[] v) {
        double norm = dot(v, v);
        return Math.sqrt(norm);
    }
    
    /**
     * Dot product of two vectors
     * @param v1 first vector
     * @param v2 second vector
     * @return dot product
     */
    public static double dot(double[] v1, double[] v2) {
        double result = 0;
        for (int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }
        return result;
    }

    /**
     * sigmoid function
     * @param f input value
     * @return sigmoid(f)
     */
    public static double sigmoid(double f) {
        // TODO: understand why they turn the formula like this (e^x faster
        // than e^-x ? Rounding error?)
        return 1 - (double) (1.0 / (1.0 + Math.exp(f)));
    }
    
    public static double[] cosineDerivative(double[] x, double[] a) {
        double lengthX = length(x);
        double lengthA = length(a);
        double dotP = dot(x, a);
        double rToScaleA = 1 / (lengthX * lengthA);
        double rToScaleX = dotP / (lengthA * lengthX * lengthX * lengthX);
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = (a[i] * rToScaleA) - (x[i] * rToScaleX);
        }
        return result;
    }
    
    
    public static SimpleMatrix cosineDerivative(SimpleMatrix x, SimpleMatrix a) {
        double lengthX = x.normF();
        double lengthA = a.normF();
        double dotP = x.mult(a).get(0,0);
        double rToScaleA = 1 / (lengthX * lengthA);
        double rToScaleX = dotP / (lengthA * lengthX * lengthX * lengthX);
        SimpleMatrix result = (a.scale(rToScaleA)).minus(x.scale(rToScaleX).transpose());
        
        return result;
    }
}
