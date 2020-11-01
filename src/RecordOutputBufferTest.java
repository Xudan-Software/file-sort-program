import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class RecordOutputBufferTest {
    RecordOutputBuffer recordOutputBuffer;
    File runFile;


    @Before public void setUp() throws IOException {
        runFile = new File("recordOutputRunFile.bin");
        boolean created = runFile.createNewFile();
        if (!created) {
            throw new IllegalStateException("Run file already exists");
        }
        recordOutputBuffer =
            new RecordOutputBuffer(160, new RandomAccessFile(runFile, "rw"));
    }


    @After public void tearDown() {
        runFile.delete();
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


    @Test public void testInsertFirstRecordNoIssues() throws IOException {
        Record record = new Record(makeRecArray(1, 1));
        recordOutputBuffer.insertRecord(record);
    }


    @Test public void testInsertSmallerRecordCreatesRun() throws IOException {
        Record record;
        for (int i = 0; i < 8; i++) {
            record = new Record(makeRecArray(i, i));
            recordOutputBuffer.insertRecord(record);
        }
        record = new Record(makeRecArray(0, 0));
        recordOutputBuffer.insertRecord(record);
        LinkedList<Long> llist = recordOutputBuffer.getRunIndexes();
        Assert.assertTrue(0L == llist.getFirst());
        // The next
        Assert.assertTrue(128L == llist.getLast());
    }

}
