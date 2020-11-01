import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class RecordOutputBuffer {
    ByteBuffer buffer;
    RandomAccessFile runFile;
    LinkedList<Long> runIndexes;
    Record lastRecordInput;


    /**
     * Create a new RecordOutputBuffer object.
     *
     * @param size size of the buffer in bytes.
     * @param runFile
     */
    public RecordOutputBuffer(int size, RandomAccessFile runFile) {
        buffer = ByteBuffer.allocate(size);
        this.runFile = runFile;
        runIndexes = new LinkedList<>();
        runIndexes.add(0L);  // the first run starts at index 0
    }


    private boolean recordSmallerThanLastRecord(Record r) {
        if (lastRecordInput != null) {
            return r.compareTo(lastRecordInput) < 0;
        }
        return false;
    }


    public void insertRecord(Record newRecord) throws IOException {
        if (recordSmallerThanLastRecord(newRecord)) {
            markRun();
        }
        lastRecordInput = newRecord;
        buffer.put(newRecord.getCompleteRecord());
        if (buffer.position() == buffer.capacity()) {
            runFile.write(buffer.array(), 0, buffer.capacity());
            buffer.clear();
        }
    }


    private void markRun() throws IOException {
        long locInFile = runFile.getFilePointer() + buffer.position();
        runIndexes.add(locInFile);
    }


    public void writeRemainingContentsToFile() throws IOException {
        runFile.write(buffer.array(), 0, buffer.limit());
    }

    public LinkedList<Long> getRunIndexes() {
        return this.runIndexes;
    }
}
