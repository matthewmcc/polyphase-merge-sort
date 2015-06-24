package minHeap;

/**
 * Creates a Node to be added to a heap
 *
 * A node has a value, a mark to say if it is sorted inside the heap it is in or not, and an
 * output which will be equal to the run it has been assigned to if it is in a run
 */
public class Node {
    public String Key;
    public Boolean Mark;
    public int Output;
    
    /**
     * Create a Node for sorting into a heap from an unsorted source
     * @param key
     * @param mark
     */
    public Node (String key, Boolean mark) {
        Key = key;
        Mark = mark;
    }
    
    /**
     * create a node for sorting into a heap from a sorted source, i.e runs
     * @param key
     * @param output
     */
    public Node (String key, int output){
        Key = key;
        Output = output;
        Mark = false;
    }
    
}