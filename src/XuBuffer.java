import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * ByteBuffer wrapper class for making operations on a ByteBuffer both easier
 * and more testable.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class XuBuffer {
    private final ByteBuffer theBuffer;


    /**
     * Create a new XuBuffer object with the given size in bytes.
     *
     * @param size the size of the buffer in bytes.
     */
    public XuBuffer(int size) {
        this.theBuffer = ByteBuffer.allocate(size);
    }


    /**
     * Returns true if the buffer is full. False otherwise.
     *
     * @return true if the buffer is full. False otherwise.
     */
    public boolean isFull() {
        return theBuffer.position() == theBuffer.capacity();
    }


    /**
     * Write the contents of this XuBuffer to the given file.
     *
     * @param raFile the file to write the contents of this buffer to.
     */
    public void writeToFile(RandomAccessFile raFile) {
        try {
            raFile.write(theBuffer.array(), 0, theBuffer.position());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the last length number of bytes in this XuBuffer. Should be used to
     * get the most recent data inserted into the buffer.
     *
     * @param length the number of bytes to get from the buffer.
     * @return array of bytes.
     */
    public byte[] getLastXBytes(int length) {
        byte[] bytesToReturn = new byte[length];
        theBuffer.get(bytesToReturn, theBuffer.position() - length, length);
        return bytesToReturn;
    }


    /**
     * Gets the first length number of bytes in this XuBuffer.
     *
     * @param length the number of bytes to get from the buffer.
     * @return array of bytes.
     */
    public byte[] getFirstXBytes(int length) {
        byte[] bytesToReturn = new byte[length];
        theBuffer.get(bytesToReturn, 0, length);
        return bytesToReturn;
    }


    public void put(byte[] bytes) {
        theBuffer.put(bytes);
    }

    public byte[] toByteArray() {
        return theBuffer.array();
    }

}
