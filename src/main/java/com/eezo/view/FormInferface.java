package main.java.com.eezo.view;

/**
 *
 * Created by Eezo on 19.03.2016.
 */
public interface FormInferface {

    /**
     * Writes into a form's hidden fields data that remained on it.
     */
    void storeFormState();

    /**
     * Fills form's data from hidden fields.
     */
    void restoreFormState();
}
