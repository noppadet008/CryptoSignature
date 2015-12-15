import java.io.*;
import java.util.Scanner;

public class SignatureHandler {


    /**
     * 5. hash function zone.
     * - String filename << file name of ciphertext;
     * - int numberOfBlock << number of block, from user
     * - int p << prime number, from generateP()
     */
    public int hash(String filename, int numberOfBlock, int p) {
        String ciphertext;
        ciphertext = readCipher(filename);
        ciphertext = pad(ciphertext, numberOfBlock);
        return polyHash(ciphertext, numberOfBlock, p);
    } // end function hash


    public String readCipher(String filename) {
        String ciphertext = "";
        try {
            BufferedReader rfi = new BufferedReader(new FileReader(filename));
            int d = 0;
            String connect = "";
            while (d != -1) {
                d = rfi.read();
                connect = String.valueOf(d);
                ciphertext = ciphertext + connect;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        ciphertext = ciphertext.substring(0, ciphertext.length() - 2);
        return ciphertext;

    } // end function readCipher

    public String pad(String ciphertext, int numberOfBlock) {
        if (ciphertext.length() % numberOfBlock == 0) {
            return ciphertext;
        }
        int numberPad = numberOfBlock - (ciphertext.length() % numberOfBlock);
        //System.out.println("numberPad = " + numberPad);
        char pad = '0';
        for (int i = 0; i < numberPad; i++) {
            ciphertext = pad + ciphertext;
        }
        return ciphertext;
    } // end function pad

    public int polyHash(String ciphertext, int numberOfBlock, int p) {
        int start = 0, initial, buffer = 0;
        int[] aBox = new int[numberOfBlock];

        while (start < ciphertext.length()) {

            for (int i = 0; i < numberOfBlock; i++) {
                /*	Substring in CIPHERTEXT each character
					minus 48 because 48 is ASCII code of zero (0) and CIPHERTEXT has only number
				*/
                aBox[i] = (int) ciphertext.charAt(start + i) - 48;
                //System.out.print(aBox[i]);
            }

            // if in first round, use IV (initial vector) is length of box
            if (start == 0) {
                initial = aBox.length;
            }

            // else it is a buffer which is result in round-1
            else {
                initial = buffer;
            }

            buffer = roundHash(aBox, initial, numberOfBlock, p);
            //System.out.println("\nResult: " + buffer);

            //System.out.println();
            start += numberOfBlock;
        }

        return buffer;

    } // end function polyHash

    public int roundHash(int[] oneBox, int iv, int numberOfBlock, int p) {
        int sum = 0, result;
        sum += iv;
        for (int i = 0; i < numberOfBlock; i++) {
            sum += Math.pow(oneBox[i], i + 2);
        }
        result = sum % p;
        result = circularShift2(result);
        result = result % p;

        return result;

    } // end function RoundHash

    public int circularShift2(int number) {
        for (int i = 0; i < 2; i++) {
            if ((16 & number) == 16) {
                number = ((number - 16) << 1) + 1;
            } else {
                number = number << 1;
            }
        }
        return number;
    } // end function circularShift


    /**
     * 3. signature zone
     * - int g << generator SENDER
     * - int k << k SENDER
     * - int p << from generateP()
     * - int u << security key (private key) SENDER
     * - int x << message digest (result from hash function)
     * - String fileCipher << filename (txt) of fileSignature
     */

    public void sign(int g, int k, int p, int u, int x, String fileSignature) {
        CryptographyTool tool = new CryptographyTool();
        int r = tool.fastExpoInt(g, k, p);

        int check = x - u * r;
        int s;

        if (check < 0) s = tool.minusModInt(check, p - 1);
        else
            s = check % (p - 1);
        s = (tool.findInverseInt(k, p - 1)) * s;
        s %= (p - 1);

        writeSignature(fileSignature, r, s);

    } // end function sign

    public void writeSignature(String filename, int r, int s) {
        File file = new File(filename);
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, false));
            buf.append(r + " " + s);
            buf.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    } // end function signatureAppend
	
	/*	4. verify zone
			- int g << generator SENDER
			- int x << message digest
			- int y << public key SENDER
			- int p << from generateP()
			- String filename << filename that read from fileSignature
	
	*/

    public void verify(int g, int x, int y, int p, String filename) {
        int r, s;

        r = readSignature(filename, 0);
        s = readSignature(filename, 1);

        CryptographyTool tool = new CryptographyTool();

        if (tool.fastExpoInt(g, x, p) == (tool.fastExpoInt(y, r, p) * tool.fastExpoInt(r, s, p)) % p) {
            System.out.println("Verify Confirm!!");
            return;
        } else {
            System.out.println("Verify Failed!!");
            return;
        }
    } // end function verify

    public int readSignature(String filename, int i) {
        int[] signa = new int[2];
        try {
            Scanner readfile = new Scanner(new FileInputStream(filename));
            signa[0] = readfile.nextInt();
            signa[1] = readfile.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return signa[i];
    } // end function readSignature



} // end class SignatureHandler
