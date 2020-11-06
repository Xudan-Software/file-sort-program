import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Buffer helper class to take in records, keep track of runs, and write to
 * a run file.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class RecordOutputBuffer {
    ByteBuffer buffer;
    RandomAccessFile runFile;
    Record lastRecordInput;
    Runs runs = new Runs();


    /**
     * Create a new RecordOutputBuffer object.
     *
     * @param size    size of the buffer in bytes.
     * @param runFile the file to write runs to.
     */
    public RecordOutputBuffer(int size, RandomAccessFile runFile) {
        buffer = ByteBuffer.allocate(size);
        this.runFile = runFile;
        runs.addRun(new Run(0L, runFile));
    }


    /**
     * Returns true if the passed record is smaller than the last record -
     * indicating that a new run has begun. If the record passed is the first
     * record to this input buffer ever, then returns false.
     *
     * @param r the record to compare to the last record.
     * @return true if the passed record is smaller than the last record. False
     * otherwise.
     */
    private boolean recordSmallerThanLastRecord(Record r) {
        if (lastRecordInput != null) {
            return r.compareTo(lastRecordInput) < 0;
        }
        return false;
    }


    /**
     * Insert a new Record into this buffer.
     *
     * @param newRecord a new Record.
     * @throws IOException if there are issues writing to the run file.
     */
    public void insertRecord(Record newRecord) throws IOException {
        if (recordSmallerThanLastRecord(newRecord)) {
            markRun();
        }
        lastRecordInput = newRecord;
        buffer.put(newRecord.getCompleteRecord());
        if (buffer.position() == buffer.capacity()) {  // buffer is full.
            runFile.write(buffer.array(), 0, buffer.capacity());
            buffer.clear();
        }
    }


    /**
     * Marks the location of a new run in bytes.
     *
     * @throws IOException if there is an issue getting the file pointer from
     *                     the run file.
     */
    private void markRun() throws IOException {
        long locInFile = runFile.getFilePointer() + buffer.position();
        runs.addEndIndexToMostRecentRun(locInFile);
        runs.addRun(new Run(locInFile, runFile));
    }


    /**
     * Writes whatever remains in this buffer to the run file.
     *
     * @throws IOException if there is an issue writing to the run file.
     */
    public void writeRemainingContentsToFile() throws IOException {
        runFile.write(buffer.array(), 0, buffer.position());
        runs.addEndIndexToMostRecentRun(runFile.getFilePointer());
    }


    public Runs getRuns() {
        return this.runs;
    }
}
