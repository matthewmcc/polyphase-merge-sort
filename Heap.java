package minHeap;

public class Heap {
    
    private Node[] Heap;
    private int size;
    private final int FRONT = 1;
    private int maxSize;
    private Boolean CMark;
    
    /**
     * Initializes a Heap object with a max size
     * @param maxsize the upper limit to how many objects the heap may contain
     */
    public Heap(int maxsize) {
        Heap = new Node[maxsize + 1];
        size = 0;
        Heap[0] = new Node("0", true);
        maxSize = maxsize;
        CMark = true;
    }
    
    /**
     * adds a element to the Heap then heap sorts
     * @param s the element to add
     * @param m marks the value if it is in order to the rest of the heap
     */
    public void push(String s, Boolean m) {
        setSize(++size);
        Node n = new Node(s, m);
        Heap[size] = n;
        int current = size;
        upHeap(current);
    }
    
    /**
     * adds a element to the heap then heap sorts
     * mark is not needed as we use this method when adding
     * already sorted data to the heap from runs
     * @param s value of the element
     * @param i value of the run it came from
     */
    public void push(String s, int i){
        setSize(++size);
        Node n = new Node(s, i);
        Heap[size] = n;
        int current = size;
        upHeap(current);
    }
    
    /**
     * Swaps the positions of two objects inside the heap at given index
     * @param p1 index 1
     * @param p2 index 2
     */
    private void swap(int p1, int p2) {
        Node t = Heap[p1];
        Heap[p1] = Heap[p2];
        Heap[p2] = t;
    }
    
    /**
     * if n1 > n2 returns positive int
     * @param n1
     * @param n2
     * @return
     */
    private int compare(Node n1, Node n2) {
        if (n1.Mark == n2.Mark) return n1.Key.compareTo(n2.Key);
        else if (n1.Mark == CMark) return 1;
        else return -1;
    }
    
    /**
     * Checks if the value of the index is smaller than its parents, swaps if true
     * @param i the index to be compared
     */
    private void upHeap(int i) {
        
        while (i > 1 && compare(Heap[i], Heap[i / 2]) < 0) {
            swap(i, i / 2);
            i = i / 2;
        }
    }
    
    /**
     * removes the top value of the heap then downHeap() to re sort the heap
     * @return the value removed
     */
    public String pop() {
        Node n = Heap[FRONT];
        Heap[FRONT] = Heap[size];
        Heap[size] = null;
        setSize(--size);
        downHeap(FRONT);
        return n.Key;
    }
    
    /**
     * same as pop but instead of returning the value of the node it returns the whole node
     * this is needed to extract the output key from the node to tell where the node came from
     * @return smallest node in the heap
     */
    public Node polyPop() {
        Node n = Heap[FRONT];
        Heap[FRONT] = Heap[size];
        Heap[size] = null;
        setSize(--size);
        downHeap(FRONT);
        return n;
    }
    
    /**
     * Swaps the value of a nodes child if it is smaller lexographically
     * @param i the index to be checked
     */
    private void downHeap(int i) {
        while ((i * 2) <= size) {
            int mc = minChild(i);
            
            if (compare(Heap[i], Heap[mc]) > 0) {
                swap(i, mc);
            }
            
            i = mc;
        }
    }
    
    /**
     * @return the top of the heap without effecting the heap
     */
    public Node peek(){
        return Heap[1];
    }
    
    /**
     * @return the size of the heap
     */
    public int getSize(){
        return size;
    }
    
    /**
     * Returns array index of the child with a smaller value
     * @param i the index of the parent node
     * @return the index of the smallest child
     */
    private int minChild(int i) {
        if ((i * 2) + 1 > size) {
            return i * 2;
        } else if (compare(Heap[i*2], Heap[(i * 2) + 1]) > 0) {
            return (i * 2) + 1;
        } else {
            return i * 2;
        }
    }
    
    /**
     * Prints the heap out to console in the format { size of the array, contents..N }
     */
    public void printHeap() {
        // TODO: Have this not print objects beyond the size of the array
        System.out.print("{");
        for (Node n: Heap) {
            System.out.print(n.Key + ", ");
        }
        System.out.println("}");
    }
    
    /**
     * @return True if empty false if not
     */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns a true if the size of the heap and maxSize are equal
     * @return
     */
    public boolean isFull(){
        if ((size) == maxSize)  return true;	
        return false;
    }
    
    /**
     * Sets the size of the array i.e how many elements are contained in the array
     * @param i the new size
     */
    private void setSize(Integer i) {
        this.size = i;
        Heap[0].Key = i.toString();
    }
    
    /**
     * returns the mark of the heap whether it is in sorted order or not
     * @return
     */
    public Boolean getCMark() {
        return CMark;
    }
    
    /**
     * set the heap to sorted order or not
     * @param cMark
     */
    public void setCMark(Boolean cMark) {
        CMark = cMark;
    }
}