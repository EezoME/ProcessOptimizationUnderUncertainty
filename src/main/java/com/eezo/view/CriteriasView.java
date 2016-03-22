package main.java.com.eezo.view;

import javax.swing.*;
import java.awt.*;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public class CriteriasView extends JFrame implements FormInferface {
    private JPanel rootPanel;
    private JTable table1;
    private JButton buttonChooseAS;

    public CriteriasView(){
        super("Таблица расчётов по критериям");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(rootPanel);
        setBounds(380, 260, 780, 370);
        setVisible(true);
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
