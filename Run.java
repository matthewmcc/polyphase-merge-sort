package minHeap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The run class is a list implementation that allows access to the first object only with free
 * ability to add elements to the end of the run used to store sorted datums from a heap
 */
public class Run {
    
    private List<String> run;
    private int size;
    
    /**
     * Creates a run with a minimum size set by the user
     * @param RunSize the minimum amount of elements allowed in the Run
     */
    public Run(int RunSize){
        run = new ArrayList<String>();
        size = 0;
    }
    
    /**
     * Adds a element to the bottom of the list
     * @param element the elements to be added
     */
    public void append(String element){
        run.add(element);
        size++;
    }
    
    /**
     * tempRun creates a temp file
     * @throws IOException
     */
    public void tempRun(String dir) throws IOException {
        File outputFile = new File(dir + File.separator + "temp.txt");
        PrintWriter writer = new PrintWriter(new FileWriter(outputFile, true));
        
        for(String s : run){
            writer.println(s);
        }
        
        writer.println("*");
        
        writer.close();
    }
    
    /**
     * returns the entire run as a string in the format { n, n2, ... , n(size), }
     */
    public String toString(){
        String runString = "{ ";
        
        for (String s : run){
            runString = runString.concat(s + ", ");
        }
        
        runString = runString.concat(" }");
        
        return runString;
    }
    
    /**
     * removes the top element of the run
     * @return the element removed
     */
    public String pop(){
        
        if (size != 0){
            String popped = run.remove(0);
            size--;
            return popped;
        }
        else{
            return null;
        }
    }
    
    /**
     * returns the top element of the list without removing the element
     * @return the top element of the list
     */
    public String peek(){
        return run.get(0);
    }
    
    /**
     * returns the largest value of a run
     * @return
     */
    public String peekLast(){
        return run.get(run.size() - 1);				
    }
    
    /**
     * returns the run in List format
     */
    public List<String> getList(){
        return this.run;
    }
    
    /**
     * Checks the size of the list to test for emptiness 
     * @return true if empty false otherwise
     */
    public boolean isEmpty(){
        if (size >= 1) return false;
        else return true;
    }
}