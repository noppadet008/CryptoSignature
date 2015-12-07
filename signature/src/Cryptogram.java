/**
 * PACKAGE_NAME
 * Last modified by Admin on 12/6/2015.
 * 5:53 PM
 */
public class Cryptogram {
    private int a;
    private int[] b;

    Cryptogram(int a, int b[]) {
        this.a = a;
        this.b = b;
    }

    public int length() {
        return b.length;
    }

    public String toString() {
        return String.format(a + " " + b);
    }

    public int getA() {
        return a;
    }

    public int[] getB() {
        return b;
    }
}
