import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Helper class which provides shared functionality to all tests.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class TestHelper {
    Random value = new Random();
    ArrayList<File> testFiles = new ArrayList<>();


    /**
     * Create a new binary file with the given name and number of records.
     *
     * @param filename   the name of the file.
     * @param numRecords the number of records the file must have.
     * @return a RandomAccessFile object linking to the new file.
     * @throws IOException if there are issues creating/writing to the file.
     */
    public RandomAccessFile createRecordFileForTests(
        String filename, int numRecords) throws IOException {
        File newFile = new File(filename);
        boolean created = newFile.createNewFile();
        if (!created) {
            throw new FileAlreadyExistsException(filename);
        }
        RandomAccessFile raFile = new RandomAccessFile(newFile, "rw");
        DataOutputStream file = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(filename)));
        long val;
        double val2;
        for (int i = 0; i < numRecords; i++) {
            val = value.nextLong();
            file.writeLong(val);
            val2 = value.nextDouble();
            file.writeDouble(val2);
        }
        file.flush();
        file.close();
        testFiles.add(newFile);
        return raFile;
    }


    /**
     * Delete any files created for tests.
     */
    public void deleteTestFiles() {
        for (File f : testFiles) {
            f.delete();
        }
    }


    /**
     * make 16 byte-long array that contains 8 long and 8 double
     *
     * @param l long value
     * @param d double value
     * @return a byte array contains long and double values
     */
    public byte[] makeRecArray(long l, double d) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(l);
        bb.putDouble(d);
        return bb.array();
    }
}
