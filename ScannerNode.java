package minHeap;

import java.util.Scanner;

/**
 * ScannerNodes are created to have a scanner object assigned to a output file and
 * a number indicating which output file the scanner has been assigned to
 * we use this to track outgoing nodes when we need to replace them in the heap
 * to avoid replacing nodes with nodes from a foriegn run
 */
public class ScannerNode {
    
    public Scanner sc;
    public int outputNum;
    public String value = null;
    
    /**
     * on creation a scannernode will save a pointer to a scanner and a value
     * that identifies which outputfile it is assigned to
     * @param s
     * @param id
     */
    public ScannerNode(Scanner s, int id){
        sc = s;
        outputNum = id;
    }
    
    /**
     * finds the next line in a output file
     * @return the line found
     */
    public String nextValue(){
        
        if (sc.hasNext()){
            value = sc.nextLine();
            return value;
        }
        
        return null;
    }
}