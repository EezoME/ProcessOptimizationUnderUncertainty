package main.java.com.eezo.view;

import main.java.com.eezo.AlternativeSolution1;
import main.java.com.eezo.Matrix;
import main.java.com.eezo.Messaging;
import main.java.com.eezo.TransData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    private java.util.List<AlternativeSolution1> alternativeSolutions;

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
        buttonNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        dataInitiation();
        buttonChooseAS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choose();
            }
        });
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
        updateZLabel(rootAS.calculateZ());
        alternativeSolutions = new ArrayList<>();
    }

    private void next(){
        alternativeSolutions.add(rootAS.getNextSolution());
        if (alternativeSolutions.get(alternativeSolutions.size()-1) == null){
            Messaging.showMessageDialog("На данной итерации больше нет новых альтернативных решений.\n" +
                    "Выберите альтернативу из представленных в списке.");
            buttonNext.setEnabled(false);
            buttonChooseAS.setEnabled(true);
            alternativeSolutions.remove(alternativeSolutions.size()-1);
        } else {
            addItemToSigmaList(alternativeSolutions.get(alternativeSolutions.size()-1).calculateSigma());
            displayASOnTable(alternativeSolutions.get(alternativeSolutions.size()-1),
                    alternativeSolutions.size());
            //
        }
    }

    private void choose(){
        int minSigma = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < alternativeSolutions.size(); i++) {
            if (minSigma > alternativeSolutions.get(i).getSigma()){
                minSigma = alternativeSolutions.get(i).getSigma();
                index = i;
            }
        }
        if (minSigma > 0){
            Messaging.showMessageDialog("Не найдено лучших альтернатив, чем первое.\n" +
                    "Этап завершён, лучшая альтернатива - #0\n"+" (Z="+rootAS.calculateZ()+")");
            return;
        }
        if (checkBoxAutoChoose.isSelected()){
            rootAS = alternativeSolutions.get(index);
            Messaging.showMessageDialog("Выбрано альтернативное решение #"+(index+1)+" (Z="+rootAS.calculateZ()+")");
        } else {
            if (listSigma.getSelectedIndex() == -1){
                Messaging.showMessageDialog("Выберите альтернативу.","err");
                return;
            }
            for (int i = 0; i < TransData.staticInstance.getMatrixRowsNumber(); i++) {
                for (int j = 0; j < TransData.staticInstance.getMatrixColsNumber(); j++) {
                    System.out.println(alternativeSolutions.get(listSigma.getSelectedIndex()).getMatrix().getCellByCoords(i, j)+" ");
                }
            }
            rootAS = alternativeSolutions.get(listSigma.getSelectedIndex());
            System.out.println();
            for (int i = 0; i < TransData.staticInstance.getMatrixRowsNumber(); i++) {
                for (int j = 0; j < TransData.staticInstance.getMatrixColsNumber(); j++) {
                    System.out.println(rootAS.getMatrix().getCellByCoords(i, j)+" ");
                }
            }
        }
        rootAS.getMatrix().recalculateValues();
        displayASOnTable(rootAS, 0);
        updateZLabel(rootAS.calculateZ());
        alternativeSolutions = new ArrayList<>();
        listSigma.setListData(new Object[0]);
        buttonChooseAS.setEnabled(false);
        buttonNext.setEnabled(true);
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
                tableCurrentAS.setValueAt(as.getMatrix().getCellByCoords(i,j).getValue()+" "
                        +(as.getMatrix().getCellByCoords(i,j).getStatus() == 0 ? "" :
                        (as.getMatrix().getCellByCoords(i,j).getStatus() == 1) ? "/ +" : "/ -"), i, j);
            }
        }
        updateASLabel(order);
    }

    private void updateASLabel(int number){
        labelASOrder.setText("Alternative solution #"+number);
    }

    private void updateZLabel(int number){
        labelZ.setText(" Z = "+number);
    }

    private void addItemToSigmaList(int sigma){
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < listSigma.getModel().getSize(); i++) {
            listModel.addElement(listSigma.getModel().getElementAt(i));
        }
        listModel.addElement("sigma"+(listModel.getSize()+1)+" = "+sigma);
        listSigma.setModel(listModel);
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
