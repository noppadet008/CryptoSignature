import java.io.File;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    void writeText(byte[] aInput ) {
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

    public Scanner getFileReader() {
        try {
            File file = new File(INPUT_FILE_NAME);
            Scanner sc = new Scanner(file);
            return sc;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void log(Object aThing) {
        System.out.println(String.valueOf(aThing));
    }

}
