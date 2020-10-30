import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class InputBufferTest {
    RandomAccessFile randAccFile =
        new RandomAccessFile("inputBufferTest.bin", "rw");


    public InputBufferTest() throws FileNotFoundException {
    }


    @Before public void setUp() throws IOException {

        for (int i = 0; i < 100; i++) {
            randAccFile.write(makeRecArray(i, i));
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


    @Test public void testInitialization() throws IOException {
        new InputBuffer(10, randAccFile);
    }


    @Test public void testPopFirstXBytes() {

    }
}
