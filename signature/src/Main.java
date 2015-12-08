import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * PACKAGE_NAME
 * Last modified by Admin on 11/28/2015.
 * 11:09 PM
 */
public class Main {
    public static void main(String args[]) {
        long startTime, endTime;
        String inputFile = "p.txt";
        FileOrganize file = new FileOrganize(inputFile);
        byte[] plaintext = file.read();
        Scanner scan = new Scanner(System.in);
        Cryptogram ciphertxt = null;
        CryptographyTool tool = new CryptographyTool();
        SignatureHandler toolSign = new SignatureHandler();
        boolean finish = true;
        int g = 0, u = 0, n = 0, hashSize = 0, messageDigest = 0, y = 0;
        int p = 0, k = 0;
        try {
            while (n == 0) {
                System.out.println("size of message block");
                n = scan.nextInt();
            }
        } catch (InputMismatchException e) {
            scan.nextLine();
            System.err.println(e);
        }
        while (finish) {
            System.out.println("Please choose menu:\n" +
                    "1.Create key\n" +
                    "2.Elgamal Encryption [an int]\n" +
                    "3.Elgamal Decryption [an int]\n" +
                    "5.Create Signature\n" +
                    "6.Verify\n" +
                    "7.Elgamal Encryption\n" +
                    "8.Elgamal Decryption\n" +
                    "9.change number of n\n" +
                    "0.For Exit Program");

            System.out.print(">> ");
            int choose;
            try {
                choose = scan.nextInt();
                String result = "";
                startTime = System.currentTimeMillis();
                switch (choose) {
                    case 1:
                        startTime = System.currentTimeMillis();
                        tool.genKey(n);
                        break;
                    case 2:
                        System.out.println("insert a int to encrytion");
                        int msg = scan.nextInt();
                        System.out.println("(p,g,y) is (Insert form 'p' 'g' 'y')");
                        p = scan.nextInt();
                        g = scan.nextInt();
                        y = scan.nextInt();
                        scan.nextLine();
                        //startTime = System.currentTimeMillis();
                        k = tool.getK(p);
                        ciphertxt = tool.intEncryption(msg, g, y, k, p);
                        System.out.println("cipher text is " + ciphertxt);
                    case 3:
                        System.out.println("insert private key");
                        u = scan.nextInt();
                        int j = tool.intDecryption(ciphertxt, n, u, p);
                        System.out.println("decrypted got " + j);
                        break;
                    case 5://hash+sign
                        System.out.println("size of Hash block");
                        hashSize = scan.nextInt();
                        System.out.println("(p,g,y) is (Insert form 'p' 'g')");
                        p = scan.nextInt();
                        g = scan.nextInt();
                        scan.nextLine();
                        startTime = System.currentTimeMillis();
                        k = tool.getK(p);
                        messageDigest = toolSign.hash(file.CIPHER_FILE, hashSize, p);
                        System.out.println("Message Di = " + messageDigest);
                        toolSign.sign(g, k, p, u, messageDigest, file.SIGNATURE_FILE);
                        break;
                    case 6://hash+sign
                        System.out.println("size of Hash block");
                        hashSize = scan.nextInt();
                        System.out.println("(p,g,y) is (Insert form 'p' 'g' 'y')");
                        p = scan.nextInt();
                        g = scan.nextInt();
                        y = scan.nextInt();
                        scan.nextLine();
                        startTime = System.currentTimeMillis();
                        messageDigest = toolSign.hash(file.CIPHER_FILE, hashSize, p);//hash
                        System.out.println("Message Di = " + messageDigest);
                        toolSign.verify(g, messageDigest, y, p, file.SIGNATURE_FILE);//verify
                        break;
                    case 7:
                        System.out.println("(p,g,y) is (Insert form 'p' 'g' 'y')");
                        p = scan.nextInt();
                        g = scan.nextInt();
                        y = scan.nextInt();
                        scan.nextLine();
                        startTime = System.currentTimeMillis();
                        k = tool.getK(p);
                        ciphertxt = tool.encryption(plaintext, g, y, k, n, p);
                        //print last 16 bit.
                        System.out.println("last plain text 16 bit");
                        System.out.println(Integer.toBinaryString(plaintext[plaintext.length - 2]));
                        System.out.println(Integer.toBinaryString(plaintext[plaintext.length - 1]));
                        file.writeCipherTxt(ciphertxt);
                        byte decrypted[] = tool.decryption(ciphertxt, p, u, n);
                        int pass = 0;
                        for (int i = 0; i < plaintext.length; i++) {
                            if (plaintext[i] == decrypted[i]) pass++;
                        }
                        System.out.println("diff of size plaintext " + (decrypted.length - plaintext.length));
                        System.out.println("correct " + pass + " of " + plaintext.length);
                        file.write(decrypted);
                        break;
                    case 9:
                        System.out.println("Insert size of message block");
                        n = scan.nextInt();
                        break;
                    case 0:
                        finish = false;
                        result = "ขอบคุณที่ใช้บริการค่ะ";
                        break;
                    default:
                        System.out.println("input error");
                }
                endTime = System.currentTimeMillis();
                String time = String.format("time to use %d m %2d.%d s ",
                        (int) (((endTime - startTime) / (1000 * 60)) % 60),
                        (int) (((endTime - startTime) / 1000) % 60),
                        (int) ((endTime - startTime) % 1000)
                );
                System.out.println(time);
                System.out.println(result);
            } catch (Exception e) {
                System.err.print(e + "\n");
                scan.nextLine();//flush data scan
            }
        }

    }
}
