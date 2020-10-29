import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

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


    private Record makeRandomRec() {
        Random r = new Random();
        return new Record(makeRecArray(r.nextLong(), r.nextDouble()));
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
<<<<<<< Updated upstream
        fullBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(4L, 4.0D)).getCompleteRecord());
=======
        fullBuffer.put(new Record(makeRecArray(0L, 0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRecArray(1L, 1.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRecArray(2L, 2.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRecArray(3L, 3.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRecArray(4L, 4.0D)).getCompleteRecord());
        fullBuffer.flip();
>>>>>>> Stashed changes
    }


    /**
     * remove the file that created from each test run.
     */
    @After public void tearDown() {
        // TODO: Remove the raFile
        File file = new File("test.bin");
        file.delete();
    }


    /**
     * test Is Full method when given an empty buffer
     */
    @Test public void testIsFullWhenEmpty() {
        emptyBuffer.flip();
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
<<<<<<< Updated upstream
        emptyBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
=======

        emptyBuffer.put(new Record(makeRecArray(0L, 0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRecArray(1L, 1.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRecArray(2L, 2.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRecArray(3L, 3.0D)).getCompleteRecord());
        emptyBuffer.flip();
>>>>>>> Stashed changes
        Assert.assertFalse(emptyBuffer.isFull());
    }


    /**
     * test write to file from an empty buffer
     * @throws IOException random access file exception
     */
    @Test public void testWriteToFileEmptyXuBuffer() throws IOException {
        long beforeWriteLength = raFile.length();
        emptyBuffer.flip();
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
<<<<<<< Updated upstream
        tenBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(4L, 4.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(5L, 5.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(6L, 6.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(7L, 7.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRedArray(8L, 8.0D)).getCompleteRecord());
=======
        tenBuffer.put(new Record(makeRecArray(0L, 0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(1L, 1.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(2L, 2.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(3L, 3.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(4L, 4.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(5L, 5.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(6L, 6.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(7L, 7.0D)).getCompleteRecord());
        tenBuffer.put(new Record(makeRecArray(8L, 8.0D)).getCompleteRecord());
        tenBuffer.flip();
>>>>>>> Stashed changes
        tenBuffer.writeToFile(raFile);
        Assert.assertEquals(144, raFile.length());
        byte[] raBytes = new byte[(int)raFile.length()];
        raFile.seek(0);
        raFile.read(raBytes, 0, 144);
        Assert.assertArrayEquals(
            Arrays.copyOfRange(tenBuffer.toByteArray(), 0, 144), raBytes);

    }

<<<<<<< Updated upstream
    @Test public void testGetLastXBytesWithEmptyBuffer(){
        byte[] bytesToReturn=emptyBuffer.getLastXBytes(16);
       Assert.assertArrayEquals(new byte[]{},bytesToReturn);
=======

    /**
     * Test getting the last bytes from an empty buffer returns an empty byte
     * array.
     */
    @Test public void testGetLastXBytesWithEmptyBuffer() {
        emptyBuffer.flip();
        byte[] bytesToReturn = emptyBuffer.getLastXBytes(16);
        Assert.assertArrayEquals(new byte[] {}, bytesToReturn);
>>>>>>> Stashed changes

    }

    @Test public void testGetLastXBytesWithFullBuffer(){
        byte[] bytesToReturn=fullBuffer.getLastXBytes(16);
        Assert.assertArrayEquals(Arrays.copyOfRange(fullBuffer.toByteArray(),64,80),bytesToReturn);

    }

    @Test public void testGetLastXBytesWithAlmostFullBuffer(){
        XuBuffer threeBuffer = new XuBuffer(5 * 16);
<<<<<<< Updated upstream
        threeBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        threeBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        threeBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        byte[] bytesToReturn=threeBuffer.getLastXBytes(16);
        Assert.assertArrayEquals(Arrays.copyOfRange(threeBuffer.toByteArray(),32,48),bytesToReturn);
    }

    @Test public void testGetFirstXBytesWithEmptyBuffer(){
        byte[] bytesToReturn=emptyBuffer.popFirstXBytes(16);
        Assert.assertArrayEquals(new byte[]{},bytesToReturn);
=======
        threeBuffer.put(new Record(makeRecArray(0L, 0D)).getCompleteRecord());
        threeBuffer.put(new Record(makeRecArray(1L, 1.0D)).getCompleteRecord());
        threeBuffer.put(new Record(makeRecArray(2L, 2.0D)).getCompleteRecord());
        threeBuffer.flip();
        byte[] bytesToReturn = threeBuffer.getLastXBytes(16);
        Assert.assertArrayEquals(
            Arrays.copyOfRange(threeBuffer.toByteArray(), 32, 48),
            bytesToReturn);
    }


    /**
     * Test popping the first bytes from an empty buffer returns an empty byte
     * array.
     */
    @Test public void testPopFirstXBytesWithEmptyBuffer() {
        emptyBuffer.flip();
        byte[] bytesToReturn = emptyBuffer.popFirstXBytes(16);
        Assert.assertArrayEquals(new byte[] {}, bytesToReturn);
>>>>>>> Stashed changes
    }

    @Test public void testGetFirstXBytesWithFullBuffer(){
        byte[] bytesToReturn=fullBuffer.popFirstXBytes(32);
        Assert.assertArrayEquals(Arrays.copyOfRange(fullBuffer.toByteArray(),0,32),bytesToReturn);
    }

    @Test public void testGetFirstXBytesWithFullBufferMultiTimes(){
        byte[] bytesToReturn1=fullBuffer.popFirstXBytes(16);
        byte[] bytesToReturn2=fullBuffer.popFirstXBytes(16);
        Assert.assertArrayEquals(Arrays.copyOfRange(fullBuffer.toByteArray(),16,32),bytesToReturn2);
    }
<<<<<<< Updated upstream
    @Test public void testPopFirstXBytesAndLastBytes(){
        byte[] bytesToReturn1=fullBuffer.popFirstXBytes(16);
        byte[] bytesToReturn2=fullBuffer.popFirstXBytes(16);
        byte[] lastBytesToReturn=fullBuffer.getLastXBytes(16);
        Assert.assertArrayEquals(Arrays.copyOfRange(fullBuffer.toByteArray(),0 ,16),bytesToReturn1);
        Assert.assertArrayEquals(Arrays.copyOfRange(fullBuffer.toByteArray(),16,32),bytesToReturn2);
        Assert.assertArrayEquals(Arrays.copyOfRange(fullBuffer.toByteArray(),64,80),lastBytesToReturn);
=======


    /**
     * Tests that using popFirstXBytes and getLastXBytes work together without
     * interference between the two methods.
     */
    @Test public void testPopFirstXBytesAndLastBytes() {
//        byte[] bytesToReturn1 = fullBuffer.popFirstXBytes(16);
//        byte[] bytesToReturn2 = fullBuffer.popFirstXBytes(16);
//        byte[] lastBytesToReturn = fullBuffer.getLastXBytes(16);
//        Assert.assertArrayEquals(
//            Arrays.copyOfRange(fullBuffer.toByteArray(), 0, 16),
//            bytesToReturn1);
//        Assert.assertArrayEquals(
//            Arrays.copyOfRange(fullBuffer.toByteArray(), 16, 32),
//            bytesToReturn2);
//        Assert.assertArrayEquals(
//            Arrays.copyOfRange(fullBuffer.toByteArray(), 64, 80),
//            lastBytesToReturn);
        Record record1 = makeRandomRec();
        Record record2 = makeRandomRec();
        Record record3 = makeRandomRec();
        XuBuffer tempBuffer = new XuBuffer(10 * 16);
        tempBuffer.put(record1.getCompleteRecord());
        tempBuffer.put(record2.getCompleteRecord());
        tempBuffer.put(record3.getCompleteRecord());
        tempBuffer.flip();
        //  Record record1r= new Record(tempBuffer.popFirstXBytes(16));
        Assert.assertTrue(
            record1.equals(new Record(tempBuffer.popFirstXBytes(16))));
        Assert.assertTrue(record2.equals(new Record(tempBuffer.popFirstXBytes(16))));
        Assert.assertTrue(record3.equals(new Record(tempBuffer.getLastXBytes(16))));
>>>>>>> Stashed changes
    }
}
