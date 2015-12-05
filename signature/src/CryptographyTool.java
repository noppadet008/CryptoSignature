import java.util.*;

public class CryptographyTool {
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
     * method to find inverse with extended gcd algorithm</br>
     * not fix param (change position param is same result)
     *
     * @param firstNumber   a number
     * @param anotherNumber a number
     */
    void findInverse(double firstNumber, double anotherNumber) {
        if (gcd((int) firstNumber, (int) anotherNumber) != 1) {
            log("Can't find inverse because they're not relative prime.");
            return;
            // Go to error!
        }

        int a1, b1, a2, b2, r, q,
                temp_a2, temp_b2, n1, n2, max, min;

        if (firstNumber > anotherNumber) {
            max = (int) firstNumber;
            min = (int) anotherNumber;
            n1 = (int) firstNumber;
            n2 = (int) anotherNumber;
        } else {
            max = (int) anotherNumber;
            min = (int) firstNumber;
            n1 = (int) anotherNumber;
            n2 = (int) firstNumber;
        }


        a1 = b2 = 1;
        b1 = a2 = 0;
        temp_a2 = a2;
        temp_b2 = b2;

        r = n1 % n2;
        q = n1 / n2;

        log("n1 n2 r q a1 a2 b1 b2\n----------------------------");
        log(n1 + " " + n2 + " "
                + r + " " + q + " " + a1 + " " + a2 + " " + b1 + " "
                + b2);

        while (r != 0) {
            n1 = n2;
            n2 = r;
            a2 = a1 - q * a2;
            b2 = b1 - q * b2;
            a1 = temp_a2;
            b1 = temp_b2;
            temp_a2 = a2;
            temp_b2 = b2;
            r = n1 % n2;
            q = n1 / n2;

            log(n1 + " " + n2 + " "
                    + r + " " + q + " " + a1 + " " + a2 + " " + b1 + " "
                    + b2);

        }

		/*log(n1 + " " + n2 + " " 
            + r + " " + q + " " + a1 + " " + a2 + " " + b1 + " "
			+ b2);*/

        log("");
        if (firstNumber == max) {
            //return a2;
            if (a2 < 0) {
                log("Inverse of " + (int) firstNumber + " mod " +
                        (int) anotherNumber + " = " + a2 + " = " + ((int) (a2 - anotherNumber * Math.floor(a2 / anotherNumber))));
                //return (int)(a2-anotherNumber*Math.floor(a2/anotherNumber));
            } else
                log("Inverse of " + (int) firstNumber + " mod " + (int) anotherNumber + " = " + a2);
            //return (int)a2;
        } else {
            //return b2
            if (b2 < 0) {
                log("Inverse of " + (int) firstNumber + " mod " +
                        (int) anotherNumber + " = " + b2 + " = " + ((int) (b2 - anotherNumber * Math.floor(b2 / anotherNumber))));
                //return (int)(b2-anotherNumber*Math.floor(b2/anotherNumber));
            } else
                log("Inverse of " + (int) firstNumber + " mod " + (int) anotherNumber + " = " + b2);
            //return (int)b2;
        }

        //System.out.println(n2 == a2*max+b2*min);

    } // end function findInverse

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
        if (n < 300) {
            for (a = 3; a < n; a++) {
                result = fastfac(a, power, n);
                isPrime = gcd(a, n) <= 1 && (result == n - 1 || result == 1);
            }
        } else {
            for (int b = 0; b < 300; b++) {
                a = (int) (Math.random() * n);
                result = fastfac(a, power, n);
                isPrime = gcd(a, n) <= 1 && (result == n - 1 || result == 1);
            }
        }
        return isPrime;
    } // end function LehmanTest

    /**
     * method part of fastexponent funcion to create power set
     *
     * @param power a number to represent power
     */
    void factorizationPower(int power) {
        int n = 1, powerStart = 1, remain;
        while (powerStart < power) {
            powerStart *= 2;
            n++;
        }
        powerStart /= 2;
        n--;
        setPower = new int[n];
        log("\nSize setPower = " + n);
        setPower[0] = powerStart;
        remain = power - setPower[0];
        for (int i = 1; remain > 0; i++) {
            while (powerStart > remain) {
                powerStart /= 2;
            }
            setPower[i] = powerStart;
            remain = remain - setPower[i];
        }

        for (int k = 0; k < n; k++) {
            log("setPower[" + k + "] = " + setPower[k]);
        }
    } // end function factorizationPower

    /**
     * part of fast exponent method create storage power set
     *
     * @param base     a number base
     * @param modulate a modulator
     * @return void
     */
    void storagePower(int base, int modulate) {
        int n = (int) (Math.log(setPower[0]) / Math.log(2)) + 1;
        log("\nSize storagePower = " + n);
        storagePower = new long[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                storagePower[i] = (long) Math.pow(base, 1) % modulate;
            } else {
                storagePower[i] = (long) Math.pow(storagePower[i - 1], 2) % modulate;
            }
        }

        for (int k = 0; k < n; k++) {
            log("storagePower[" + k + "] = " + storagePower[k]);
        }
    } // end function storagePower


    /**
     * method to multiply in power storage set
     *
     * @param modulation modulator
     */
    int combinationTogether(int modulation) {
        long product = 1;
        int realSize = 0;
        for (int aSetPower : setPower) {
            if (aSetPower != 0)
                realSize++;
        }
        log("Real size: " + realSize);
        for (int i = 0; i < realSize; i++) {
            product = product * storagePower[(int) (Math.log(setPower[i]) / Math.log(2))];
            log("\n------------\nproduct = " + product);
        }
        product %= modulation;
        log("\n------------\nResult = " + product);
        return Math.toIntExact(product);
    } // end function combinationTogether

    /**
     * method equivalent pow(base,power)% modulation
     * with avoid to overflow
     *
     * @param base       int - base
     * @param power      int - power
     * @param modulation int - modulator
     */
    int fastExponential(int base, int power, int modulation) {
        factorizationPower(power);
        storagePower(base, modulation);
        return combinationTogether(modulation);
    }

    /**
     * method check tester is a generator of z
     * inner can show generator all" bug a di "
     *
     * @param tester int - less than z
     * @param z      int - number represent generator set
     */
    void checkGenerator(int tester, int z) {
        if (tester < 1 || tester > z - 1) {
            log("Tester generator must be in range 0<tester<z ");
            return;
            // Go to error!
        }
        boolean[] tableGenerator = new boolean[z];
        boolean bomb = false;
        int valueGeneratorBeforeMod, valueGeneratorAfterMod, TesterPower1 = 0, TesterPowerBefore = 0;

        for (int i = 1; i < z; i++) {
            if (i == 1) {
                valueGeneratorBeforeMod = tester;
                valueGeneratorAfterMod = TesterPower1 = TesterPowerBefore = valueGeneratorBeforeMod % z;
            } else {
                valueGeneratorBeforeMod = TesterPowerBefore * TesterPower1;
                valueGeneratorAfterMod = valueGeneratorBeforeMod % z;
                TesterPowerBefore = valueGeneratorAfterMod;

            }

            if (!tableGenerator[valueGeneratorAfterMod]) {
                tableGenerator[valueGeneratorAfterMod] = true;
            } else {
                log(tester + " is NON-generator of Z" + z + "*");
                bomb = true;
                break;

            }
            log("checkGenerator[" + i + "] = " + valueGeneratorAfterMod);
            //System.out.println(tester + "^" + i + " mod " + z + " = " + valueGeneratorAfterMod);
        }

		/*log("---------------------------------");
		for (int r=0;r<z;r++) {
			log("tableGenerator[" + r + "] = " + tableGenerator[r]);
		}*/

        if (!bomb) {
            log(tester + " is Generator of Z" + z + "*");
        }

    } /* end function checkGenerator
		** Status: It's many bugs. << OveRfLoW!!! 
		*/

    private boolean checkGeneratorII(int tester, int mod) {
        if (tester < 0) return false;
        int result = fastfac(tester, (mod - 1) / 2, mod);
        boolean check = result == 1;
        //System.out.println(result); //track result
        return check;
    } // end function checkGeneratorII

    /*
      void hashFunction() {
        hashStorage = new int[6];
        int hashResultI = 0;
        for (int i=0;i<6;i++) {
            hashStorage[i] = scan.nextInt();
            hashResultI += (Math.pow(hashStorage[i],(i+1)))%23;
        }
        hashResultI = hashResultI % 23;
        log("hashResultI = " + hashResultI);
        hashResultI = circularShift2(hashResultI);
        log("hashResultI = " + hashResultI);
        hashResultI = hashResultI % 23;
        log("hashResultI = " + hashResultI);


    } // end function hashFunction (TEST BETA!!)

      int circularShift2(int number) {
        for (int i=0; i<2; i++) {
            if ((16 & number) == 16) {
                number = ((number - 16) << 1) + 1;
            }
            else {
                number = number << 1;
            }
        }
        return number;
    } // end function circularShift
    */
    public static void log(Object anyThings) {
        System.out.println(String.valueOf(anyThings));
    } // end function log

    //Ice part

    /**
     * fix index out of bound
     * of fast factorization
     * @param base base
     * @param power power
     * @param mod modulator
     * @return base^power
     */
    int fastfac(int base, int power, int mod) {
        if (power == 0) return 1;
        int count = power;
        long result = base;//prevent overflow
        while (count > 1) {
            result *= result;
            result %= mod;
            count = count >> 1;
        }
        return Math.toIntExact(result);
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
        if (i % 2 == 0) i++;
        log("lehmann testing and increasing when false ");
        //increse one if not prime
        while (!lehmanTest(i)) {
            i += 2;
        }
        log("got p");
        return i;
    }

    /**
     * @param plaintext a plaintext want to encrypt
     * @param sk        private key
     * @param generator a generator
     * @return
     */
    public int[][] encryption(byte[] plaintext, int sk, int generator, int bitCount) {

        //create key
        int p = generateP(bitCount);
        int count = 0;//count for tracking

        // gen k
        int k = -1;// -1 mean not init
        while (k < 1 || gcd(k, p - 1) != 1) {
            count++;
            k = (int) ((Math.random() * (p - 2)) + 2);
        }
        log("got k false " + count + " time");
        /*int bitCountP = 0; //no. of bit group
        while (i > 0) {//count how many bit can assign to 1 block
            i = i >> 1;
            bitCountP++;
        }*/
        log("gain p = " + p + " group of bit " + bitCount);
        //prevent generator out of set Z
        if (generator >= p || generator < 2) {
            log("not in set Z reset to 0");
            generator = 2;
        }
        count = 0;//reset count for track next
        while (!checkGeneratorII(generator, p)) {
            count++;
            generator++;
        }
        log("false " + count + " time");
        //create public key
        int y = fastfac(generator, sk, p);
        log("generator = " + generator +
                " k is " + k +
                " y is " + y);

        int sizeOfCiphertext = (plaintext.length * 8) / bitCount;
        int remindSize = (plaintext.length * 8) % bitCount;//check padding?
        int cryptogram[][] = new int[sizeOfCiphertext + 1][2];
        log("cipher text size is " + sizeOfCiphertext + " remind " + remindSize);
        //encryption
        int index = 0;//for point cryptogram
        int temp;//assign temp value
        boolean[] block = new boolean[bitCount];//create for exact bit(all value false)
        int a = fastfac(generator, k, p);
        //iterate all plaintext
        for (byte b : plaintext) {
            boolean[] bitGroup = Bit.toBit(b);
            for (boolean bit : bitGroup) {
                block[index] = bit;
                index++;
                if (index >= bitCount) {//when collect full block assign to crytogram
                    cryptogram[index][0] = a;
                    cryptogram[index][1] = fastfac(y, k, p) * Bit.fromBit(block);
                    Arrays.fill(block, false);//fill false for all bit
                    index = 0;
                }
            }
        }//complete crytogram with pading all
        return cryptogram;
    }


} // end class
