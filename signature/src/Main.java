import java.util.Scanner;

/**
 * PACKAGE_NAME
 * Last modified by Admin on 11/28/2015.
 * 11:09 PM
 */
public class Main {
    public static void main(String args[]){
        long startTime, endTime;
        String inputFile = "p.txt";
        FileOrganize file = new FileOrganize(inputFile);
        byte[] plaintext = file.read();
        Scanner scan = new Scanner(System.in);
        Cryptogram ciphertxt;
        CryptographyTool tool = new CryptographyTool();
        SignatureHandler toolSign = new SignatureHandler();
        int[] key;
        boolean finish = true;
        int g = 0, u = 2, n, hashSize = 0, messageDigest = 0, y = 0;
        int p = 0, k = 0;
        while (finish) {
            System.out.println("Please choose menu:\n"+
                    "4.Hash Function (Test Beta)\n" +
                    "5.Create Signature\n" +
                    "6.Verify\n" +
                    "7.Elgamal Encryption\n" +
                    "0.For Exit Program");

            System.out.print(">> ");
            int choose;
            try {
                choose = scan.nextInt();
                System.out.println("size of message block");
                n = scan.nextInt();
                System.out.println("size of Hash block");
                hashSize = scan.nextInt();
                System.out.println("file name");
                scan.nextLine();
                System.out.println("Enter your private key");
                scan.nextInt();
            } catch (Exception e) {
                System.err.print(e + "\n");
                scan.nextLine();//flush data scan
                continue;
            }
            String result = "";
            if (p == 0) {
                p = tool.generateP(n);
                k = tool.getK(p);
            }
            startTime = System.currentTimeMillis();
            switch (choose) {
                case 4:
                    messageDigest = toolSign.hash(file.CIPHER_FILE, hashSize, p);
                    break;
                case 5:
                    System.out.println("Message Di = " + messageDigest);
                    toolSign.sign(g, k, p, u, messageDigest, file.SIGNATURE_FILE);
                    break;
                case 6:
                    System.out.println("");
                    toolSign.verify(g, messageDigest, y, p, file.SIGNATURE_FILE);
                    break;
                case 7:
                    key = tool.genKey(u, k, p);
                    g = key[1];
                    y = key[2];
                    ciphertxt = tool.encryption(plaintext, g, y, k, u, n);
                    System.out.println(ciphertxt);
                    System.out.println(ciphertxt.getA());
                    System.out.println(ciphertxt.getB()[ciphertxt.length() - 2]);
                    System.out.println(Integer.toBinaryString(plaintext[plaintext.length - 2]));
                    System.out.println(Integer.toBinaryString(plaintext[plaintext.length - 1]));
                    file.writeCipherTxt(ciphertxt);
                    byte decrypted[] = tool.decryption(ciphertxt, p, u, n);
                    int pass = 0;
                    for (int i = 0; i < plaintext.length; i++) {
                        if (plaintext[i] == decrypted[i]) pass++;
                    }
                    System.out.println("diff of size plaintext " + (plaintext.length - decrypted.length));
                    System.out.println("correct " + pass + " of " + plaintext.length);
                    file.write(decrypted);


                    break;
                case 0:
                    finish = false;
                    result = "ขอบคุณที่ใช้บริการค่ะ";
                    break;
            }
            endTime = System.currentTimeMillis();
            String time = String.format("time to use %d m %2d.%d s ",
                    (int) (((endTime - startTime) / (1000 * 60)) % 60),
                    (int) (((endTime - startTime) / 1000) % 60),
                    (int) ((endTime - startTime) % 1000)
            );
            System.out.println(time);
            System.out.println(result);
        }

    }
}
