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
     * Gets the first length number of bytes in this InputBuffer.
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
     * Load data into this buffer from file. Only call this if the buffer is
     * completely empty.
     *
     * @throws IOException If the file does not exist.
     */
    private void loadFromFile() throws IOException {
        theBuffer.position(0);
        if (file.length() - file.getFilePointer() > theBuffer.capacity()) {
            lastValueInBuffer = theBuffer.capacity();
            file.read(theBuffer.array(), 0, theBuffer.capacity());
        }
        else {
            lastValueInBuffer = file.length() - file.getFilePointer();
            file.read(theBuffer.array(), 0, Math.toIntExact(lastValueInBuffer));
        }
    }


    /**
     * Returns true if the buffer is empty, false otherwise.
     *
     * @return true if the buffer is empty, else returns false.
     */
    private boolean isEmpty() {
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
