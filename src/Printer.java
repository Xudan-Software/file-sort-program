import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * print the first record from each block size (8192 byte) of the sorted file.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class Printer {
    private final RandomAccessFile file;


    /**
     * Create a new Printer object
     *
     * @param file the file of sorted records.
     */
    public Printer(RandomAccessFile file) {
        this.file = file;

    }


    /**
     * Print the first record from each block of the sorted file.
     *
     * @throws IOException if there is an issue reading from the sorted file.
     */
    public void print() throws IOException {
        // TODO: Make this static
        int count = 0;
        while (file.getFilePointer() < file.length()) {
            byte[] recordByte = new byte[16];
            file.read(recordByte, 0, recordByte.length);
            Record record = new Record(recordByte);
            if (count == 5) {
                System.out.println();
                count = 0;
            }
            System.out.printf("%s %s ", record.getID(), record.getKey());
            count++;

            file.seek(file.getFilePointer() + 8192 - 16);
        }
    }
}
