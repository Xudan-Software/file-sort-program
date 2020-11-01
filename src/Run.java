import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class Run {
    long startIndex;
    long runLength;
    RandomAccessFile runFile;
    ByteBuffer runBuffer;
    long runFilePointer;

    // can use a linked list
    // can use an array
    // all that matters is the popNextVal returns its next smallest value
    // and that isExhausted returns true if the run has no values left

    public Run(long startIndex,long runLength, RandomAccessFile runFile, int memorySize) {
        runFilePointer = startIndex;
        this.runLength = runLength;
        this.runFile=runFile;
        runBuffer=ByteBuffer.allocate(memorySize);

    }

    public void loadBuffer() throws IOException {
        long readSize = Math.min(runBuffer.capacity(),runLength-runFilePointer);
        runFile.read(runBuffer.array(),(int)runFilePointer,(int)readSize);
        runFilePointer=runFile.getFilePointer();
    }

    /**
     * Returns the next value in the run, but does not pop it and remove it
     * from the run.
     * @return
     */
    public Record peekNextVal() {

    }


    /**
     * Remove and return the next record from the run
     * @return
     */
    public Record popNextVal() {

    }

    public boolean isExhausted() {

    }




}
