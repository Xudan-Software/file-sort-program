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
public class InputBuffer {
    private final ByteBuffer theBuffer;
    private final RandomAccessFile file;
    private long lastValueInBuffer;


    /**
     * Create a new InputBuffer object with the given size in bytes and source
     * file.
     *
     * @param size the size of the buffer in bytes.
     */
    public InputBuffer(int size, RandomAccessFile file) throws IOException {
        this.theBuffer = ByteBuffer.allocate(size);
        this.file = file;
        loadFromFile();
    }


    /**
     * Gets the first length number of bytes in this XuBuffer.
     *
     * @param length the number of bytes to get from the buffer.
     * @return array of bytes.
     * @throws IllegalStateException If the buffer is empty and the file is
     *                               empty.
     */
    public byte[] popFirstXBytes(int length) throws IOException {
        if (this.isEmpty() && file.length() == file.getFilePointer()) {
            throw new IllegalStateException();
        }
        if (this.isEmpty()) {
            loadFromFile();
        }
        if (theBuffer.position() + length > theBuffer.capacity()) {
            return new byte[] {};
        }
        byte[] bytesToReturn = new byte[length];

        theBuffer.get(bytesToReturn, 0, length);
        return bytesToReturn;
    }


    /**
     * Load data into the buffer from file. Only call this if the buffer is
     * completely empty.
     *
     * @throws IOException If the file does not exist.
     */
    private void loadFromFile() throws IOException {
        theBuffer.position(0);
        // This is fine
        if (file.length() - file.getFilePointer() > theBuffer.capacity()) {
            lastValueInBuffer = theBuffer.capacity();
            file.read(theBuffer.array(), 0, theBuffer.capacity());
        }
        // This wont let the buffer know where it ends
        else {
            lastValueInBuffer = file.length() - file.getFilePointer();
            file.read(theBuffer.array(), 0, Math.toIntExact(lastValueInBuffer));
        }
    }


    /**
     * if the buffer is empty
     *
     * @return true if the buffer is empty, else return false
     */
    private boolean isEmpty() {
        // Usually the last value in the buffer is the buffers capacity,
        // but sometimes there aren't enough values in the file to load the
        // buffer all of the way.
        return theBuffer.position() == lastValueInBuffer;
    }


    /**
     * Returns true if both the file has no new values and this is empty.
     *
     * @return true if both the file has no new values and this is empty.
     * @throws IOException if the file does not exist.
     */
    public boolean isExhausted() throws IOException {
        return file.length() == file.getFilePointer() && this.isEmpty();
    }
}
