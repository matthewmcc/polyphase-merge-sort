package minHeap;

/**
 * FibList is a class used to identify how we should distribute our runs
 * given a total runs value we can increment the fibonacci sequence until we find a suitable
 * distribution that fits the number of files we are working with
 */
public class FibList {
    public int[] FibNum;
    private int ArrayWidth;
    public int[] RunLengths;
    public int depth;
    
    /**
     * creates the configuration we will be using to distribute the initial runs
     * @param arrayW number of output files
     * @param totalRuns number of runs
     */
    public FibList (int arrayW, int totalRuns) {
        ArrayWidth = arrayW;
        RunLengths = new int[ArrayWidth];
        FibNum = new int[2];
        FibNum[0] = 1;
        FibNum[1] = 1;
        depth = 1;
        
        int i = 0;
        while(i < ArrayWidth) {
            RunLengths[i] = 1;
            i++;
        }
        RunLengths[0] = 0;
        
        while(findConfig() < totalRuns){
            depth++;
            nextLayer();
        }
    }
    
    /**
     * Finds the initial distribution of runs
     * @return The depth required to run to get to termination state
     */
    private int findConfig() {
        int t = 0;
        for (int i : RunLengths)
            t += i;
        return t;
    }
    
    /**
     * Finds the next distribution of runs.
     */
    public void nextLayer() {
        int i = 0;
        int z = findZero();
        
        nextFib();
        int fib = FibNum[0];
        
        while(i < ArrayWidth) {
            RunLengths[i] += fib;
            i++;
        }
        if(z + 1 == ArrayWidth)
            RunLengths[0] = 0;
        else RunLengths[z + 1] = 0;
        depth++;
    }
    
    /**
     * Finds the next 2 fibonnaci numbers in the sequence
     */
    private void nextFib() {
        int t = FibNum[1];
        FibNum[1] = FibNum[0] + FibNum[1];
        FibNum[0] = t;
    }
    
    /**
     * Finds the last 2 fibonnaci numbers in the sequence
     */
    public void lastFib() {
        int t = FibNum[0];
        FibNum[0] = FibNum[1] - FibNum[0];
        FibNum[1] = t;
    }
    
    /**
     * @return The index of the zero in the run suquence
     */
    public int findZero() {
        int i = 0;
        while(true) {
            if(RunLengths[i] == 0) return i;
            i++;
        }
    }
}