/**
 * Minimum heap implementation. Code comes from OpenDSA's MaxHeap
 * implementation with minor changes.
 *
 * @author Xu Wang, Jordan Gillard
 * @version 1.0
 */
public class MinHeap<T extends Comparable<T>> {
    private final T[] heap; // Pointer to the heap array
    private final int size;          // Maximum size of the heap
    private int n;             // Number of things now in heap


    /**
     * Constructs a new Minimum Heap object.
     *
     * @param h   the array to use as the underlying heap.
     * @param num the number of elements already in the array.
     * @param max the max elements that the heap can hold.
     */
    MinHeap(T[] h, int num, int max) {
        heap = h;
        n = num;
        size = max;
        buildheap();
    }


    /**
     * Swap the elements e1 & e2 in the array h.
     *
     * @param h  an array of comparable elements.
     * @param e1 the index of one of the elements to swap.
     * @param e2 the index of the other element to swap.
     */
    private void swap(Comparable<T>[] h, int e1, int e2) {
        if (e1 >= h.length || e2 >= h.length) {
            throw new IllegalStateException();
        }
        Comparable<T> temp = h[e1];
        h[e1] = h[e2];
        h[e2] = temp;
    }


    /**
     * Get the size of the heap.
     *
     * @return the size of the heap.
     */
    int heapsize() {
        return n;
    }


    /**
     * Returns true if pos a leaf position, false otherwise.
     *
     * @param pos true if pos a leaf position, false otherwise.
     * @return boolean
     */
    boolean isLeaf(int pos) {
        return (pos >= n / 2) && (pos < n);
    }


    /**
     * Returns the position of the left child of the given position. Returns
     * -1 if there is no left child.
     *
     * @param pos the position of the node to get the left child of.
     * @return position of the left child.
     */
    int leftchild(int pos) {
        if (pos >= n / 2)
            return -1;
        return 2 * pos + 1;
    }


    /**
     * Returns the position of the right child of the given position. Returns
     * -1 if there is no right child.
     *
     * @param pos the position of the node to get the right child of.
     * @return position of the right child.
     */
    int rightchild(int pos) {
        if (pos >= (n - 1) / 2)
            return -1;
        return 2 * pos + 2;
    }


    /**
     * Returns position for the parent of the given position. Returns -1
     * if there is no parent for the given position.
     *
     * @param pos the position of the child node.
     * @return position of the parent.
     */
    int parent(int pos) {
        if (pos <= 0)
            return -1;
        return (pos - 1) / 2;
    }


    /**
     * Insert the given value into the heap.
     *
     * @param key the value to insert.
     * @throws IllegalStateException if the heap is full.
     */
    void insert(T key) {
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
    void buildheap() {
        for (int i = n / 2 - 1; i >= 0; i--)
            siftdown(i);
    }


    /**
     * Put element at the given position into the correct place, i.e. replace
     * it with one of it's children if it's child has a smaller value.
     *
     * @param pos the position of the element.
     */
    void siftdown(int pos) {
        if ((pos < 0) || (pos >= n))
            return; // Illegal position
        while (!isLeaf(pos)) {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (heap[j].compareTo(heap[j + 1]) > 0)) {
                j++;
            }// j is now index of child with greater value
            if (heap[pos].compareTo(heap[j]) <= 0) {
                return;
            }
            swap(heap, pos, j);
            pos = j;  // Move down
        }
    }


    /**
     * Remove the minimum (i.e. root) value from the heap and return it.
     *
     * @return the minimum value in the heap.
     * @throws IllegalStateException when there are no elements in the heap
     */
    T removemin() {
        if (n == 0)
            throw new IllegalStateException();  // Removing from empty heap
        swap(heap, 0, --n); // Swap minimum with last value
        siftdown(0);   // Put new heap root val in correct place
        return heap[n];
    }


    /**
     * Removes the element at the given position and returns it.
     *
     * @param pos the position of the element in the heap.
     * @return the element at pos.
     * @throws IllegalArgumentException when n is greater or smaller than the
     *                                  heap.
     */
    T remove(int pos) {
        if ((pos < 0) || (pos >= n))
            throw new IllegalArgumentException(); // Illegal heap position
        if (pos == (n - 1))
            n--; // Last element, no work to be done
        else {
            swap(heap, pos, --n); // Swap with last value
            update(pos);
        }
        return heap[n];
    }


    /**
     * Modify the value at the given position by setting it's value to the new
     * value.
     *
     * @param pos    the position in the heap to be modified.
     * @param newVal the new value to be placed that the position.
     */
    void modify(int pos, T newVal) {
        if ((pos < 0) || (pos >= n))
            return; // Illegal heap position
        heap[pos] = newVal;
        update(pos);
    }


    /**
     * The value at pos has been changed, so restore the minimum heap property.
     *
     * @param pos the position that has changed.
     */
    void update(int pos) {
        // If it is a small value, push it up
        while ((pos > 0) && (heap[pos].compareTo(heap[parent(pos)]) < 0)) {
            swap(heap, pos, parent(pos));
            pos = parent(pos);
        }
        siftdown(pos); // If it is larger, push down
    }


    /**
     * Return the underlying array used by the heap. This should only be used
     * for testing purposes.
     *
     * @return the underlying array used by the heap.
     */
    public Comparable<T>[] getArray() {
        return this.heap;
    }


    /**
     * Get the minimum (root) value, but do not delete it.
     *
     * @return the minimum (root) value.
     */
    public T peek() {
        return heap[0];
    }


    /**
     * Inserts the given value into the Heap, but limits it's access so that
     * the heap can not load it into the output buffer.
     *
     * @param newVal The new value to insert into the heap.
     */
    public void insertAndDecrement(T newVal) {
        // Note: we only call this when root is empty
        modify(0, heap[n]);
        modify(n, newVal);
        n--;
        // Restore the min heap property
        siftdown(0);
    }
}
