package main.java.com.eezo;

import java.util.ArrayList;
import java.util.List;

/**
 * Static data from file.
 * Created by Eezo on 19.03.2016.
 */
    /*
    ТранспДанные(TransData) - статический объект;
    содержит в себе скрытые поля для хранения входных данных о поставщиках, заказчиках,
    матрицах растратов на единицу товара (простую и с нечёткими треугольными числами),
    а также поля размерности матрицы (количествоСтрок и количествоСтолбцов)
     */
public class TransData {
    /**
     * Static instance of TransData.
     * Represents unified access to data from file.
     */
    public static TransData staticInstance;

    /**
     * List of vendors (sellers) names <br/>
     * <i>NOTE:</i> this list size must be equals to the list of vendors volume size
     */
    private List<String> vendorsList; // or seller
    /**
     * List of customers names <br/>
     * <i>NOTE:</i> this list size must be equals to the list of customers volume size
     */
    private List<String> customersList;
    /**
     * List of vendors volume of shipments <br/>
     * <i>NOTE:</i> this list size must be equals to the list of vendors size
     */
    private List<Integer> vendorsVolumeList; // .size() = vendorsList.size()
    /**
     * List of customers volume of orders <br/>
     * <i>NOTE:</i> this list size must be equals to the list of customers size
     */
    private List<Integer> customersVolumeList;// .size() = customersList.size()
    /**
     * Matrix of costs per unit of goods
     */
    private int[][] matrixOfCosts;
    /**
     * Fuzzy matrix of costs per unit of goods <br/>
     * (matrix[i][j] is a TriangularNumber instance)
     */
    private TriangularNumber[][] fuzzyMatrixOfCosts;
    /**
     * Number of rows in matrices of costs
     */
    private int matrixRowsNumber = 0;
    /**
     * Number of columns in matrices of costs
     */
    private int matrixColsNumber = 0;

    public TransData() {
        vendorsList = new ArrayList<>();
        customersList = new ArrayList<>();
        vendorsVolumeList = new ArrayList<>();
        customersVolumeList = new ArrayList<>();
    }

    public TransData(List<String> vendorsList, List<String> customersList, List<Integer> vendorsVolumeList, List<Integer> customersVolumeList, int[][] matrixOfCosts, TriangularNumber[][] fuzzyMatrixOfCosts) {
        this.vendorsList = vendorsList;
        this.customersList = customersList;
        this.vendorsVolumeList = vendorsVolumeList;
        this.customersVolumeList = customersVolumeList;
        this.matrixOfCosts = matrixOfCosts;
        this.fuzzyMatrixOfCosts = fuzzyMatrixOfCosts;
    }

    public List<String> getVendorsList() {
        return vendorsList;
    }

    public void setVendorsList(List<String> vendorsList) {
        this.vendorsList = vendorsList;
        this.matrixRowsNumber = this.vendorsList.size();
    }

    public List<String> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<String> customersList) {
        this.customersList = customersList;
        this.matrixColsNumber = customersList.size();
    }

    public List<Integer> getVendorsVolumeList() {
        return vendorsVolumeList;
    }

    public void setVendorsVolumeList(List<Integer> vendorsVolumeList) {
        this.vendorsVolumeList = vendorsVolumeList;
    }

    public List<Integer> getCustomersVolumeList() {
        return customersVolumeList;
    }

    public void setCustomersVolumeList(List<Integer> customersVolumeList) {
        this.customersVolumeList = customersVolumeList;
    }

    public int[][] getMatrixOfCosts() {
        return matrixOfCosts;
    }

    public void setMatrixOfCosts(int[][] matrixOfCosts) {
        this.matrixOfCosts = matrixOfCosts;
    }

    public TriangularNumber[][] getFuzzyMatrixOfCosts() {
        return fuzzyMatrixOfCosts;
    }

    public void setFuzzyMatrixOfCosts(TriangularNumber[][] fuzzyMatrixOfCosts) {
        this.fuzzyMatrixOfCosts = fuzzyMatrixOfCosts;
    }

    public int getMatrixRowsNumber() {
        return matrixRowsNumber;
    }

    public int getMatrixColsNumber() {
        return matrixColsNumber;
    }
}
