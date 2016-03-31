package main.java.com.eezo.view;

import main.java.com.eezo.Messaging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Welcome form
 * Created by Eezo on 19.03.2016.
 */
public class Welcome extends JFrame implements FormInferface {
    private JPanel rootPanel;
    private JButton buttonLoadData;
    private JButton buttonChangeData;
    private JButton buttonGoToFS;
    private JButton buttonGoToSS;
    private JButton buttonExit;

    public Welcome(){
        super("Оптимизация процессов в условиях неопределённости");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setBounds(530, 400, 470, 130);
        setVisible(true);
        buttonLoadData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Выберете исходный файл");
                chooser.setCurrentDirectory(new File("..\\Project\\"));
                chooser.showDialog(rootPanel, "Выбрать файл");
                Messaging.readFile(chooser.getSelectedFile());
            }
        });
        buttonChangeData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputDataDialog.main(null);
            }
        });
        buttonGoToFS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToForms(1);
            }
        });
        buttonGoToSS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToForms(2);
            }
        });
    }

    private void goToForms(int formIndex){
        setVisible(false);
        if (formIndex == 1) {
            FirstStage.main(this);
        } else {
            SecondStage.main(this);
        }
    }


    @Override
    public void storeFormState() {
        throw new UnsupportedOperationException("This form has no data to store.");
    }

    @Override
    public void restoreFormState() {
        throw new UnsupportedOperationException("This form has no data to restore.");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Welcome();
            }
        });
    }
}
