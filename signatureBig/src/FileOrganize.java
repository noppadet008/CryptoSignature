import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Converting binary data into different forms.
 * <p>
 * <P>Reads binary data into memory, and writes it back out.
 * (If your're actually copying a file, there are better ways to do this.)
 * <p>
 * <P>Buffering is used when reading and writing files, to minimize the number
 * of interactions with the disk.
 */
public class FileOrganize {

    private String INPUT_FILE_NAME = "input\\p.pdf";
    private String OUTPUT_FILE_NAME = "output\\output.pdf";
    public String CIPHER_FILE = "output\\cipher.txt";
    public String SIGNATURE_FILE = "output\\sign.txt";


    public FileOrganize() {
    }

    /**
     * Run the example.
     */
    public static void main(String... args) {

        // Change these settings before running this class.


        FileOrganize test = new FileOrganize("p.txt");
        //read in the bytes
        long start = System.currentTimeMillis();
        byte[] fileContents = test.read();
        test.writeText(fileContents);
        fileContents = test.readText();
        //test.readAlternateImpl(INPUT_FILE_NAME);
        //write it back out to a different file name
        test.write(fileContents);
        long end = System.currentTimeMillis();
        log((end - start) / 1000.0);
    }

    /**
     *
     * @param inputFile path of file to read
     */
    public FileOrganize(String inputFile){
        INPUT_FILE_NAME = "input\\"+inputFile;
        String[] str = inputFile.split("\\.");
        OUTPUT_FILE_NAME = "output\\"+str[0]+"OP."+str[1];
    }
    public String getINPUT_FILE_NAME() {
        return INPUT_FILE_NAME;
    }

    public String getOUTPUT_FILE_NAME() {
        return OUTPUT_FILE_NAME;
    }

    /**
     * Read the given binary file, and return its contents as a byte array.
     */
    byte[] read() {
        return readfile(INPUT_FILE_NAME);
    }

    byte[] readText(){
        return readfile("output\\cipher.txt");
    }


    byte[] readfile(String aInputFileName) {
        log("Reading in binary file named : " + aInputFileName);
        File file = new File(aInputFileName);
        log("File size: " + file.length());
        byte[] result = new byte[(int) file.length()];
        try {
            try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
                int totalBytesRead = 0;
                while (totalBytesRead < result.length) {
                    int bytesRemaining = result.length - totalBytesRead;
                    //input.read() returns -1, 0, or more :
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                    log("bytesRead = " + bytesRead);
                    if (bytesRead > 0) {
                        totalBytesRead = totalBytesRead + bytesRead;
                    } // end if
                } // end while
                /*
                 the above style is a bit tricky: it places bytes into the 'result' array;
                 'result' is an output parameter;
                 the while loop usually has a single iteration only.
                */
                log("Num bytes read: " + totalBytesRead);
            } finally {
                log("Closing input stream.");
                /*for (int i=0;i<result.length;i++) {
                    log("result[" + i+1 + "] = " + result[i]);
                }*/
                log("Size: " + result.length);
                log("Closing input stream.\n*****************************************");

            }
        } catch (FileNotFoundException ex) {
            log("File not found. pls check path of file.");
        } catch (IOException ex) {
            log(ex);
        }
        return result;
    }

    /**
     * Write a byte array to the given file.
     * Writing binary data is significantly simpler than reading it.
     */
    void write(byte[] aInput ) {
        log("Writing binary file...");
        String aOutputFileName = OUTPUT_FILE_NAME;
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(aOutputFileName))) {
            output.write(aInput);
        } catch (FileNotFoundException ex) {
            log("File not found.");
        } catch (IOException ex) {
            log(ex);
        }
    }

    void writeText(byte[] aInput) {//test version
        log("Writing binary file...");
        String aOutputFileName = "output\\cipher.txt";
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(aOutputFileName))) {
            output.write(aInput);
        } catch (FileNotFoundException ex) {
            log("File not found.");
        } catch (IOException ex) {
            log(ex);
        }
    }

    void writeCipherTxt(Cryptogram cipherTxt) {
        log("Writing cipher file...");
        String aOutputFileName = CIPHER_FILE;
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(aOutputFileName))) {
            output.write((cipherTxt.getA() + " ").getBytes("UTF-8"));
            BigInteger[] b = cipherTxt.getB();
            for (BigInteger v : b) {
                output.write((v + " ").getBytes());
            }
        } catch (FileNotFoundException ex) {
            log("File not found.");
        } catch (IOException ex) {
            log(ex);
        }
        log("complete");
    }

    Cryptogram readCipherTxt() {
        log("reading cipher text file...");
        Scanner sc;
        try {
            sc = this.getFileReader(CIPHER_FILE);
            BigInteger a = new BigInteger( sc.next());
            BigInteger b[];
            ArrayList<BigInteger> temp = new ArrayList<>();
            while (sc.hasNext()) {
                temp.add(new BigInteger( sc.next()));
            }
            b = new BigInteger[temp.size()];
            for (int t = 0; t < temp.size(); t++) {
                b[t] = temp.get(t);
            }
            return new Cryptogram(a, b);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (InputMismatchException e) {
            System.out.println("not cipher text file");
        }
        return null;
    }
    /**
     * Read the given binary file, and return its contents as a byte array.
     */
    byte[] readAlternateImpl(String aInputFileName) {
        log("Reading in binary file named : " + aInputFileName);
        File file = new File(aInputFileName);
        log("File size: " + file.length());
        byte[] result = null;
        try {
            InputStream input = new BufferedInputStream(new FileInputStream(file));
            result = readAndClose(input);
        } catch (FileNotFoundException ex) {
            log(ex);
        }
        return result;
    }


    /**
     * Read an input stream, and return it as a byte array.
     * Sometimes the source of bytes is an input stream instead of a file.
     * This implementation closes aInput after it's read.
     */
    byte[] readAndClose(InputStream aInput) {
        //carries the data from input to output :
        byte[] bucket = new byte[32 * 1024];
        ByteArrayOutputStream result = null;
        try {
            try {
                //Use buffering? No. Buffering avoids costly access to disk or network;
                //buffering to an in-memory stream makes no sense.
                result = new ByteArrayOutputStream(bucket.length);
                int bytesRead = 0;
                while (bytesRead != -1) {
                    //aInput.read() returns -1, 0, or more :
                    bytesRead = aInput.read(bucket);
                    if (bytesRead > 0) {
                        result.write(bucket, 0, bytesRead);
                    }
                }
            } finally {
                aInput.close();
                //result.close(); this is a no-operation for ByteArrayOutputStream
            }
        } catch (IOException ex) {
            log(ex);
        }
        return (result!=null)? result.toByteArray() : null ;//if null return (Null pointer case)
    }

    public Scanner getFileReader(String filename) throws FileNotFoundException {
        try {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            return sc;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }
    }

    private static void log(Object aThing) {
        System.out.println(String.valueOf(aThing));
    }

    public int readPrivateKey() {
        int u = 0;
        try {
            Scanner readfile = new Scanner(new FileInputStream("output\\keyOP.txt"));
            readfile.next();
            readfile.next();
            u = readfile.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;


    }

}
