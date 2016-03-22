package main.java.com.eezo.view;

import main.java.com.eezo.AlternativeSolution1;
import main.java.com.eezo.Matrix;
import main.java.com.eezo.TransData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class FirstStage extends JFrame implements FormInferface {
    private JPanel rootPanel;
    private JTable tableInputData;
    private JTable tableCurrentAS;
    private JButton buttonNWCorner;
    private JButton buttonNext;
    private JLabel labelZ;
    private JList listSigma;
    private JButton buttonChooseAS;
    private JCheckBox checkBoxAutoChoose;
    private JButton buttonBack;
    private JLabel labelASOrder;
    private JFrame parent;

    /** WORKS VARS */
    private AlternativeSolution1 rootAS;

    public FirstStage(final JFrame parent){
        super("Первый этап");
        this.parent = parent;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setBounds(380, 260, 780, 370);
        setVisible(true);
        buttonNext.setEnabled(false);
        buttonChooseAS.setEnabled(false);
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //storeFormState();
                parent.setVisible(true);
                dispose();
            }
        });
        buttonNWCorner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateRootAS();
                buttonNWCorner.setEnabled(false);
                buttonNext.setEnabled(true);
            }
        });
        dataInitiation();
    }

    /**
     * Initializes trans data into table for best viewing
     */
    private void dataInitiation(){
        DefaultTableModel model = (DefaultTableModel) tableInputData.getModel();
        model.setRowCount(TransData.staticInstance.getMatrixRowsNumber()+2);
        model.setColumnCount(TransData.staticInstance.getMatrixColsNumber()+2);
        for (int i = 1; i < tableInputData.getColumnCount()-1; i++) {
            tableInputData.setValueAt(TransData.staticInstance.getCustomersList().get(i-1), 0, i);
            tableInputData.setValueAt(TransData.staticInstance.getCustomersVolumeList().get(i-1), tableInputData.getRowCount()-1, i);
        }
        for (int i = 1; i < tableInputData.getRowCount()-1; i++) {
            tableInputData.setValueAt(TransData.staticInstance.getVendorsList().get(i-1), i, 0);
            tableInputData.setValueAt(TransData.staticInstance.getVendorsVolumeList().get(i-1),i,  tableInputData.getColumnCount()-1);
        }
        for (int i = 1; i < tableInputData.getRowCount()-1; i++) {
            for (int j = 1; j < tableInputData.getColumnCount()-1; j++) {
                tableInputData.setValueAt(TransData.staticInstance.getMatrixOfCosts()[i-1][j-1], i, j);
            }
        }
    }

    private void calculateRootAS(){
        rootAS = new AlternativeSolution1(Matrix.northWestCorner());
        displayASOnTable(rootAS, 0);
    }

    public static void main(final JFrame parent) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FirstStage(parent);
            }
        });
    }

    private void displayASOnTable(AlternativeSolution1 as, int order){
        DefaultTableModel model = (DefaultTableModel) tableCurrentAS.getModel();
        model.setRowCount(TransData.staticInstance.getMatrixRowsNumber());
        model.setColumnCount(TransData.staticInstance.getMatrixColsNumber());
        for (int i = 0; i < tableCurrentAS.getRowCount(); i++) {
            for (int j = 0; j < tableCurrentAS.getColumnCount(); j++) {
                tableCurrentAS.setValueAt(as.getMatrix().getCellByCoords(i,j).getValue(), i, j);
            }
        }
        labelASOrder.setText(String.valueOf(order));
    }

    @Override
    public void storeFormState() {
        throw new UnsupportedOperationException("This form has no data to store.");
    }

    @Override
    public void restoreFormState() {
        throw new UnsupportedOperationException("This form has no data to restore.");
    }
}
