package common;

/**
 * This class contains a set of utility method for simple maths
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
    public static double cosine(float[] v1, float[] v2) {
        double length1 = length(v1);
        double length2 = length(v2);
        if (length1 == 0 || length2 == 0) return 0.0;
        else return dot(v1, v2) / (length1 * length2);
    }

    /**
     * Length of a vector
     * @param v: input vector
     * @return length
     */
    public static double length(float[] v) {
        double norm = dot(v, v);
        return Math.sqrt(norm);
    }
    
    /**
     * Dot product of two vectors
     * @param v1 first vector
     * @param v2 second vector
     * @return dot product
     */
    public static double dot(float[] v1, float[] v2) {
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
    public static float sigmoid(float f) {
        // TODO: understand why they turn the formula like this (e^x faster
        // than e^-x ?)
        return 1 - (float) (1.0 / (1.0 + Math.exp(f)));
    }
}
