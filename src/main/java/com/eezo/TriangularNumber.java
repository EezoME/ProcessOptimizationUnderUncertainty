package main.java.com.eezo;

import java.util.StringTokenizer;

/**
 * Class represents fuzzy triangular number, its properties and methods
 * <i>NOTE:</i> numbers are always integer.
 * Created by Eezo on 19.03.2016.
 */
public class TriangularNumber {
    private int a1;
    private int a0;
    private int a2;

    public TriangularNumber(int a1, int a0, int a2) {
        this.a1 = a1;
        this.a0 = a0;
        this.a2 = a2;
    }

    public TriangularNumber(String numbers){
        StringTokenizer st = new StringTokenizer(numbers, "\n\t,:;/ ");
        try {
            a1 = Integer.parseInt(st.nextToken());
            a0 = Integer.parseInt(st.nextToken());
            a2 = Integer.parseInt(st.nextToken());
        } catch (NumberFormatException e){
            Messaging.showMessageDialog("Ошибка при парсинге треугольного числа: "+numbers);
            a1 = 0;
            a0 = 0;
            a2 = 0;
        }
    }

    /** OPERATIONS */

    /**
     * Do the math.op. 'unary minus' with current object.
     */
    public void unaryMinus(){
        this.a1 = -this.a1;
        this.a0 = -this.a0;
        this.a2 = -this.a2;
    }

    /**
     * Check whether three numbers can be a triangular number.
     * @param a1 lower border (probability = 0)
     * @param a0 middle number (probability = 1)
     * @param a2 upper border (probability = 0)
     * @return <b>true</b> if it can, <b>false</b> - otherwise
     */
    public static boolean checkTN(int a1, int a0, int a2){
        return !(a1 > a0 || a0 > a2);
    }

    public String toParseFormat(){
        return a1+" "+a0+" "+a2;
    }

    @Override
    public String toString() {
        return "TriangularNumber{" +
                "a1=" + a1 +
                ", a0=" + a0 +
                ", a2=" + a2 +
                '}';
    }
}
