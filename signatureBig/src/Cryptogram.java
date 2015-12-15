import java.math.BigInteger;

/**
 * PACKAGE_NAME
 * Last modified by Admin on 12/6/2015.
 * 5:53 PM
 */
public class Cryptogram {
    private BigInteger a;
    private BigInteger[] b;

    Cryptogram(BigInteger a, BigInteger b[]) {
        this.a = a;
        this.b = b;
    }

    public int length() {
        return b.length;
    }

    public String toString() {
        return String.format(a + " " + b);
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger[] getB() {
        return b;
    }
}
