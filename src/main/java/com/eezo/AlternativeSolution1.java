package main.java.com.eezo;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class AlternativeSolution1 {
    private Matrix matrix;
    private int lastEmptyCellIndexFound;

    public AlternativeSolution1(Matrix matrix) {
        this.matrix = matrix;
        lastEmptyCellIndexFound = -1;
    }

    public Matrix getMatrix() {
        return matrix;
    }
}
