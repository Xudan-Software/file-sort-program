import java.io.IOException;

/**
 * Minimum heap implementation. Code comes from OpenDSA's MaxHeap
 * implementation with minor changes.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class MinHeap {
    private final Record[] heap; // Pointer to the heap array
    private final int size;          // Maximum size of the heap
    private final InputBuffer buffer;
    private int n;             // Number of things now in heap
    private int badVals = 0;


    /**
     * Constructs a new Minimum Heap object.
     *
     * @param h      the array to use as the underlying heap.
     * @param num    the number of elements already in the array.
     * @param max    the max elements that the heap can hold.
     * @param buffer the input buffer that load records for the heap
     */
    MinHeap(Record[] h, int num, int max, InputBuffer buffer) {
        heap = h;
        n = num;
        size = max;
        buildheap();
        this.buffer = buffer;
    }


    /**
     * Swap the elements e1 & e2 in the array h.
     *
     * @param h  an array of comparable elements.
     * @param e1 the index of one of the elements to swap.
     * @param e2 the index of the other element to swap.
     */
    private void swap(Record[] h, int e1, int e2) {
        if (e1 >= h.length || e2 >= h.length) {
            throw new IllegalStateException();
        }
        Record temp = h[e1];
        h[e1] = h[e2];
        h[e2] = temp;
    }


    /**
     * Get the size of the heap. This does not include bad values.
     *
     * @return the size of the heap.
     */
    public int heapsize() {
        return n;
    }


    /**
     * Get the number of bad values in the heap.
     *
     * @return the size of the heap.
     */
    public int numBadVals() {
        return badVals;
    }


    /**
     * Checks whether the input buffer is completely empty, and if the heap is
     * empty of both good and bad values.
     *
     * @return true if the input buffer and heap are completely empty. False
     * otherwise.
     * @throws IOException if the buffer has an error checking its underlying
     *                     random access file.
     */
    public boolean isFinished() throws IOException {
        return badVals == 0 && n == 0 && buffer.isExhausted();
    }


    /**
     * Returns true if pos a leaf position, false otherwise.
     *
     * @param pos true if pos a leaf position, false otherwise.
     * @return boolean
     */
    private boolean isLeaf(int pos) {
        return (pos >= n / 2) && (pos < n);
    }


    /**
     * Returns the position of the left child of the given position. Returns
     * -1 if there is no left child.
     *
     * @param pos the position of the node to get the left child of.
     * @return position of the left child.
     */
    private int leftchild(int pos) {
        if (pos >= n / 2) {
            return -1;
        }
        return 2 * pos + 1;
    }


    /**
     * Returns position for the parent of the given position. Returns -1
     * if there is no parent for the given position.
     *
     * @param pos the position of the child node.
     * @return position of the parent.
     */
    private int parent(int pos) {
        if (pos <= 0) {
            return -1;
        }
        return (pos - 1) / 2;
    }


    /**
     * Insert the given value into the heap.
     *
     * @param key the value to insert.
     * @throws IllegalStateException if the heap is full.
     */
    public void insert(Record key) {
        if (n >= size) {
            throw new IllegalStateException();
        }
        int curr = n++;
        heap[curr] = key;  // Start at end of heap
        // Now sift up until curr's parent's key > curr's key
        while ((curr != 0) && (heap[curr].compareTo(heap[parent(curr)]) < 0)) {
            swap(heap, curr, parent(curr));
            curr = parent(curr);
        }
    }


    /**
     * Heapify the heap, i.e. perform the tasks to assure that all parent nodes
     * are smaller than their child nodes.
     */
    private void buildheap() {
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftdown(i);
        }
    }


    /**
     * Put element at the given position into the correct place, i.e. replace
     * it with one of it's children if it's child has a smaller value.
     *
     * @param pos the position of the element.
     */
    private void siftdown(int pos) {
        if ((pos < 0) || (pos >= n)) {
            return;
        } // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (heap[j].compareTo(heap[j + 1]) > 0)) {
                j++;
            } // j is now index of child with greater value
            if (heap[pos].compareTo(heap[j]) <= 0) {
                return;
            }
            swap(heap, pos, j);
            pos = j;  // Move down
        }
    }


    /**
     * Remove the minimum (i.e. root) value from the heap and return it. The
     * behavior of this function is the default min heap behavior and should
     * only be used when the input buffer is exhausted.
     *
     * @return the minimum value in the heap.
     * @throws IllegalStateException when there are no elements in the heap
     */
    private Record defaultRemoveMin() {
        if (n == 0) {
            throw new IllegalStateException();
        }  // Removing from empty heap
        swap(heap, 0, --n); // Swap minimum with last value
        siftdown(0);   // Put new heap root val in correct place
        return heap[n];
    }


    /**
     * Gets the next minimum value from the buffer.
     *
     * @return the next min Record from the heap.
     * @throws IOException If the file the buffer uses does not exist
     */
    private Record getNextMinValue() throws IOException {
        if (buffer.isExhausted()) {
            return defaultRemoveMin();
        }
        else {
            Record minVal = heap[0];
            heap[0] = null;
            Record inputBufferRec = new Record(buffer.popFirstXBytes(16));
            if (inputBufferRec.compareTo(minVal) < 0) {
                insertAndDecrement(inputBufferRec);
            }
            else {
                selectionInsert(inputBufferRec);
            }
            return minVal;
        }
    }


    /**
     * Remove the minimum (i.e. root) value from the heap and return it.
     *
     * @return the minimum value in the heap.
     * @throws IllegalStateException when there are no elements in the heap
     * @throws IOException           if the file for the buffer does not exist
     */
    public Record removemin() throws IOException {
        if (n > 0) {
            return getNextMinValue();
        }
        if (badVals == 0 && buffer.isExhausted()) {
            throw new IllegalStateException();
        }
        if (badVals > 0) {
            copyBadValToFront();
            n = badVals;
            badVals = 0;
            buildheap();
        }
        if (n < size) {
            // we still have free spaces in the heap
            while (!buffer.isExhausted() && n < size) {
                insert(new Record(buffer.popFirstXBytes(16)));
            }
        }
        return getNextMinValue();
    }


    /**
     * Insert the given value into the null head position, and heapify.
     *
     * @param key the value to insert.
     * @throws IllegalStateException if the heap is full.
     */
    private void selectionInsert(Record key) {
        if (n > size) {
            throw new IllegalStateException();
        }
        if (heap[0] != null) {
            throw new IllegalStateException();
        }
        heap[0] = key;
        siftdown(0);
    }


    /**
     * Move bad values from back of this heap to front of this heap.
     */
    private void copyBadValToFront() {
        if (badVals >= 0) {
            System.arraycopy(heap, size - badVals, heap, 0, badVals);
        }
    }


    /**
     * Inserts the given value into the Heap, but limits it's access so that
     * the heap can not load it into the output buffer. Note that this method
     * should only be called when the root node is null.
     *
     * @param newVal The new value to insert into the heap.
     */
    public void insertAndDecrement(Record newVal) {
        heap[0] = heap[n - 1];
        heap[n - 1] = newVal;
        n--;
        siftdown(0);
        badVals++;
    }
}
