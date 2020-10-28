import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Test of XuBuffer Class
 */
public class XuBufferTest {
    RandomAccessFile raFile;
    private XuBuffer fullBuffer;
    private XuBuffer emptyBuffer;


    /**
     * make 16 byte-long array that contains 8 long and 8 double
     * @param l long value
     * @param d double value
     * @return a byte array contains long and double values
     */
    public byte[] makeRedArray(long l, double d) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(l);
        bb.putDouble(d);
        return bb.array();
    }


    /**
     * set up random access file for write and read from Xubuffer
     * full buffer is buffer with full size (5) amout of data
     * empty buffer is an empty buffer with no data
     * @throws FileNotFoundException exception for random access file
     */
    @Before public void setUp() throws FileNotFoundException {
        this.raFile = new RandomAccessFile("test.bin", "rw");
        fullBuffer = new XuBuffer(5 * 16);  // holds 5 records
        emptyBuffer = new XuBuffer(5 * 16);
        fullBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(4L, 4.0D)).getCompleteRecord());
    }


    /**
     * remove the file that created from each test run.
     */
    @After public void tearDown() {
        // TODO: Remove the raFile
    }


    /**
     * test Is Full method when given an empty buffer
     */
    @Test public void testIsFullWhenEmpty() {
        Assert.assertFalse(emptyBuffer.isFull());
    }


    /**
     * test Is Full method when given an full buffer
     */
    @Test public void testIsFullWhenFull() {
        Assert.assertTrue(fullBuffer.isFull());
    }


    /**
     * test Is Full method when given a buffer with almost full(4) value
     */
    @Test public void testIsFullWhenAlmostFull() {
        emptyBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        Assert.assertFalse(emptyBuffer.isFull());
    }


    /**
     * test write to file from an empty buffer
     * @throws IOException random access file exception
     */
    @Test public void testWriteToFileEmptyXuBuffer() throws IOException {
        long beforeWriteLength = raFile.length();
        emptyBuffer.writeToFile(raFile);
        Assert.assertEquals(0, raFile.length() - beforeWriteLength);
    }


    /**
     * test write to file from a full buffer
     * @throws IOException random access file exception
     */
    @Test public void testWriteToFileFullBuffer() throws IOException {
        fullBuffer.writeToFile(raFile);
        Assert.assertEquals(80, raFile.length());
        byte[] raBytes = new byte[(int)raFile.length()];
        raFile.seek(0);
        raFile.readFully(raBytes);
        Assert.assertArrayEquals(fullBuffer.toByteArray(), raBytes);
    }

    /**
     * test write to file from an almost full buffer that contains 9 values
     * @throws IOException random access file exception
     */
    @Test public void testWriteToFileAlmostFullBuffer() throws IOException {
        XuBuffer tenBuffer = new XuBuffer(10 * 16);
        tenBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(4L, 4.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(5L, 5.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(6L, 6.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(7L, 7.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(8L, 8.0D)).getCompleteRecord());
        tenBuffer.writeToFile(raFile);
        Assert.assertEquals(144, raFile.length());
        byte[] raBytes = new byte[(int)raFile.length()];
        raFile.seek(0);
        raFile.read(raBytes, 0, 144);
        Assert.assertArrayEquals(
            Arrays.copyOfRange(tenBuffer.toByteArray(), 0, 144), raBytes);

    }
}
