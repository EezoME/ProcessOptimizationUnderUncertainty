package main.java.com.eezo.view;

import main.java.com.eezo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Eezo on 19.03.2016.
 */
public class SecondStage extends JFrame implements FormInferface {
    private JPanel rootPanel;
    private JTable tablePreviousAS;
    private JTable tableCurrentAS;
    private JLabel labelZ;
    private JButton buttonNext;
    private JButton buttonBack;
    private JList listZ;
    private JList listEpsilon;
    private JButton buttonChooseAS;
    private JLabel labelASOrder;
    private JFrame parent;

    /**
     * WORKS VARS
     */
    public static AlternativeSolution2 rootAS;
    private java.util.List<AlternativeSolution2> alternativeSolutions;

    public SecondStage(final JFrame parent) {
        super("Второй этап");
        this.parent = parent;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setBounds(380, 260, 780, 410);
        setVisible(true);
        buttonChooseAS.setEnabled(false);
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //storeFormState();
                parent.setVisible(true);
                dispose();
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
                //storeFormState();
                choose();
                buttonNext.setEnabled(true);
            }
        });
    }

    private void dataInitiation() {
        rootAS = new AlternativeSolution2(Matrix.northWestCorner());
        displayASOnTable(rootAS);
        alternativeSolutions = new ArrayList<>();
    }

    public static void main(final JFrame parent) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SecondStage(parent);
            }
        });
    }


    private void displayASOnTable(AlternativeSolution2 as) {
        DefaultTableModel model = (DefaultTableModel) tablePreviousAS.getModel();
        model.setRowCount(TransData.staticInstance.getMatrixRowsNumber());
        model.setColumnCount(TransData.staticInstance.getMatrixColsNumber());
        for (int i = 0; i < tablePreviousAS.getRowCount(); i++) {
            for (int j = 0; j < tablePreviousAS.getColumnCount(); j++) {
                tablePreviousAS.setValueAt(as.getMatrix().getCellByCoords(i, j).getValue() + " "
                        + (as.getMatrix().getCellByCoords(i, j).getStatus() == 0 ? "" :
                        (as.getMatrix().getCellByCoords(i, j).getStatus() == 1) ? "/ +" : "/ -"), i, j);
            }
        }
    }

    private void displayASOnTable(AlternativeSolution2 as, int order) {
        DefaultTableModel model = (DefaultTableModel) tableCurrentAS.getModel();
        model.setRowCount(TransData.staticInstance.getMatrixRowsNumber());
        model.setColumnCount(TransData.staticInstance.getMatrixColsNumber());
        for (int i = 0; i < tableCurrentAS.getRowCount(); i++) {
            for (int j = 0; j < tableCurrentAS.getColumnCount(); j++) {
                tableCurrentAS.setValueAt(as.getMatrix().getCellByCoords(i, j).getValue(), i, j);
            }
        }
        updateASLabel(order);
    }

    private void next() {
        if (alternativeSolutions == null){
            alternativeSolutions = new ArrayList<>();
        }
        alternativeSolutions.add(rootAS.getNextSolution()); // find new solution
        if (alternativeSolutions.get(alternativeSolutions.size() - 1) == null) {
            Messaging.showMessageDialog("На данной итерации больше нет новых альтернативных решений.\n" +
                    "Выберите альтернативу из представленных в списке отклонений (эпсилон)\n" +
                    "либо перейдите к расчёту по критериям.");
            buttonNext.setEnabled(false);
            buttonChooseAS.setEnabled(true);
            alternativeSolutions.remove(alternativeSolutions.size() - 1); // remove null AS
        } else {
            displayASOnTable(rootAS);
            alternativeSolutions.get(alternativeSolutions.size() - 1).getMatrix().recalculateValues(); // change values
            addItemToZList(alternativeSolutions.get(alternativeSolutions.size() - 1).calculateZ());
            addItemToEpsilonList(alternativeSolutions.get(alternativeSolutions.size() - 1).getZ().calculateEpsilon());
            displayASOnTable(alternativeSolutions.get(alternativeSolutions.size() - 1),
                    alternativeSolutions.size());
        }
    }

    private void choose() {
        if (listEpsilon.getSelectedIndex() != -1) {
            rootAS = alternativeSolutions.get(listEpsilon.getSelectedIndex());
            Messaging.showMessageDialog("Выбрано альтернативное решение №" + listEpsilon.getSelectedIndex() + ".");
            buttonNext.setEnabled(true);
            buttonChooseAS.setEnabled(false);
            return;
        }
        CriteriasView.main(null);
        CriteriasView.alternativeSolutions = new ArrayList<>();
        rootAS.calculateZ();
        CriteriasView.alternativeSolutions.add(rootAS);
        CriteriasView.alternativeSolutions.addAll(alternativeSolutions);
        alternativeSolutions = null;
        listEpsilon.setListData(new Object[0]);
        listZ.setListData(new Object[0]);
    }

    private void addItemToZList(TriangularNumber number) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < listZ.getModel().getSize(); i++) {
            listModel.addElement(listZ.getModel().getElementAt(i));
        }
        listModel.addElement("Z" + (listModel.getSize() + 1) + " = " + number.toLineFormat());
        listZ.setModel(listModel);
    }

    private void addItemToEpsilonList(float number) {
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < listEpsilon.getModel().getSize(); i++) {
            listModel.addElement(listEpsilon.getModel().getElementAt(i));
        }
        listModel.addElement("eps" + (listModel.getSize() + 1) + " = " + number);
        listEpsilon.setModel(listModel);
    }

    private void updateASLabel(int number) {
        labelASOrder.setText("Alternative solution #" + number);
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
