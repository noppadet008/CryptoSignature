import java.util.Scanner;

/**
 * PACKAGE_NAME
 * Last modified by Admin on 11/28/2015.
 * 11:09 PM
 */
public class Main {
    public static void main(String args[]){
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
                    "0.For Exit Program");
            int a, b, c;
            System.out.print(">> ");
            int choose = scan.nextInt();
            String result = "";
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
                    tool.hashFunction();
                    break;
                case 0:
                    finish = false;
                    result = "ขอบคุณที่ใช้บริการค่ะ";
                    break;
            }
            System.out.println(result);
        }

    }
}
