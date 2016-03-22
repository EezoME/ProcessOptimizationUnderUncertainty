package main.java.com.eezo;

import java.util.List;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class Matrix {
    private Cell[] matrix;
    boolean isSteppingStoneUsed;

    public Matrix(int rowsCount, int colsCount) {
        this.matrix = new Cell[rowsCount*colsCount];
        int counter = 0;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                matrix[counter++] = new Cell(i, j);
            }
        }
        isSteppingStoneUsed = false;
    }

    public Cell getCellByCoords(int row, int col){
        for (Cell aMatrix : matrix) {
            if (aMatrix.getRow() == row && aMatrix.getColumn() == col) {
                return aMatrix;
            }
        }
        return null;
    }

    /**
     * Generates new matrix using north-west corner method.
     * @return a new matrix
     */
    public static Matrix northWestCorner() {
        if (TransData.staticInstance.getCustomersVolumeList() == null || TransData.staticInstance.getVendorsVolumeList() == null ||
                TransData.staticInstance.getCustomersVolumeList().size() == 0 ||
                TransData.staticInstance.getVendorsVolumeList().size() == 0) {
            Messaging.log("Customers volume list or vendors volume list is null or zero length.","err");
            return null;
        }
        /** S - customers; N - vendors */
        // check for condition sum Sj == sum Ni
        int sumOfS = 0;
        int sumOfN = 0;
        for (int i = 0; i < TransData.staticInstance.getCustomersVolumeList().size(); i++) {
            sumOfS += TransData.staticInstance.getCustomersVolumeList().get(i);
        }
        for (int i = 0; i < TransData.staticInstance.getVendorsVolumeList().size(); i++) {
            sumOfN += TransData.staticInstance.getVendorsVolumeList().get(i);
        }
        if (sumOfS != sumOfN) {
            Messaging.showMessageDialog("Расчёт перевозок методом северо-западного угла невозможен:\n" +
                    "Сумма поставок не совпадает с суммой заявок!", "err");
            Messaging.log("Расчёт перевозок методом северо-западного угла невозможен:\n" +
                    "Сумма поставок не совпадает с суммой заявок!", "err");
            return null;
        }

        // create a new matrix
        Matrix matrix = new Matrix(TransData.staticInstance.getMatrixRowsNumber(), TransData.staticInstance.getMatrixColsNumber());
        List<Integer> S = TransData.staticInstance.getCustomersVolumeList();
        List<Integer> N = TransData.staticInstance.getVendorsVolumeList();
        for (int i = 0; i < TransData.staticInstance.getMatrixRowsNumber(); i++) {
            for (int j = 0; j < TransData.staticInstance.getMatrixColsNumber(); j++) {
                if (N.get(i) < S.get(j)) {
                    matrix.getCellByCoords(i, j).setValue(N.get(i));
                    S.set(j, S.get(j) - N.get(i));
                    N.set(i, 0);
                    break;
                } else {
                    matrix.getCellByCoords(i, j).setValue(S.get(j));
                    N.set(i, N.get(i) - S.get(j));
                    S.set(j, 0);
                }
            }
        }
        return matrix;
    }

    public void assignmentSteppingStone(int lastEnptyCellPosFound){
        if (lastEnptyCellPosFound < 0 || lastEnptyCellPosFound >= matrix.length){
            throw new ArrayIndexOutOfBoundsException(lastEnptyCellPosFound);
        }
        isSteppingStoneUsed = true;
        //TODO finish this
    }

    public void recalculateValues(){
        if (!isSteppingStoneUsed){
            throw new IllegalStateException("Attempt to recalculate of matrix values" +
                    " without using stepping stone method.");
        }
        // TODO finish this
    }
}
