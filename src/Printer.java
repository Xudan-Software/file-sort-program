import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * print the first block size (8192 byte) from the sorted file
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class Printer {
    RandomAccessFile file;


    public Printer(RandomAccessFile file) {
        this.file = file;

    }


    public void print() throws IOException {
        int count = 0;
        while (file.getFilePointer() < file.length()) {
            byte[] recordByte = new byte[16];
            file.read(recordByte, 0, recordByte.length);
            Record record = new Record(recordByte);
            if (count == 5) {
                System.out.println("");
                count = 0;
            }
            System.out.printf("%s %s ", record.getID(), record.getKey());
            count++;

            file.seek(file.getFilePointer() + 8192 - 16);
        }
    }
}
