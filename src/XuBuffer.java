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
    private int front = 0; // this the position pointer for remove purpose


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
        return theBuffer.limit() == theBuffer.capacity();
    }


    public void setFront(int frontPos) {
        this.front = frontPos;
    }


    /**
     * Write the contents of this XuBuffer to the given file.
     *
     * @param raFile the file to write the contents of this buffer to.
     */
    public void writeToFile(RandomAccessFile raFile) {
        try {
            raFile.write(theBuffer.array(), 0, theBuffer.limit());
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
        if (theBuffer.limit() - length < 0) {
            return new byte[] {};
        }
        byte[] bytesToReturn = new byte[length];

        theBuffer.position(theBuffer.limit() - length);
        theBuffer.get(bytesToReturn, 0, length);
        return bytesToReturn;
    }


    /**
     * Gets the first length number of bytes in this XuBuffer.
     *
     * @param length the number of bytes to get from the buffer.
     * @return array of bytes.
     */
    public byte[] popFirstXBytes(int length) {
        if (theBuffer.limit() - length < 0) {
            return new byte[] {};
        }
        byte[] bytesToReturn = new byte[length];

        // theBuffer.position(front);
        theBuffer.get(bytesToReturn, 0, length);

        // front+=length;
        return bytesToReturn;
    }


    /**
     * put a byte array into the buffer
     *
     * @param bytes array to be input
     */
    public void put(byte[] bytes) {
        theBuffer.put(bytes);
    }


    /**
     * output a byte array of the buffer
     *
     * @return
     */
    public byte[] toByteArray() {
        return theBuffer.array();
    }


    /**
     * clear the buffer. set up position to 0 and set limit to capacity
     */
    public void clear() {
        theBuffer.clear();
    }


    /**
     * if the buffer is empty
     *
     * @return true if the buffer is empty, else return false
     */
    public boolean isEmpty() {
        return theBuffer.position() == theBuffer.limit();
    }


    /**
     * flip the buffer, set the limit equals to position
     * set the position equals to zero.
     * call this method when ready to output from the buffer
     */
    public void flip() {
        theBuffer.flip();
    }
}
