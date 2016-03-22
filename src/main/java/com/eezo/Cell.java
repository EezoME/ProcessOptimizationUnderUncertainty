package main.java.com.eezo;

/**
 *
 * Created by Eezo on 22.03.2016.
 */
public class Cell {
    private final int row; // or X
    private final int column; // or Y
    private int value;
    /**
     * Possible next of these values:<br/>
     * <li>  1 - cell marked '+'</li>
     * <li> -1 - cell marked '-'</li>
     * <li>  0 - cell has no marker</li>
     */
    private int status;

    public Cell(int row, int column, int value, int status) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.status = status;
    }

    public Cell(int row, int column) {
        this(row, column, 0, 0);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "row="+row+" col="+column+" value="+value+" status="+status;
    }
}