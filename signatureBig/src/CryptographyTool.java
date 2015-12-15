import java.math.BigInteger;
import java.util.*;

public class CryptographyTool {
    private static BigInteger[] setPower;
    private static long[] storagePower;
    private static BigInteger[] hashStorage;

    public static void main(String args[]){
        CryptographyTool tool = new CryptographyTool();
        BigInteger i;
        i = new BigInteger("17");
        System.out.println( tool.fastExpo(new BigInteger("2"), new BigInteger("4"), new BigInteger("1024")));
        //System.out.println(tool.lehmanTest(i,1024));

        do {
            i = new BigInteger(1024, new Random());
        }while (!i.isProbablePrime(1));
        System.out.println(i);
        System.out.println(i.isProbablePrime(1));

        boolean b = tool.lehmanTest(i,1024);
        System.out.println(b);
        //System.out.println(new BigInteger("3").modInverse(new BigInteger("10")));
    }


    /**
     * method to find gcd with two number
     *
     * @param firstNumber   BigInteger - a number to find gcd with next param
     * @param anotherNumber BigInteger - a number to find gcd with before param
     * @return a gcd in type BigInteger
     */
    BigInteger gcd(BigInteger firstNumber, BigInteger anotherNumber) {
        return firstNumber.gcd(anotherNumber);
    } // end funcrion gcd

    /**
     *
     * @param n a bigInt
     * @param p modulator
     * @return n^-1 mod p
     */
    BigInteger findInverse(BigInteger n,BigInteger p) {
        return n.modInverse(p);
    } // end function findInverse

    /**
     * to prove tester is prime Ice edition
     *
     * @param p a number to test prime.
     * @return true when a is prime (probability)
     */
    boolean lehmanTest(BigInteger p,int n) {
        if (p.getLowestSetBit()  != 0 && p.compareTo(new BigInteger("3"))!=1) {
            log("lehman test n only odd and n > 2");
            return false;
        }
        boolean isPrime = true;
        BigInteger power = p.subtract(BigInteger.ONE);
        power = power.shiftRight(1);//divide two.
        BigInteger result;
        int a;
        BigInteger temp;
        if (p.compareTo(new BigInteger("100")) == -1) {
            for (a = 3; a < p.intValueExact(); a++) {
                result = fastExpo(BigInteger.valueOf((long)a), power, p);
                isPrime = gcd(BigInteger.valueOf((long)a), p).compareTo(BigInteger.ONE) != 1;//<= 1
                isPrime &= (result.equals(p.subtract(BigInteger.ONE)) || result.equals(BigInteger.ONE));
                if (!isPrime) break; //break when not test failed.
            }
        } else {
            for (a = 0; a < 100; a++) {
                temp = new BigInteger(n,new Random()).mod(p);
                result = fastExpo(temp, power, p);
                isPrime = gcd(temp, p).compareTo(BigInteger.ONE) != 1;//<= 1
                isPrime &= (result.equals(p.subtract(BigInteger.ONE)) || result.equals(BigInteger.ONE));
                if (!isPrime) break;
            }
        }
        return isPrime;
    } // end function LehmanTest


    private boolean checkGeneratorII(BigInteger tester, BigInteger mod) {
        if (tester.compareTo(BigInteger.ZERO) == -1) return false;
        BigInteger result = fastExpo(tester, mod.subtract(BigInteger.ONE).shiftRight(1), mod);
        boolean check = !result.equals(BigInteger.ONE);
        //System.out.println(result); //track result
        return check;
    } // end function checkGeneratorII

    public static void log(Object anyThings) {
        System.out.println(String.valueOf(anyThings));
    } // end function log

    public BigInteger minusMod(BigInteger minus, BigInteger mod) {
        //BigInteger x, y, z;
        BigInteger a = mod.add(minus);
        return a;

    } // end function minusMod


    //Ice part

    /**
     * base must not exceed result.
     * ezy copy form net.
     *
     * @param base
     * @param power
     * @param mod
     * @return BigInteger base^power
     */
    BigInteger fastExpo(BigInteger base, BigInteger power, BigInteger mod) {
        BigInteger result = BigInteger.ONE;
        BigInteger precompu = base;
        while (power.compareTo(BigInteger.ZERO) > 0) {//power 0 do nothing
            if (((power.getLowestSetBit()) == 0)) { //index setBit == 0 is odd.
                result = result.multiply(precompu).mod(mod);
            }
            precompu = precompu.multiply(precompu).mod(mod);
            power = power.shiftRight(1);
        }
        return result.mod( mod);
    }

    /**
     * generate p from a file form default fileOrganize class
     *
     * @return BigInteger-generate form first bit of file
     */
    public BigInteger generateP(int bitCount) {
        FileOrganize file = new FileOrganize();//default
        byte[] content = file.read();
        BigInteger i = BigInteger.ZERO;
        int index = 0;
        byte pick = content[index];
        log("init bitGroupP");
        // initial bitGroup
        for (int j = 0; j < bitCount; j++) {
            i = i.shiftLeft(1);
            i = i.add(BigInteger.valueOf(pick & 0b1));
            pick >>= 1;
            if (j % 7 == 0) {
                index++;
                pick = content[index];
            }
        }
        log("shifting");
        //shift for 1 in LSB
        BigInteger mask = BigInteger.ONE.shiftLeft(bitCount - 1);
        while ((i.and(mask).equals(BigInteger.ZERO))) {
            if (pick == 0) {//pick the next byte
                index++;
                pick = content[index];
            }
            i = i.shiftLeft(1);
            i = i.and(mask.shiftLeft(1));//cut MSB
            i = i.add(BigInteger.valueOf(pick & 0b1));//add LSB
            pick >>= 1;
        }
        if (i.getLowestSetBit() != 0) i = i.add(BigInteger.ONE);//plus 1 to odd
        log("lehmann testing and increasing when false i before test = " + i);
        //increse one if not prime
        while (!(lehmanTest(i,bitCount) && lehmanTest(i.subtract(BigInteger.ONE).shiftRight(1),bitCount))) { //!1||!2 == !(1&&2)
            i = i.add(new BigInteger("2"));
        }//got p is safe Prime
        log("got p is\n"+i);
        return i;
    }


    /**
     * random faster than read p from file.
     * @param bitCount number of bit
     * @return a safe prime p.
     */
    public BigInteger optimizeGenP(int bitCount) {
        BigInteger p;
        log("randoming p");
        int f = 0;
        do{
            p = new BigInteger(bitCount,bitCount,new Random());
            f++;
        } while (!(lehmanTest(p,bitCount) && lehmanTest(p.subtract(BigInteger.ONE).shiftRight(1),bitCount))); //!1||!2 == !(1&&2)
        //got p is safe Prime
        log("fail " + f +
                "got p is\n"+p);
        return p;
    }




    /**
     * genkey write on file key.txt.
     * @param n number group of bit
     */
    public void genKey(int n) {
        BigInteger p = optimizeGenP(n);
        BigInteger generator = BigInteger.ZERO;//not init
        int count = 0;//reset count for track next
        while (!checkGeneratorII(generator, p) || generator.compareTo(BigInteger.valueOf(2)) == -1) {
            count++;
            generator = new BigInteger(n,new Random()).mod(p);
        }
        log("generator random false " + count + " time");
        //create private key
        BigInteger u = new BigInteger(n,new Random()).mod(p);

        //create public key
        BigInteger y = fastExpo(generator, u, p);

        String s = String.format("(p,g,y) is %d\n%d\n%d\n", p, generator, y);
        FileOrganize file = new FileOrganize("PublicKey.txt");
        file.write(s.getBytes());
        file = new FileOrganize("SecertKey.txt");
        file.write(("u is "+u).getBytes());
        log(s);
    }

    public BigInteger getK(BigInteger p,int n) {
        // gen k
        int count = 0;
        BigInteger k = BigInteger.ZERO;// -1 mean not init
        while (k.compareTo(BigInteger.ONE)== -1 || !gcd(k, p.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
            count++;
            k = new BigInteger(n,new Random()).mod(p);
        }
        log("got k false " + count + " time");
        return k;
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
    public Cryptogram encryption(byte[] plaintext, BigInteger generator, BigInteger y, BigInteger k, int bitCount, BigInteger p) {

        int sizeOfCiphertext = (plaintext.length * 8) / bitCount;//byte * 8 / n
        int remindSize = (plaintext.length * 8) % bitCount;//check padding?
        BigInteger[] b = new BigInteger[sizeOfCiphertext + 2];
        //BigInteger[] test = new BigInteger[sizeOfCiphertext + 2];
        log("cipher text size is " + sizeOfCiphertext + " remind " + remindSize);
        //encryption
        int index = 0;//for point cryptogram
        int bitPointer = 0;//pointer of bit
        boolean[] block = new boolean[bitCount];//create for exact bit(all value false)
        BigInteger a = fastExpo(generator, k, p);
        BigInteger bPre = fastExpo(y, k, p);
        BigInteger temp ;//to assigntemp value;
        //iterate all plaintext
        for (byte byt : plaintext) {
            boolean[] bitGroup = Bit.toBit(byt);
            for (boolean bit : bitGroup) {
                block[bitPointer] = bit;
                bitPointer++;
                if (bitPointer >= bitCount) {//when collect full block assign to crytogram
                    b[index] = bPre.multiply( Bit.bigIntFromBit(block)).mod(p);
                    Arrays.fill(block, false);//fill false for all bit
                    index++;
                    bitPointer = 0;
                }
            }
        }
        Bit.paintByte(block);
        log("");
        b[index] = bPre.multiply( Bit.bigIntFromBit(block)).mod(p);
        index++;
        //send no. of remindSize
        b[index] = bPre.multiply( BigInteger.valueOf(remindSize)).mod(p);
        System.out.println(a);
        Cryptogram cryptogram = new Cryptogram(a, b);
        //complete crytogram with padding all
        return cryptogram;
    }


    public byte[] decryption(Cryptogram cipherText, BigInteger p, BigInteger u, int n) {
        BigInteger a = cipherText.getA();
        BigInteger[] b = cipherText.getB();

        //decrypt reminder
        BigInteger last = b[cipherText.length() - 1];
        BigInteger c = fastExpo(a, u, p);//a^u
        c = findInverse(c, p);//(a^u)^-1
        BigInteger reminder = c.multiply(last).mod(p);

        //decrypt cipher text
        boolean[] block = new boolean[8];
        int plaintextSize = ((cipherText.length() - 1) * n / 8);
        byte[] plaintext = new byte[plaintextSize];
        int index = -1; // point bit in b. n+1 for fetch value
        int pointer = 0;//point bit in block.
        BigInteger temp = BigInteger.ZERO;//temp value
        int i = 0;//index of ciphertext;
        int byteIndex = 0; // index of plaintext
        do {
            if (index < 0) {
                //decrypt to plain assign in temp
                temp = b[i];
                c = fastExpo(a, u, p);
                c = findInverse(c, p);
                temp = c.multiply(temp).mod(p);
                index = n - 1;
                i++;
            }
            if (pointer >= 8) {
                plaintext[byteIndex] = Bit.byteFromBit(block);
                pointer = 0;
                byteIndex++;
            }
            block[pointer] = temp.testBit(index);
            pointer++;
            index--;
        } while (i < b.length);//b last(rem) = length-1, b last(not full) = length-2 assign and left next.
        // look for one data
        log("");
        Bit.paintByte(Bit.toBit(plaintext[0]));
        return plaintext;
    }

    //int part
    int findInverseInt(double firstNumber, double anotherNumber) {
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

    int gcd(int firstNumber, int anotherNumber) {
        if (anotherNumber == 0) {
            return firstNumber;
        } else if (firstNumber == 0) {
            return anotherNumber;
        } else {
            return gcd(anotherNumber, firstNumber % anotherNumber);
        }
    } // end funcrion gcd

    public int minusModInt(int minus, int mod) {
        //int x, y, z;
        int a = Math.abs(minus), b = mod;
        return (b * ((a / b) + 1)) - a;

    } // end function minusMod

    int fastExpoInt(int base, int power, int mod) {
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

    public int getKint(int p) {
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



} // end class
