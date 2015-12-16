import java.util.*;

public class GenKeyIntTool {
    private static int[] setPower;
    private static long[] storagePower;
    private static int[] hashStorage;


    /**
     * method to find gcd with two number
     *
     * @param firstNumber   int - a number to find gcd with next param
     * @param anotherNumber int - a number to find gcd with before param
     * @return a gcd in type int
     */
    int gcd(int firstNumber, int anotherNumber) {
        if (anotherNumber == 0) {
            return firstNumber;
        } else if (firstNumber == 0) {
            return anotherNumber;
        } else {
            return gcd(anotherNumber, firstNumber % anotherNumber);
        }
    } // end funcrion gcd

    /**
     * to prove tester is prime Ice edition
     *
     * @param n a number to test prime.
     * @return true when a is prime (probability)
     */
    boolean lehmanTest(int n) {
        if (n % 2 == 0 && n < 3) {
            log("lehman test n only odd and n > 2");
            return false;
        }
        boolean isPrime = true;
        int power = (n - 1) / 2;
        int result, a;
        if (n < 100) {
            for (a = 3; a < n; a++) {
                result = fastExpo(a, power, n);
                isPrime = gcd(a, n) <= 1 && (result == n - 1 || result == 1);
                if (!isPrime) break; //break when not test failed.
            }
        } else {
            for (int b = 0; b < 100; b++) {
                a = (int) (Math.random() * n);
                result = fastExpo(a, power, n);
                isPrime = gcd(a, n) <= 1 && (result == n - 1 || result == 1);
                if (!isPrime) break;
            }
        }
        return isPrime;
    } // end function LehmanTest



    private boolean checkGeneratorII(int tester, int mod) {
        if (tester < 0) return false;
        int result = fastExpo(tester, (mod - 1) / 2, mod);
        boolean check = result != 1;
        //System.out.println(result); //track result
        return check;
    } // end function checkGeneratorII

    public static void log(Object anyThings) {
        System.out.println(String.valueOf(anyThings));
    } // end function log





    /**
     * base must not exceed result.
     * ezy copy form net.
     *
     * @param base
     * @param power
     * @param mod
     * @return int base^power
     */
    int fastExpo(int base, int power, int mod) {
        int result = 1;
        int precompu = base;
        while (power > 0) {//power 0 do nothing
            if ((power % 2) == 1) {
                result = Math.toIntExact((long) result * precompu % mod);
            }
            precompu = Math.toIntExact((long) precompu * precompu % mod);
            power >>= 1;
        }
        return result % mod;
    }

    /**
     * generate p from a file form default fileOrganize class
     *
     * @return int-generate form first 10 bit of file
     */
    public int generateP(int bitCount) {
        FileOrganize file = new FileOrganize();
        byte[] content = file.read();
        int i = 0, index = 0;
        byte pick = content[index];
        log("init bitGroupP");
        // initial bitGroup
        for (int j = 0; j < bitCount; j++) {
            i <<= 1;
            i += (pick & 0b1);
            pick >>= 1;
            if (j % 7 == 0) {
                index++;
                pick = content[index];
            }
        }
        log("shifting");
        //shift for 1 in LSB
        int mask = 0b1 << (bitCount - 1);
        while ((i & mask) == 0) {
            if (pick == 0) {//pick the next byte
                index++;
                pick = content[index];
            }
            i <<= 1;
            i &= (mask << 1);//cut MSB
            i += (pick & 0b1);//add LSB
            pick >>= 1;
        }
        if (i % 2 == 0) i++;//plus 1 to odd
        log("lehmann testing and increasing when false i before test = " + i);
        //increse one if not prime
        int count = 0;
        while (!(lehmanTest(i) && lehmanTest((i - 1) / 2))) { //!1||!2 == !(1&&2)
            i += 2;
            count++;
        }//got p is safe Prime
        log("got p false " +count);
        return i;
    }

    /**
     * genkey write on file key.txt.
     * @param n number group of bit
     */
    public void genKey(int n) {
        int p = generateP(n);
        int generator = -1;//not init
        int count = 0;//reset count for track next
        while (!checkGeneratorII(generator, p) || generator < 2) {
            count++;
            generator = (int) (Math.random() * p);
        }
        log("generator random false " + count + " time");
        //create private key
        int u = (int) (Math.random() * p);
        System.out.println("u is "+u);
        //create public key
        int y = fastExpo(generator, u, p);

        String s = String.format("u = %d (p,g,y) is (%d,%d,%d)", u, p, generator, y);
        FileOrganize file = new FileOrganize("key.txt");
        file.write(s.getBytes());
        log(s);
    }





} // end class
