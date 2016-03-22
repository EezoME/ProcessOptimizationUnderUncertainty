package main.java.com.eezo.view;

import main.java.com.eezo.Messaging;
import main.java.com.eezo.TransData;
import main.java.com.eezo.TriangularNumber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.StringTokenizer;

/**
 * GUI that allows to input data via form manually.
 */
public class InputDataDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonCreateNewMatrix;
    private JButton buttonVendorAdd;
    private JList listCustomers;
    private JList listVendors;
    private JTable tableMatrix;
    private JButton buttonCustomerAdd;
    private JButton buttonCustomerDelete;
    private JButton buttonVendorDelete;
    private JCheckBox checkboxWriteData;
    private JTextField tfOutputFileName;
    private JTable tableFuzzyMatrix;
    private JButton buttonCreateNewFuzzyMatrix;

    public InputDataDialog() {
        //super(JFrame., "Редактирование входных данных", true);
        setModal(true);
        setContentPane(contentPane);
        setBounds(500, 310, 560, 340);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonCustomerAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customer = JOptionPane.showInputDialog("Введите значение в формате <название>-<значение>");
                if (!checkListRecord(customer)) {
                    JOptionPane.showMessageDialog(null, "Неверно введено значение.");
                    return;
                }
                // not good but it works
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < listCustomers.getModel().getSize(); i++) {
                    listModel.addElement(listCustomers.getModel().getElementAt(i));
                }
                listModel.addElement(customer);
                listCustomers.setModel(listModel);
                //DefaultListModel<> listModel = (DefaultListModel)listCustomers.getModel();
            }
        });
        buttonCustomerDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listCustomers.getSelectedIndex() == -1) {
                    return;
                }
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < listCustomers.getModel().getSize(); i++) {
                    if (i != listCustomers.getSelectedIndex()) {
                        listModel.addElement(listCustomers.getModel().getElementAt(i));
                    }
                }
                listCustomers.setModel(listModel);
            }
        });
        buttonVendorAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String vendor = JOptionPane.showInputDialog("Введите значение в формате <название> <значение>");
                if (!checkListRecord(vendor)) {
                    JOptionPane.showMessageDialog(null, "Неверно введено значение.");
                    return;
                }
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < listVendors.getModel().getSize(); i++) {
                    listModel.addElement(listVendors.getModel().getElementAt(i));
                }
                listModel.addElement(vendor);
                listVendors.setModel(listModel);
            }
        });
        buttonVendorDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listVendors.getSelectedIndex() == -1) {
                    return;
                }
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < listVendors.getModel().getSize(); i++) {
                    if (i != listVendors.getSelectedIndex()) {
                        listModel.addElement(listVendors.getModel().getElementAt(i));
                    }
                }
                listVendors.setModel(listModel);
            }
        });
        buttonCreateNewMatrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tableMatrix.getModel();
                model.setRowCount(listVendors.getModel().getSize());
                model.setColumnCount(listCustomers.getModel().getSize());
            }
        });
        buttonCreateNewFuzzyMatrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tableFuzzyMatrix.getModel();
                model.setRowCount(listVendors.getModel().getSize());
                model.setColumnCount(listCustomers.getModel().getSize());
            }
        });
        /** ----- */

        if (TransData.staticInstance == null) {
            Messaging.showMessageDialog("Введите данные на форму.");
            return;
        }
        loadLists();
        loadMatrix();
        loadFuzzyMatrix();
    }

    /**
     * Write trans. data lists on the form
     */
    private void loadLists() {
        if (TransData.staticInstance.getCustomersList() == null ||
                TransData.staticInstance.getCustomersVolumeList() == null) {
            Messaging.log("customers list or customers volume list is null", "warn");
            return;
        }
        if (TransData.staticInstance.getVendorsList() == null ||
                TransData.staticInstance.getVendorsVolumeList() == null) {
            Messaging.log("vendor list or vendor volume list is null", "warn");
            return;
        }
        String[] customers = new String[TransData.staticInstance.getMatrixColsNumber()];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = TransData.staticInstance.getCustomersList().get(i) + " " +
                    TransData.staticInstance.getCustomersVolumeList().get(i);
        }
        String[] vendors = new String[TransData.staticInstance.getMatrixRowsNumber()];
        for (int i = 0; i < vendors.length; i++) {
            vendors[i] = TransData.staticInstance.getVendorsList().get(i) + " " +
                    TransData.staticInstance.getVendorsVolumeList().get(i);
        }
        listCustomers.setListData(customers);
        listVendors.setListData(vendors);
        Messaging.log("trans data (lists) is written successfully on the form");
    }

    private void loadMatrix() {
        if (TransData.staticInstance.getMatrixOfCosts() == null) {
            Messaging.log("the matrix of costs is null", "warn");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableMatrix.getModel();
        model.setRowCount(TransData.staticInstance.getMatrixRowsNumber());
        if (TransData.staticInstance.getMatrixRowsNumber() > 0) {
            model.setColumnCount(TransData.staticInstance.getMatrixColsNumber());
        } else {
            Messaging.log("matrix rows number is less than 1", "warn");
            return;
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt(TransData.staticInstance.getMatrixOfCosts()[i][j], i, j);
            }
        }
        Messaging.log("trans data (matrix) is written successfully on the form");
    }

    private void loadFuzzyMatrix() {
        if (TransData.staticInstance.getFuzzyMatrixOfCosts() == null) {
            Messaging.log("the fuzzy matrix of costs is null", "warn");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableFuzzyMatrix.getModel();
        model.setRowCount(TransData.staticInstance.getMatrixRowsNumber());
        if (TransData.staticInstance.getMatrixRowsNumber() > 0) {
            model.setColumnCount(TransData.staticInstance.getMatrixColsNumber());
        } else {
            Messaging.log("fuzzy matrix rows number is less than 1", "warn");
            return;
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                model.setValueAt(TransData.staticInstance.getFuzzyMatrixOfCosts()[i][j].toParseFormat(), i, j);
            }
        }
        Messaging.log("trans data (fuzzy matrix) is written successfully on the form");
    }

    private void onOK() {
        TransData transData = new TransData();
        for (int i = 0; i < listVendors.getModel().getSize(); i++) {
            StringTokenizer st = new StringTokenizer(listVendors.getModel().getElementAt(i).toString());
            transData.getVendorsList().add(st.nextToken());
            transData.getVendorsVolumeList().add(Integer.parseInt(st.nextToken()));
        }
        for (int i = 0; i < listCustomers.getModel().getSize(); i++) {
            StringTokenizer st = new StringTokenizer(listCustomers.getModel().getElementAt(i).toString());
            transData.getCustomersList().add(st.nextToken());
            transData.getCustomersVolumeList().add(Integer.parseInt(st.nextToken()));
        }
        transData.setMatrixOfCosts(new int[tableMatrix.getRowCount()][tableMatrix.getColumnCount()]);
        for (int i = 0; i < tableMatrix.getRowCount(); i++) {
            for (int j = 0; j < tableMatrix.getColumnCount(); j++) {
                transData.getMatrixOfCosts()[i][j] = Integer.parseInt(tableMatrix.getValueAt(i, j).toString());
            }
        }
        transData.setFuzzyMatrixOfCosts(new TriangularNumber[tableFuzzyMatrix.getRowCount()][tableFuzzyMatrix.getColumnCount()]);
        for (int i = 0; i < tableFuzzyMatrix.getRowCount(); i++) {
            for (int j = 0; j < tableFuzzyMatrix.getColumnCount(); j++) {
                transData.getFuzzyMatrixOfCosts()[i][j] = new TriangularNumber(tableFuzzyMatrix.getValueAt(i, j).toString());
            }
        }
        TransData.staticInstance = transData;
        Messaging.log("Изменения сохранены.");
        if (checkboxWriteData.isSelected()) {
            Messaging.log("Запись данных в файл.");
            Messaging.writeFile(tfOutputFileName.getText());
        }

        dispose();
    }


    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private boolean checkListRecord(String record) {
        return !(record == null || record.isEmpty()) && record.matches("^[\"#'№!-&*а-яА-ЯёЁa-zA-Z0-9\\s]+[ ]\\s*[\\d]+$");
    }

    public static void main(String[] args) {
        InputDataDialog dialog = new InputDataDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
