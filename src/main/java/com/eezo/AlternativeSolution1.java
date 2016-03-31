package main.java.com.eezo;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class AlternativeSolution1 {
    private Matrix matrix;
    private int lastEmptyCellIndexFound;
    private int sigma;
    private int Z;

    public AlternativeSolution1(Matrix matrix) {
        this.matrix = matrix;
        lastEmptyCellIndexFound = -1;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    /**
     * Returns a new alternative solution.
     * @return new AS or null if there are no more new AS
     */
    public AlternativeSolution1 getNextSolution(){
        lastEmptyCellIndexFound = matrix.getEmptyCellPosition(lastEmptyCellIndexFound);
        if (lastEmptyCellIndexFound == -1){
            return null;
        }
        return new AlternativeSolution1(matrix.assignmentSteppingStone(lastEmptyCellIndexFound));
    }

    public int calculateSigma(){
        sigma = 0;
        for (int i = 0; i < TransData.staticInstance.getMatrixRowsNumber(); i++) {
            for (int j = 0; j < TransData.staticInstance.getMatrixColsNumber(); j++) {
                sigma += TransData.staticInstance.getMatrixOfCosts()[i][j] * matrix.getCellByCoords(i, j).getStatus();
            }
        }
        return sigma;
    }

    public int calculateZ(){
        Z = 0;
        for (int i = 0; i < TransData.staticInstance.getMatrixRowsNumber(); i++) {
            for (int j = 0; j < TransData.staticInstance.getMatrixColsNumber(); j++) {
                Z += TransData.staticInstance.getMatrixOfCosts()[i][j]*matrix.getCellByCoords(i,j).getValue();
            }
        }
        return Z;
    }

    public int getSigma() {
        return sigma;
    }
}
