package main.java.com.eezo.view;

import main.java.com.eezo.AlternativeSolution2;
import main.java.com.eezo.Messaging;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Created by Eezo on 19.03.2016.
 */
public class CriteriasView extends JFrame implements FormInferface {
    private JPanel rootPanel;
    private JTable table1;
    private JButton buttonChooseAS;
    private JRadioButton radioButtonMM;
    private JRadioButton radioButtonBL;
    private JRadioButton radioButtonS;
    private JRadioButton radioButtonHW;
    private JRadioButton radioButtonHL;
    private JRadioButton radioButtonP;
    private JRadioButton radioButtonG;
    private JButton buttonCalculate;
    private JTextField textFieldQ1;
    private JLabel labelQ1;
    private JLabel labelQ2;
    private JLabel labelQ3;
    private JTextField textFieldQ2;
    private JTextField textFieldQ3;
    private JLabel labelC;
    private JLabel labelV;
    private JTextField textFieldV;
    private JTextField textFieldC;
    private JRadioButton radioButtonAll;
    private ButtonGroup buttonGroup;
    public static java.util.List<AlternativeSolution2> alternativeSolutions;
    private int[][] F;
    DefaultTableModel model;


    public CriteriasView() {
        super("Таблица расчётов по критериям");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(rootPanel);
        setBounds(380, 260, 780, 370);
        setVisible(true);
        buttonChooseAS.setEnabled(false);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonMM);
        buttonGroup.add(radioButtonBL);
        buttonGroup.add(radioButtonS);
        buttonGroup.add(radioButtonHW);
        buttonGroup.add(radioButtonHL);
        buttonGroup.add(radioButtonP);
        buttonGroup.add(radioButtonG);
        buttonGroup.add(radioButtonAll);
        hideOps();
        radioButtonMM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
            }
        });
        radioButtonBL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
                changeVisibleStatusForPossibilities(true);
            }
        });
        radioButtonS.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
            }
        });
        radioButtonHW.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
                changeVisibleStatusForC(true);
            }
        });
        radioButtonHL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
                changeVisibleStatusForPossibilities(true);
                changeVisibleStatusForV(true);
            }
        });
        radioButtonG.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
                changeVisibleStatusForPossibilities(true);
            }
        });
        radioButtonP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hideOps();
            }
        });
        radioButtonAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeVisibleStatusForPossibilities(true);
                changeVisibleStatusForV(true);
                changeVisibleStatusForC(true);
            }
        });
        buttonCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonGroup.getSelection() == null) {
                    Messaging.showMessageDialog("Выберите критерий!", "err");
                    return;
                }
                if (buttonGroup.getSelection() == radioButtonMM.getModel()) {
                    minimaxCriteria();
                } else if (buttonGroup.getSelection() == radioButtonBL.getModel()) {
                    bayesLaplaceCriteria();
                } else if (buttonGroup.getSelection() == radioButtonS.getModel()) {
                    savageCriteria();
                } else if (buttonGroup.getSelection() == radioButtonHW.getModel()) {
                    hurwitzCriteria();
                } else if (buttonGroup.getSelection() == radioButtonHL.getModel()) {
                    hodgeLehmannCriteria();
                } else if (buttonGroup.getSelection() == radioButtonG.getModel()) {
                    germeierCriteria();
                } else if (buttonGroup.getSelection() == radioButtonP.getModel()) {
                    productCriteria();
                } else {
                    allCriterias();
                }
                buttonChooseAS.setEnabled(true);
            }
        });
        buttonChooseAS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choose();
            }
        });
        initiateTable();
    }

    private void initiateTable() {
        F = new int[alternativeSolutions.size()][3];
        model = (DefaultTableModel) table1.getModel();
        model.setRowCount(F.length);
        model.setColumnCount(3);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3"});
        for (int i = 0; i < F.length; i++) {
            alternativeSolutions.get(i).getZ().unaryMinus();
            F[i] = alternativeSolutions.get(i).getZ().toArray();
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
        }
    }

    private void choose() {
        if (table1.getSelectedRow() == -1) {
            Messaging.showMessageDialog("Выберите строку-альтернативу!", "err");
            return;
        }
        SecondStage.rootAS = alternativeSolutions.get(table1.getSelectedRow());
        if (table1.getSelectedRow() == 0) {
            Messaging.showMessageDialog("Второй этап завершён - победила первая альтернатива: " + SecondStage.rootAS.getZ().toLineFormat());
            // TODO: total for all
            return;
        }
        SecondStage.rootAS.getMatrix().recalculateValues();
        dispose();
    }

    /**
     * CRITERIAS
     */

    private boolean[] minimaxCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        int[] mins = new int[F.length];
        for (int i = 0; i < mins.length; i++) {
            mins[i] = getMin(F[i]);
        }

        int max = getMax(mins);
        model.setColumnCount(5);
        clearTableInterval(3, 4);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3", "<html>e<sub>ir</sub>",
                "<html>max(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(mins[i], i, 3);
            if (max == mins[i]) {
                markers[i] = true;
                table1.setValueAt(mins[i], i, 4);
            }
        }
        return markers;
    }

    private boolean[] bayesLaplaceCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        float[] possibilities = getPossibilities();
        if (possibilities == null) {
            return null;
        }
        float[] e = new float[F.length];
        Arrays.fill(e, 0.0f);
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < F[i].length; j++) {
                e[i] += F[i][j] * possibilities[j];
            }
        }

        float max = getMax(e);
        model.setColumnCount(5);
        clearTableInterval(3, 4);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3", "<html>e<sub>ir</sub>",
                "<html>max(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(e[i], i, 3);
            if (Float.compare(max, e[i]) == 0) {
                markers[i] = true;
                table1.setValueAt(e[i], i, 4);
            }
        }
        return markers;
    }

    private boolean[] savageCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        int[] colMax = new int[3];
        Arrays.fill(colMax, Integer.MIN_VALUE);
        for (int i = 0; i < F.length; i++) {
            if (colMax[0] < F[i][0]) {
                colMax[0] = F[i][0];
            }
            if (colMax[1] < F[i][1]) {
                colMax[1] = F[i][1];
            }
            if (colMax[2] < F[i][2]) {
                colMax[2] = F[i][2];
            }
        }
        int[][] A = new int[F.length][F[0].length];
        for (int i = 0; i < A.length; i++) {
            A[i][0] = colMax[0] - F[i][0];
            A[i][1] = colMax[1] - F[i][1];
            A[i][2] = colMax[2] - F[i][2];
        }
        int[] maxes = new int[F.length];
        for (int i = 0; i < maxes.length; i++) {
            maxes[i] = getMax(A[i]);
        }

        int min = getMin(maxes);
        model.setColumnCount(8);
        clearTableInterval(3, 7);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3", "F1'", "F2'", "F3'",
                "<html>max(a<sub>ij</sub>)", "<html>min(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(A[i][0], i, 3);
            table1.setValueAt(A[i][1], i, 4);
            table1.setValueAt(A[i][2], i, 5);
            table1.setValueAt(maxes[i], i, 6);
            if (min == maxes[i]) {
                markers[i] = true;
                table1.setValueAt(maxes[i], i, 7);
            }
        }
        return markers;
    }

    private boolean[] hurwitzCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        float c = getC();
        if (c == -1) {
            return null;
        }
        float[] e = new float[F.length];
        for (int i = 0; i < e.length; i++) {
            e[i] = c * getMin(F[i]) + (1 - c) * getMax(F[i]);
        }

        float max = getMax(e);
        model.setColumnCount(5);
        clearTableInterval(3, 4);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3",
                "<html>c*min(e<sub>ij</sub>)+(1-c)*max(e<sub>ij</sub>)",
                "<html>min(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(e[i], i, 3);
            if (Float.compare(max, e[i]) == 0) {
                markers[i] = true;
                table1.setValueAt(e[i], i, 4);
            }
        }
        return markers;
    }

    private boolean[] hodgeLehmannCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        float v = getV();
        if (v == -1) {
            return null;
        }
        int[] mins = new int[F.length];
        for (int i = 0; i < mins.length; i++) {
            mins[i] = getMin(F[i]);
        }
        float[] possibilities = getPossibilities();
        float[] e = new float[F.length];
        Arrays.fill(e, 0.0f);
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < possibilities.length; j++) {
                e[i] += F[i][j] * possibilities[j];
            }
            e[i] *= v;
            e[i] += (1 - v) * mins[i];
        }

        float max = getMax(e);
        model.setColumnCount(6);
        clearTableInterval(3, 5);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3", "<html>min(e<sub>ij</sub>)",
                "<html>e<sub>ir</sub>", "<html>min(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(mins[i], i, 3);
            table1.setValueAt(e[i], i, 4);
            if (Float.compare(max, e[i]) == 0) {
                markers[i] = true;
                table1.setValueAt(e[i], i, 5);
            }
        }
        return markers;
    }

    private boolean[] germeierCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        float[] possibilities = getPossibilities();
        if (possibilities == null) {
            return null;
        }
        int a = Integer.MIN_VALUE;
        for (int i = 0; i < F.length; i++) {
            if (a < getMax(F[i])) {
                a = getMax(F[i]);
            }
        }
        int[][] F2 = new int[F.length][F[0].length];
        for (int i = 0; i < F2.length; i++) {
            for (int j = 0; j < F2[i].length; j++) {
                F2[i][j] = a - F[i][j];
            }
        }
        float[][] F3 = new float[F.length][F[0].length];
        for (int i = 0; i < F3.length; i++) {
            for (int j = 0; j < F3[i].length; j++) {
                F3[i][j] = F2[i][j] * possibilities[j];
            }
        }
        float[] e = new float[F.length];
        for (int i = 0; i < e.length; i++) {
            e[i] = getMin(F3[i]);
        }

        float min = getMin(e);
        model.setColumnCount(11);
        clearTableInterval(3, 10);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3", "F1'", "F2'", "F3'", "F1'*q1", "F2'*q2", "F3'*q3",
                "<html>min(e<sub>ij</sub>)",
                "<html>max(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(F2[i][0], i, 3);
            table1.setValueAt(F2[i][1], i, 4);
            table1.setValueAt(F2[i][2], i, 5);
            table1.setValueAt(F3[i][0], i, 6);
            table1.setValueAt(F3[i][1], i, 7);
            table1.setValueAt(F3[i][2], i, 8);
            table1.setValueAt(e[i], i, 9);
            if (Float.compare(min, e[i]) == 0) {
                markers[i] = true;
                table1.setValueAt(e[i], i, 10);
            }
        }
        return markers;
    }

    private boolean[] productCriteria() {
        boolean[] markers = new boolean[F.length];
        Arrays.fill(markers, false);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < F.length; i++) {
            if (min > getMin(F[i])) {
                min = getMin(F[i]) - 1;
            }
        }
        int[][] F1 = new int[F.length][F[0].length];
        for (int i = 0; i < F1.length; i++) {
            for (int j = 0; j < F1[i].length; j++) {
                F1[i][j] = F[i][j] - min;
            }
        }
        long[] product = new long[F.length];
        Arrays.fill(product, 1L);
        for (int i = 0; i < F1.length; i++) {
            for (int j = 0; j < F1[i].length; j++) {
                product[i] *= (long) F1[i][j];
            }
        }

        long max = getMax(product);
        model.setColumnCount(5);
        clearTableInterval(3, 4);
        model.setColumnIdentifiers(new Object[]{"F1", "F2", "F3", "<html>П(e<sub>ij</sub>)",
                "<html>max(e<sub>ir</sub>)</html>"});
        for (int i = 0; i < table1.getRowCount(); i++) {
            table1.setValueAt(F[i][0], i, 0);
            table1.setValueAt(F[i][1], i, 1);
            table1.setValueAt(F[i][2], i, 2);
            table1.setValueAt(product[i], i, 3);
            if (max == product[i]) {
                markers[i] = true;
                table1.setValueAt(product[i], i, 4);
            }
        }
        return markers;
    }

    private void allCriterias() {
        boolean[] mm = minimaxCriteria();
        boolean[] bl = bayesLaplaceCriteria();
        boolean[] s = savageCriteria();
        boolean[] hw = hurwitzCriteria();
        boolean[] hl = hodgeLehmannCriteria();
        boolean[] g = germeierCriteria();
        boolean[] p = productCriteria();
        boolean[][] allMarkers = new boolean[F.length][7];
        for (int i = 0; i < allMarkers[0].length; i++) {
            allMarkers[i][0] = mm[i];
            allMarkers[i][1] = bl[i];
            allMarkers[i][2] = s[i];
            allMarkers[i][3] = hw[i];
            allMarkers[i][4] = hl[i];
            allMarkers[i][5] = g[i];
            allMarkers[i][6] = p[i];
        }
        model.setColumnCount(8);
        model.setColumnIdentifiers(new Object[]{"", "MM", "BL", "S", "HW", "HL", "G", "P"});
        clearTableInterval(0, 7);
        for (int i = 0; i < table1.getRowCount(); i++) {
            for (int j = 0; j < table1.getColumnCount(); j++) {
                if (j == 0) {
                    table1.setValueAt("E" + (i + 1), i, j);
                } else {
                    table1.setValueAt(allMarkers[i][j - 1] ? "*" : "", i, j);
                }
            }
        }
    }

    /**
     * HELP METHODS
     */

    private int getMax(int[] line) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < line.length; i++) {
            if (max < line[i]) {
                max = line[i];
            }
        }
        return max;
    }

    private long getMax(long[] line) {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < line.length; i++) {
            if (max < line[i]) {
                max = line[i];
            }
        }
        return max;
    }

    private float getMax(float[] line) {
        float max = Integer.MIN_VALUE;
        for (int i = 0; i < line.length; i++) {
            if (max < line[i]) {
                max = line[i];
            }
        }
        return max;
    }

    private int getMin(int[] line) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < line.length; i++) {
            if (min > line[i]) {
                min = line[i];
            }
        }
        return min;
    }

    private float getMin(float[] line) {
        float min = Integer.MAX_VALUE;
        for (int i = 0; i < line.length; i++) {
            if (min > line[i]) {
                min = line[i];
            }
        }
        return min;
    }

    private float[] getPossibilities() {
        float[] possibilities = new float[3];
        try {
            possibilities[0] = Float.parseFloat(textFieldQ1.getText());
            possibilities[1] = Float.parseFloat(textFieldQ2.getText());
            possibilities[2] = Float.parseFloat(textFieldQ3.getText());
        } catch (NumberFormatException e) {
            Messaging.showMessageDialog("Неверный формат числа!", "err");
            return null;
        }
        if (Float.compare(possibilities[0] + possibilities[1] + possibilities[2], 1) != 0) {
            Messaging.showMessageDialog("Распределение вероятности в сумме должно равняться 1", "err");
            return null;
        }
        return possibilities;
    }

    private float getC() {
        float c;
        try {
            c = Float.parseFloat(textFieldC.getText());
        } catch (NumberFormatException e) {
            Messaging.showMessageDialog("Неверный формат числа!", "err");
            return -1;
        }
        if (c < 0 || c > 1) {
            Messaging.showMessageDialog("с должно быть в пределах от 0 до 1", "err");
            return -1;
        }
        return c;
    }

    private float getV() {
        float v;
        try {
            v = Float.parseFloat(textFieldV.getText());
        } catch (NumberFormatException e) {
            Messaging.showMessageDialog("Неверный формат числа!", "err");
            return -1;
        }
        if (v < 0 || v > 1) {
            Messaging.showMessageDialog("v должно быть в пределах от 0 до 1", "err");
            return -1;
        }
        return v;
    }

    private void clearTableInterval(int colStart, int colEnd) {
        for (int i = 0; i < table1.getRowCount(); i++) {
            for (int j = colStart; j <= colEnd; j++) {
                table1.setValueAt("", i, j);
            }
        }
    }

    private void hideOps() {
        changeVisibleStatusForPossibilities(false);
        changeVisibleStatusForV(false);
        changeVisibleStatusForC(false);
    }

    private void changeVisibleStatusForPossibilities(boolean flag) {
        labelQ1.setVisible(flag);
        labelQ2.setVisible(flag);
        labelQ3.setVisible(flag);
        textFieldQ1.setVisible(flag);
        textFieldQ2.setVisible(flag);
        textFieldQ3.setVisible(flag);
    }

    private void changeVisibleStatusForC(boolean flag) {
        labelC.setVisible(flag);
        textFieldC.setVisible(flag);
    }

    private void changeVisibleStatusForV(boolean flag) {
        labelV.setVisible(flag);
        textFieldV.setVisible(flag);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CriteriasView();
            }
        });
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
