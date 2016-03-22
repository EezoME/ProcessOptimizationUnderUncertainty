package main.java.com.eezo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class Matrix implements Cloneable {
    private Cell[] matrix;
    //boolean isSteppingStoneUsed;

    public Matrix(int rowsCount, int colsCount) {
        this.matrix = new Cell[rowsCount*colsCount];
        int counter = 0;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < colsCount; j++) {
                matrix[counter++] = new Cell(i, j);
            }
        }
        //isSteppingStoneUsed = false;
    }

    private Matrix(Cell[] cells){
        /*this.matrix = new Cell[cells.length];
        for (int i = 0; i < matrix.length; i++) {
            this.matrix[i] = new Cell(cells[i].getRow(), cells[i].getColumn(), cells[i].getValue(), 0);
        }*/
        this.matrix = cells.clone();
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

    public Matrix assignmentSteppingStone(int lastEmptyCellPosFound){
        if (lastEmptyCellPosFound < 0 || lastEmptyCellPosFound >= matrix.length){
            throw new ArrayIndexOutOfBoundsException(lastEmptyCellPosFound);
        }
        Matrix newMatrix = new Matrix(matrix);
        newMatrix.resetStatuses();
        //System.out.println(newMatrix+" "+matrix);
        Cell startCell = newMatrix.matrix[lastEmptyCellPosFound];
        List<Cell> cells = new ArrayList<>();
        cells.add(startCell);
        boolean flag = true;
        do {
            if (flag) {
                cells.add(checkRow(cells.get(cells.size() - 1), startCell));
            } else {
                cells.add(checkCol(cells.get(cells.size() - 1), startCell));
            }
            Messaging.log("Adding cell: "+cells.get(cells.size() - 1));
            if (cells.get(cells.size() - 1) == null || cells.get(cells.size() - 1) == startCell){
                break;
            }
            flag = !flag;
        } while (true);
        cells.remove(cells.size()-1);

        for (int i = 0; i < cells.size(); i++) {
            cells.get(i).setStatus(i % 2 == 0 ? 1 : -1);
        }

        for (int i = 0; i < cells.size(); i++) {
            for (int j = 0; j < newMatrix.matrix.length; j++) {
                if (cells.get(i).equals(newMatrix.matrix[j])){
                    newMatrix.matrix[j].setStatus(cells.get(i).getStatus());
                    break;
                }
            }
        }
        return newMatrix;
    }

    public void recalculateValues(){
        /*if (!isSteppingStoneUsed){
            throw new IllegalStateException("Attempt to recalculate of matrix values" +
                    " without using stepping stone method.");
        }*/
        int minValue = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getStatus() == -1 && minValue > matrix[i].getValue()){
                minValue = matrix[i].getValue();
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getStatus() == -1){
                matrix[i].setValue(matrix[i].getValue()-minValue);
                continue;
            }
            if (matrix[i].getStatus() == 1){
                matrix[i].setValue(matrix[i].getValue()+minValue);
            }
        }
    }

    public int getEmptyCellPosition(int lastEmptyCellPosition){
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getValue() == 0 && i > lastEmptyCellPosition){
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns:<ul>
     *     <li><b>{targetCell}</b> - if row contains {targetCell} and {currentCell} != {targetCell}</li>
     *     <li><b>a cell from row</b> - if column of that cell contains non zero value cells</li>
     *     <li><b>null</b> - otherwise</li>
     * </ul>
     * @param currentCell cell entry
     * @param targetCell target cell
     * @return resulting cell
     */
    /*  Проверка текущей строки(ячейка вхождения, начальная позиция)
        Если она содержит НП, которая не является ЯВ, то возврат - НП
        Иначе проверка заполненых ячеек
        Если столбец ячейки содержит непустые значения, то возврат - заполненая ячейка строки
     */
    private Cell checkRow(Cell currentCell, Cell targetCell){
        Cell[] cells = getSpecificRowElements(currentCell);
        if (currentCell.getColumn() > TransData.staticInstance.getMatrixColsNumber()/2){
            for (int i = cells.length-1; i >= 0; i--) {
                if (cells[i] == currentCell)
                    continue;
                if (cells[i] == targetCell && targetCell != currentCell){
                    return cells[i];
                }
                if (cells[i].getValue() == 0){
                    continue;
                }
                // если колонка со значениями, вернёт ячейку, просчитать чтобы посморела что там нет таргет
                if (checkLineHasMoreValues(cells[i], "col", targetCell)){
                    return cells[i];
                }
            }
        } else {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] == currentCell)
                    continue;
                if (cells[i] == targetCell && targetCell != currentCell){
                    return cells[i];
                }
                if (cells[i].getValue() == 0){
                    continue;
                }
                // если колонка со значениями, вернёт ячейку, просчитать чтобы посморела что там нет таргет
                if (checkLineHasMoreValues(cells[i], "col", targetCell)){
                    return cells[i];
                }
            }
        }
        return null;
    }

    /**
     * Returns:<ul>
     *     <li><b>{targetCell}</b> - if column contains {targetCell} and {currentCell} != {targetCell}</li>
     *     <li><b>a cell from column</b> - if row of that cell contains non zero value cells</li>
     *     <li><b>null</b> - otherwise</li>
     * </ul>
     * @param currentCell cell entry
     * @param targetCell target cell
     * @return resulting cell
     */
    /*  Проверка текущего столбца(ячейка вхождения, начальная позиция)
        Если она содержит НП, которая не является ЯВ, то возврат - НП
        Иначе проверка заполненых ячеек
        Если строка ячейки содержит непустые значения, то возврат - заполненная ячейка столбца
     */
    private Cell checkCol(Cell currentCell, Cell targetCell){
        Cell[] cells = getSpecificColElements(currentCell);
        if (currentCell.getRow() > TransData.staticInstance.getMatrixRowsNumber()/2){
            for (int i = cells.length-1; i >= 0; i--) {
                if (cells[i] == currentCell)
                    continue;
                if (cells[i].getValue() == 0) {
                    continue;
                }
                if (checkLineForTarget(cells, targetCell) && targetCell != currentCell) {
                    return targetCell;
                }
                if (checkLineHasMoreValues(cells[i], "row", targetCell)) {
                    return cells[i];
                }
            }
        } else {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] == currentCell)
                    continue;
                if (cells[i].getValue() == 0) {
                    continue;
                }
                if (checkLineForTarget(cells, targetCell) && targetCell != currentCell) {
                    return targetCell;
                }
                if (checkLineHasMoreValues(cells[i], "row", targetCell)) {
                    return cells[i];
                }
            }
        }
        return null;
    }

    /**
     * Check line (row or column) for non zero values
     * @param currentCell core cell
     * @param lineType "row" or "col"
     * @return <b>true</b> - if line has non zero values, <b>false</b> - otherwise
     */
    private boolean checkLineHasMoreValues(Cell currentCell, String lineType, Cell targetCell){
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == currentCell){
                continue;
            }
            if (lineType.equalsIgnoreCase("row")){
                if (matrix[i].getRow() == currentCell.getRow()){
                    if (matrix[i].getValue() != 0 || matrix[i] == targetCell)
                        return true;
                }
            } else {
                if (matrix[i].getColumn() == currentCell.getColumn()){
                    if (matrix[i].getValue() != 0 || matrix[i] == targetCell)
                        return true;
                }
            }
        }
        return false;
    }

    private boolean checkLineForTarget(Cell[] cells, Cell target){
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == target){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a row, which contains specific cell
     * @param cell specific cell
     * @return an array of cells
     */
    private Cell[] getSpecificRowElements(Cell cell){
        Cell[] cells = new Cell[TransData.staticInstance.getMatrixColsNumber()];
        int ind = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].equals(cell)){
            }
            if (matrix[i].getRow() == cell.getRow()){
                cells[ind++] = matrix[i];
            }
        }
        return cells;
    }

    /**
     * Returns a column, which contains specific cell
     * @param cell specific cell
     * @return an array of cells
     */
    private Cell[] getSpecificColElements(Cell cell){
        Cell[] cells = new Cell[TransData.staticInstance.getMatrixRowsNumber()];
        int ind = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].getColumn() == cell.getColumn()){
                cells[ind++] = matrix[i];
            }
        }
        return cells;
    }

    private void resetStatuses(){
        for (int i = 0; i < matrix.length; i++) {
            matrix[i].setStatus(0);
        }
    }
}
