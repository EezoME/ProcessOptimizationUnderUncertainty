package main.java.com.eezo;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class AlternativeSolution2 implements Cloneable {
    private Matrix matrix;
    private int lastEmptyCellIndexFound;
    private TriangularNumber Z;

    public AlternativeSolution2(){
        lastEmptyCellIndexFound = -1;
    }

    public AlternativeSolution2(Matrix matrix) {
        this.matrix = matrix;
        lastEmptyCellIndexFound = -1;
    }


    public Matrix getMatrix() {
        return matrix;
    }

    public TriangularNumber calculateZ(){
        Z = new TriangularNumber();
        for (int i = 0; i < TransData.staticInstance.getMatrixRowsNumber(); i++) {
            for (int j = 0; j < TransData.staticInstance.getMatrixColsNumber(); j++) {
                Z.additionOfTN(TransData.staticInstance.getFuzzyMatrixOfCosts()[i][j], matrix.getCellByCoords(i,j).getValue());
            }
        }
        return Z;
    }


    /**
     * Returns a new alternative solution.
     * @return new AS or null if there are no more new AS
     */
    public AlternativeSolution2 getNextSolution(){
        lastEmptyCellIndexFound = matrix.getEmptyCellPosition(lastEmptyCellIndexFound);
        if (lastEmptyCellIndexFound == -1){
            return null;
        }
        return new AlternativeSolution2(matrix.assignmentSteppingStone(lastEmptyCellIndexFound));
    }

    public TriangularNumber getZ() {
        return Z;
    }
}
