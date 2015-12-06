import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * PACKAGE_NAME
 * Last modified by Admin on 11/28/2015.
 * 11:09 PM
 */
public class Main {
    public static void main(String args[]){
        long startTime, endTime;
        FileOrganize file = new FileOrganize();
        byte[] plaintext = file.read();
        Scanner scan = new Scanner(System.in);
        CryptographyTool tool = new CryptographyTool();
        boolean finish = true;
        while (finish) {
            System.out.println("Please choose menu:\n"+
                    "1.Fast Exponential\n" +
                    "2.Find Inverse\n" +
                    "3.Check Generator\n" +
                    "4.Hash Function (Test Beta)\n" +
                    "5.Elgamal Encryption\n" +
                    "0.For Exit Program");
            int a, b, c;
            System.out.print(">> ");
            int choose;
            try {
                choose = scan.nextInt();
            } catch (Exception e) {
                System.err.print(e + "\n");
                scan.nextLine();//flush data scan
                continue;
            }
            String result = "";
            startTime = System.currentTimeMillis();
            switch (choose) {
                case 1:
                    System.out.print("Base: ");
                    a = scan.nextInt();
                    System.out.print("Power: ");
                    b = scan.nextInt();
                    System.out.print("Modulo: ");
                    c = scan.nextInt();
                    tool.fastExponential(a, b, c);
                    break;

                case 2:
                    System.out.print("Inverse of ");
                    a = scan.nextInt();
                    System.out.print("Mod ");
                    b = scan.nextInt();
                    tool.findInverse(a, b);
                    break;

                case 3:
                    System.out.print("Check generator of ");
                    a = scan.nextInt();
                    System.out.print("Mod ");
                    b = scan.nextInt();
                    tool.checkGenerator(a, b);
                    break;
                case 4:
                    //tool.hashFunction();
                    tool.lehmanTest(19);
                    System.out.println("in progress dev");
                    break;
                case 5:
                    int[][] ciphertxt = tool.encryption(plaintext, 99, 23);
                    System.out.println(ciphertxt[ciphertxt.length - 1][0]);
                    System.out.println(ciphertxt[ciphertxt.length - 1][1]);
                    System.out.println(Integer.toBinaryString(plaintext[plaintext.length - 2]));
                    System.out.println(Integer.toBinaryString(plaintext[plaintext.length - 1]));
                    int p = scan.nextInt();
                    tool.decryption(ciphertxt, p, 99);
                    file.writeCipherTxt(ciphertxt);
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
