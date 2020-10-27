import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class XuBufferTest {
    RandomAccessFile raFile;
    private XuBuffer fullBuffer;
    private XuBuffer emptyBuffer;


    public byte[] makeRedArray(long l, double d) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(l);
        bb.putDouble(d);
        return bb.array();
    }


    @Before public void setUp() throws FileNotFoundException {
        this.raFile = new RandomAccessFile("test", "rw");
        fullBuffer = new XuBuffer(5 * 16);  // holds 5 records
        emptyBuffer = new XuBuffer(5 * 16);
        fullBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        fullBuffer.put(new Record(makeRedArray(4L, 4.0D)).getCompleteRecord());
    }

    @After public void tearDown() {
        // TODO: Remove the raFile
    }


    @Test public void testIsFullWhenEmpty() {
        Assert.assertFalse(emptyBuffer.isFull());
    }


    @Test public void testIsFullWhenFull() {
        Assert.assertTrue(fullBuffer.isFull());
    }


    @Test public void testIsFullWhenAlmostFull() {
        emptyBuffer.put(new Record(makeRedArray(0L, 0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(1L, 1.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(2L, 2.0D)).getCompleteRecord());
        emptyBuffer.put(new Record(makeRedArray(3L, 3.0D)).getCompleteRecord());
        Assert.assertFalse(emptyBuffer.isFull());
    }


    @Test public void testWriteToFileEmptyXuBuffer() throws IOException {
        long beforeWriteLength = raFile.length();
        emptyBuffer.writeToFile(raFile);
        Assert.assertEquals(0, raFile.length() - beforeWriteLength);
    }


    @Test public void testWriteToFileFullBuffer() {

    }
}
