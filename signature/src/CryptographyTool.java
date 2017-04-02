import java.util.*;

public class CryptographyTool {
    private static int[] setPower;
    private static long[] storagePower;
    private static int[] hashStorage;

    public static void main(String[] arg) {
        CryptographyTool tool = new CryptographyTool();
        log(tool.fastExpo(2,4,1024));
        log(tool.findInverse(19,20));
    }

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
    int findInverse(double firstNumber, double anotherNumber) {
        if (gcd((int) firstNumber, (int) anotherNumber) != 1) {
            log("Can't find inverse because they're not relative prime.");
            return 0;
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
        /*
        log("n1 n2 r q a1 a2 b1 b2\n----------------------------");
        log(n1 + " " + n2 + " "
                + r + " " + q + " " + a1 + " " + a2 + " " + b1 + " "
                + b2);
        *///comment for optimize
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
            /*
            log(n1 + " " + n2 + " "
                    + r + " " + q + " " + a1 + " " + a2 + " " + b1 + " "
                    + b2);
             *///comment for optimize
        }

		/*log(n1 + " " + n2 + " " 
            + r + " " + q + " " + a1 + " " + a2 + " " + b1 + " "
			+ b2);

        log("");*/
        if (firstNumber == max) {
            //return a2;
            if (a2 < 0) {
                //log("Inverse of " + (int) firstNumber + " mod " +
                //      (int) anotherNumber + " = " + a2 + " = " + ((int) (a2 - anotherNumber * Math.floor(a2 / anotherNumber))));
                return (int) (a2 - anotherNumber * Math.floor(a2 / anotherNumber));
            } else
                //log("Inverse of " + (int) firstNumber + " mod " + (int) anotherNumber + " = " + a2);
                return a2;
        } else {
            if (b2 < 0) {
                //log("Inverse of " + (int) firstNumber + " mod " +
                //      (int) anotherNumber + " = " + b2 + " = " + ((int) (b2 - anotherNumber * Math.floor(b2 / anotherNumber))));
                return (int) (b2 - anotherNumber * Math.floor(b2 / anotherNumber));
            } else
                //log("Inverse of " + (int) firstNumber + " mod " + (int) anotherNumber + " = " + b2);
                return b2;
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
    boolean checkGenerator(int tester, int z) {
        if (tester < 1 || tester > z - 1) {
            log("Tester generator must be in range 0<tester<z ");
            return false;
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
                //          log(tester + " is NON-generator of Z" + z + "*");
                bomb = true;
                return false;

            }
            //      log("checkGenerator[" + i + "] = " + valueGeneratorAfterMod);
            //System.out.println(tester + "^" + i + " mod " + z + " = " + valueGeneratorAfterMod);
        }

		/*log("---------------------------------");
        for (int r=0;r<z;r++) {
			log("tableGenerator[" + r + "] = " + tableGenerator[r]);
		}*/

        if (!bomb) {
            // log(tester + " is Generator of Z" + z + "*");
            return true;
        } else {
            return false;
        }

    } /* end function checkGenerator
        ** Status: It's many bugs. << OveRfLoW!!!
		*/

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

    public int minusMod(int minus, int mod) {
        //int x, y, z;
        int a = Math.abs(minus), b = mod;

		/*x = a/b;
        System.out.println("x = " + x);
		y = b*(x+1);
		System.out.println("y = " + y);
		z = y-a;
		System.out.println("z = " + z);*/
        return (b * ((a / b) + 1)) - a;

    } // end function minusMod


    //Ice part

    /**
     * fix index out of bound
     * of fast power in binary base
     *
     * @param base  base
     * @param power power
     * @param mod   modulator
     * @return base^power
     */
    int fastfac(int base, int power, int mod) {
        if (power == 0) return 1;
        int count = power;
        long preOomputation = 1;
        long result = preOomputation;//prevent overflow
        while (count > 0) {
            preOomputation *= base;
            preOomputation %= mod;
            if ((count & 0b1) == 1) {
                result *= preOomputation;
                result %= mod;
            }
            count = count >> 1;
        }
        return Math.toIntExact(result);
    }


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

    public int getK(int p) {
        // gen k
        int count = 0;
        int k = -1;// -1 mean not init
        while (k < 1 || gcd(k, p - 1) != 1) {
            count++;
            k = (int) ((Math.random() * (p)));
        }
        log("got k false " + count + " time");
        return k;
    }


    /**
     * for test encrypt by a int.
     *
     * @param plaintext
     * @param generator
     * @param y
     * @param k
     * @param p
     * @return
     */
    public Cryptogram intEncryption(int plaintext, int generator, int y, int k, int p) {
        int a = fastExpo(generator, k, p);
        int b = Math.toIntExact((long) plaintext * fastExpo(y, k, p));
        int listB[] = {b};
        return new Cryptogram(a, listB);
    }

    public int intDecryption(Cryptogram cryptogram, int n, int u, int p) {
        int a = cryptogram.getA();
        int b = cryptogram.getB()[0];
        int temp = fastExpo(a, u, p);
        temp = findInverse(temp, p);
        int plaintext = Math.toIntExact((long) temp * b % p);
        return plaintext;
    }


    /**
     *
     * @param plaintext
     * @param generator
     * @param y
     * @param k
     * @param bitCount
     * @param p
     * @return
     */
    public Cryptogram encryption(byte[] plaintext, int generator, int y, int k, int bitCount, int p) {

        int sizeOfCiphertext = (plaintext.length * 8) / bitCount;//byte * 8 / n
        int remindSize = (plaintext.length * 8) % bitCount;//check padding?
        int[] b = new int[sizeOfCiphertext + 2];
        int[] test = new int[sizeOfCiphertext + 2];
        log("cipher text size is " + sizeOfCiphertext + " remind " + remindSize);
        //encryption
        int index = 0;//for point cryptogram
        int bitPointer = 0;//pointer of bit
        boolean[] block = new boolean[bitCount];//create for exact bit(all value false)
        int a = fastExpo(generator, k, p);
        int bPre = fastExpo(y, k, p);
        long cb;//to assigntemp value;
        //iterate all plaintext
        for (byte byt : plaintext) {
            boolean[] bitGroup = Bit.toBit(byt);
            for (boolean bit : bitGroup) {
                block[bitPointer] = bit;
                bitPointer++;
                if (bitPointer >= bitCount) {//when collect full block assign to crytogram
                    test[index] = Bit.fromBit(block);
                    cb = ((long) bPre * Bit.fromBit(block)) % p;
                    b[index] = Math.toIntExact(cb);
                    Arrays.fill(block, false);//fill false for all bit
                    index++;
                    bitPointer = 0;
                }
            }
        }
        Bit.paintByte(block);
        log("");
        cb = ((long) Bit.fromBit(block) * bPre) % p;
        b[index] = Math.toIntExact(cb);
        index++;
        //send no. of remindSize
        cb = ((long) remindSize * bPre) % p;
        b[index] = Math.toIntExact(cb);
        System.out.println(a);
        Cryptogram cryptogram = new Cryptogram(a, b);
        //complete crytogram with padding all
        return cryptogram;
    }


    public byte[] decryption(Cryptogram cipherText, int p, int u, int n) {
        int a = cipherText.getA();
        int[] b = cipherText.getB();

        //decrypt reminder
        int last = b[cipherText.length() - 1];
        int c = fastExpo(a, u, p);//a^u
        c = findInverse(c, p);//(a^u)^-1
        long reminder = ((long) c * last);
        reminder %= p;
        int remind = Math.toIntExact(reminder);

        //decrypt cipher text
        boolean[] block = new boolean[8];
        int plaintextSize = ((cipherText.length() - 1) * n / 8);
        byte[] plaintext = new byte[plaintextSize];
        int index = -1; // point bit in b. n+1 for fetch value
        int pointer = 0;//point bit in block.
        int temp = 0;//temp value
        int i = 0;//index of ciphertext;
        int byteIndex = 0; // index of plaintext
        do {
            if (index < 0) {
                //decrypt to plain assign in temp
                temp = b[i];
                c = fastExpo(a, u, p);
                c = findInverse(c, p);
                temp = Math.toIntExact(((long) c * temp) % p);
                index = n - 1;
                i++;
            }
            if (pointer >= 8) {
                plaintext[byteIndex] = Bit.byteFromBit(block);
                pointer = 0;
                byteIndex++;
            }
            block[pointer] = (temp & (0b1 << index)) != 0;
            pointer++;
            index--;
        } while (i < b.length);//b last(rem) = length-1, b last(not full) = length-2 assign and left next.
        // look for one data
        log("");
        Bit.paintByte(Bit.toBit(plaintext[0]));
        return plaintext;
    }

    int[] byteToInt(byte[] text, int divider) {
        int sizeOfCiphertext = text.length * 8 / divider;
        int rem = text.length * 8 % divider;
        int test[] = new int[sizeOfCiphertext + 2];
        int bitPointer = 0;
        boolean[] block = new boolean[divider];
        int index = 0; // point bit in b. n+1 for fetch value

        for (byte byt : text) {
            boolean[] bitGroup = Bit.toBit(byt);
            for (boolean bit : bitGroup) {
                block[bitPointer] = bit;
                bitPointer++;
                if (bitPointer >= divider) {//when collect full block assign to crytogram
                    test[index] = Bit.fromBit(block);
                    Arrays.fill(block, false);//fill false for all bit
                    index++;
                    bitPointer = 0;
                }
            }
        }
        test[index] = Bit.fromBit(block);
        index++;
        test[index] = rem;
        return test;
    }

    byte[] intToByte(int divider, int[] text) {
        //decrypt cipher text
        boolean[] block = new boolean[8];
        int plaintextSize = ((text.length - 1) * divider / 8);
        byte[] plaintext = new byte[plaintextSize];
        int index = -1; // point bit in b. -1 for fetch value
        int pointer = 0;//point bit in block. point from last to frist .
        int temp = 0;//temp value
        int i = 0;//index of ciphertext;
        int byteIndex = 0; // index of plaintext
        do {
            if (index < 0) {
                //decrypt to plain assign in temp
                temp = text[i];
                index = divider - 1;
                i++;
            }
            if (pointer >= 8) {
                plaintext[byteIndex] = Bit.byteFromBit(block);
                pointer = 0;
                byteIndex++;
            }
            //read form frist bit to last bit.
            block[pointer] = (temp & (0b1 << index)) != 0;
            pointer++;
            index--;
        } while (i < text.length);//cut last bit already.
        return plaintext;
    }


} // end class
