import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Run {
    long initalStartIndex;
    long runLength;
    RandomAccessFile runFile;
    ByteBuffer runBuffer;
    long runFilePointer;


    public Run(
        long startIndex, RandomAccessFile runFile) {
        initalStartIndex = startIndex;
        runFilePointer = startIndex;
        this.runFile = runFile;
    }


    public void loadBuffer() throws IOException {
        runBuffer.position(0);
        long readSize =
            Math.min(runBuffer.capacity(), runLength - runFilePointer);
        runFile.read(runBuffer.array(), (int)runFilePointer, (int)readSize);
        runFilePointer += readSize;
    }


    /**
     * Returns the next value in the run, but does not pop it and remove it
     * from the run.
     *
     * @return return the first record
     */
    public Record peekNextVal() throws IOException {
        Record record = getNextRecordFromBuffer();
        runBuffer.position(runBuffer.position() - 16);
        return record;
    }


    /**
     * Remove and return the next record from the run
     *
     * @return return the next record and remove it from the run buffer
     */
    public Record popNextVal() throws IOException {
        return getNextRecordFromBuffer();
    }


    /**
     * return if runBuffer and input file are both exhausted
     *
     * @return true is both exhausted, else return false
     */
    public boolean isExhausted() {
        return (bufferIsEmpty() && (runFilePointer
            == initalStartIndex + runLength));
    }


    /**
     * see if the buffer is empty
     *
     * @return true if buffer is empty, else return false
     */
    private boolean bufferIsEmpty() {
        return (runBuffer.position() == runBuffer.limit());
    }


    /**
     * helper method for pop and peek next value method
     *
     * @return return the next record from the run buffer
     * @throws IOException if the input file and run buffer is exhausted
     */
    private Record getNextRecordFromBuffer() throws IOException {
        if (isExhausted()) {
            throw new IllegalStateException();
        }
        if (bufferIsEmpty()) {
            loadBuffer();
        }
        byte[] recordByte = new byte[16];
        runBuffer.get(recordByte, runBuffer.position(), 16);
        return new Record(recordByte);
    }


    public long getInitalStartIndex() {
        return this.initalStartIndex;
    }


    public void addLength(long length) {
        this.runLength = length;
    }

    public void initializeRunBufferOfSize(int size) throws IOException {
        runBuffer = ByteBuffer.allocate(size);
        loadBuffer();
    }

}
