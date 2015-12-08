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
        Cryptogram ciphertxt;
        CryptographyTool tool = new CryptographyTool();
        SignatureHandler toolSign = new SignatureHandler();
        boolean finish = true;
        int g = 0, u = 2, n, hashSize = 0, messageDigest = 0, y = 0;
        int p = 0, k = 0;
        while (finish) {
            System.out.println("Please choose menu:\n" +
                    "1.Create key\n" +
                    "5.Create Signature\n" +
                    "6.Verify\n" +
                    "7.Elgamal Encryption\n" +
                    "8/Elgamal Decryption\n" +
                    "0.For Exit Program");

            System.out.print(">> ");
            int choose;
            try {
                choose = scan.nextInt();
                System.out.println("size of message block");
                n = scan.nextInt();
                String result = "";
                switch (choose) {
                    case 1:
                        startTime = System.currentTimeMillis();
                        tool.genKey(n);
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
                        ciphertxt = tool.encryption(plaintext, g, y, k, u, n, p);
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
                    case 0:
                        startTime = System.currentTimeMillis();
                        finish = false;
                        result = "ขอบคุณที่ใช้บริการค่ะ";
                        break;
                    default:
                        startTime = System.currentTimeMillis();
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
