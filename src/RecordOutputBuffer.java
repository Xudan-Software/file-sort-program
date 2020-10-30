import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class RecordOutputBuffer {
    ByteBuffer buffer;
    RandomAccessFile runFile;
    LinkedList<Long> runIndexes;
    Record lastRecordInput;


    public RecordOutputBuffer(int size, RandomAccessFile runFile) {
        buffer = ByteBuffer.allocate(size);
        this.runFile = runFile;
        runIndexes = new LinkedList<>();
    }


    public boolean recordSmallerThanLastRecord(Record r) {
        if (lastRecordInput != null) {
            return r.compareTo(lastRecordInput) < 0;
        }
        return false;
    }


    public void insertRecord(Record newRecord) throws IOException {
        if (recordSmallerThanLastRecord(newRecord)) {
            writeContentsToFile();
            buffer.clear();
            markRun();
        }
        lastRecordInput = newRecord;
        buffer.put(newRecord.getCompleteRecord());
    }


    public void writeContentsToFile() throws IOException {
        // TODO: Test this
        buffer.flip();
        runFile.write(buffer.array());
        buffer.flip();
    }


    public void markRun() throws IOException {
        long locInFile = runFile.getFilePointer();
        runIndexes.add(locInFile);
    }


    public void writeRemainingContentsToFile() throws IOException {
        buffer.flip();
        runFile.write(buffer.array(), 0, buffer.limit());
        buffer.flip();
    }
}
