import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
            newFile.delete();
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


    /**
     * Taken from https://stackoverflow.com/questions/5388146/copy-and-rename-file-on-different-location
     *
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new RandomAccessFile(sourceFile, "rw").getChannel();
            destination = new RandomAccessFile(destFile, "rw").getChannel();

            long position = 0;
            long count = source.size();

            source.transferTo(position, count, destination);
        }
        finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        testFiles.add(destFile);
    }
}
