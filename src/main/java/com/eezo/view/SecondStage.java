package main.java.com.eezo.view;

import javax.swing.*;
import java.awt.*;

/**
 *
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

    public SecondStage(){
        super("Второй этап");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(rootPanel);
        setBounds(380, 260, 780, 370);
        setVisible(true);
        buttonChooseAS.setEnabled(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SecondStage();
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
