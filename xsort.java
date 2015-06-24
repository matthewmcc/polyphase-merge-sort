package minHeap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class xsort {
    
    //JAMES LUXTON 1190809
    //MATTHEW MCCULLOUGH 1058868
    
    // This value is used to assign which output file is being read in and out
    static int OutputNum = 1;
    // size of the heap we will be working with
    static int runsize = 7;
    // amount of output files we will have open to sort with
    static int numfiles = 7;
    // directory temp files will be written to
    static String tempdir = System.getProperty("user.dir");
    // name of the final sorted file
    static String outputfilename = "stdout.txt";
    // our fib list master object, singleton design pattern
    static FibList fibL;
    // the inputfilename optional
    static String inputfilename = "notset";
    // total sort operations
    static int operations = 0;
    
    /**
     *
     * Xsort is a sorting algorithm authored by James Luxton - 1190809 and Matthew McCullough - 1058868
     * designed to sort large text files to large to fit into virtual memory. It completes this by using replacement
     * selection sort, a priority queue and a fibbonacci distribution sequence function that discovers
     * they ideal way to distribute the runs and complete the sort in the fastest time possible
     *
     * This program has been created for COMP317 Assignment 1 at the University of Waikato under Tony Smith
     *
     * @param args command line values
     * @throws IOException
     * @throws org.apache.commons.cli.ParseException
     */
    public static void main(String[] args) throws ParseException, IOException {
        
        
        // Reads in arguments from the commandline
        for (int i = 0; i < args.length; i++){
            try{
                if (args[i].equals("-r")) runsize = Integer.parseInt(args[i + 1]);
                if (args[i].equals("-k")) numfiles = Integer.parseInt(args[i + 1]);
                if (args[i].equals("-o")) outputfilename = args[i + 1];
                if (args[i].equals("-d")) tempdir = args[i + 1];
                if (args[i].equals("-i")) inputfilename = args[i + 1];
            }
            catch(Exception e){
                System.err.println("Please input valid in line arguments, -o Output file name, -d tempfile directory, -r heap size, -k number of output files, -i for input file name");
            }
        }
        
        // Print runtime arguments to the System.out
        System.err.println("Heap Size: " + runsize);
        System.err.println("Number of Output Files: " + numfiles);
        System.err.println("Output File: " + outputfilename);
        System.err.println("Temp File Directory: " + tempdir);
        
        deleteTempFiles();
        createTempFiles();
        
        
        int totalRuns = 0;
        
        /*
         String absoluteFilePath = inputfilename;
         File file = new File(absoluteFilePath);*/
        
        Heap heap = new Heap(runsize);
        Run run = new Run(runsize);
        Boolean CMark = true;
        
        Scanner sc;
        
        if (inputfilename.equals("notset")) sc = new Scanner(System.in);
        else {
            File inputfile = new File(tempdir + File.separator + inputfilename);
            sc = new Scanner(inputfile);
        }
        String line = "";
        
        // Adds values until the heap is initially full
        while (sc.hasNext() && !heap.isFull()) {
            line = sc.nextLine();
            heap.push(line, CMark);
        }
        
        while (sc.hasNext()) {
            
            // Checks heap if the root of the heap if marked for a new run
            if (heap.peek().Mark != CMark) {
                CMark = !CMark;
                heap.setCMark(!CMark);
                run.tempRun(tempdir);
                totalRuns++;
                run = new Run(runsize);
            }
            
            // Adds the root of the heap to the end of the run, then pushes next values on to the...
            // ...heap with the appropriate mark
            if (run.isEmpty() || (!run.isEmpty() && run.peekLast().compareTo(heap.peek().Key) <= 0)) {
                run.append(heap.pop());
                if (sc.hasNext()) {
                    line = sc.nextLine();
                    if (run.peekLast().compareTo(line) > 0) {
                        heap.push(line, !CMark);
                    }
                    else heap.push(line, CMark);
                }
            }
            
            // Changes the mark of the root of the heap if it can't be added to the run...
            // ...then pushes it back on the heap
            if (!run.isEmpty() && run.peekLast().compareTo(heap.peek().Key) > 0 &&
                heap.peek().Mark == CMark) {
                String s = heap.pop();
                heap.push(s, !CMark);
            }
        }
        
        // Adds the last of the heap to the runList
        if (!heap.isEmpty() && run.peekLast().compareTo(heap.peek().Key) <= 0)
            while (!heap.isEmpty()) {
                if (heap.peek().Mark != CMark){
                    run.tempRun(tempdir);
                    totalRuns++;
                    run = new Run(runsize);
                    CMark = !CMark;
                }
                run.append(heap.pop());
            }
        // creates a new run increments the total runs we have made then continues to create runs
        else {
            run.tempRun(tempdir);
            totalRuns++;
            run = new Run(runsize);
            while (!heap.isEmpty()) run.append(heap.pop());
        }
        
        // Adds the final run to the temp file
        run.tempRun(tempdir);
        totalRuns++;
        
        sc.close();
        
        File finalFile = new File(tempdir + File.separator + "output0.txt");
        PrintWriter writer = new PrintWriter(new FileWriter(finalFile, true));
        writer.close();
        System.err.println("Total runs generated: " + totalRuns);
        fibL = new FibList(numfiles + 1, totalRuns);
        // distributes the runs across the available output files
        System.err.println("Distributing Runs...");
        distributeRuns();
        System.err.println("Starting Sort...");
        // starts the recursive function of polyphase
        polyPhase(fibL.findZero());
    }
    
    /**
     * Distribute runs takes a temp file with all runs and assigns them to available output files
     * per the fib sequence generated for the number of runs in the temp file
     * @throws IOException
     */
    public static void distributeRuns() throws IOException{
        
        // Opens the temp file runs are stored in
        File tempFile = new File(tempdir + File.separator + "temp.txt");
        Scanner sc = new Scanner(tempFile);
        
        // Gets the distribution int [] to used to set the correct amount of runs
        int[] fibNums = new int[numfiles + 1];
        
        // Location by index of the first zero lengthfile
        int zero = fibL.findZero();
        int curFile = 0;
        
        if (zero == 0) { curFile = 1; }
        
        // Opens first file to output runs to
        File currentOutput = new File(tempdir + File.separator + "output" + curFile + ".txt");
        PrintWriter writer = new PrintWriter(new FileWriter(currentOutput, true));
        
        String line;
        System.err.println("Initial distribution of runs: "  + Arrays.toString(fibL.RunLengths));
        
        // While the tempFile isn't empty runs a distributed across the output files
        while(sc.hasNext()) {
            
            line = sc.nextLine();
            
            if (line.equals("*")) {
                writer.println("*");
                fibNums[curFile]++;
                if (fibNums[curFile] == fibL.RunLengths[curFile]){
                    curFile++;
                    if (curFile == zero) {
                        curFile++;
                    }
                    
                    // Opens next output file when correct amount of runs are added to the last
                    currentOutput = new File(tempdir + File.separator + "output" + curFile + ".txt");
                    writer.close();
                    writer = new PrintWriter(new FileWriter(currentOutput, true));
                }
            }
            else {
                writer.println(line);
            }
        }
        
        
        // Creates the correct amount of dummy runs by adding "*" to the currentOutput file
        while(true){
            writer.println("*");
            fibNums[curFile]++;
            
            if (fibNums[curFile] == fibL.RunLengths[curFile]) {
                curFile++;
                if (curFile == zero) curFile++;
                
                // Opens new output file in the correct amount of dummy runs haven't been added yet
                currentOutput = new File(tempdir + File.separator + "output" + curFile + ".txt");
                writer.close();
                writer = new PrintWriter(new FileWriter(currentOutput, true));
            }
            if (curFile > numfiles) break;
        }
        
        // Closes last writer and scanner
        writer.close();
        sc.close();
    }
    
    /**
     *
     *
     * @param filename the file to remove runs from
     * @param Runs how many runs we want to take off the top
     * @throws IOException
     */
    public static void updateOutput(String filename, int Runs) throws IOException{
        
        // Creates a temp file to store the output file in
        File tempFile = new File("tempFile.txt");
        PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
        
        // Load the file at given name
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        String line;
        
        // Count end of run characters until we have reached the given runs to count
        while (Runs != 0) {
            
            if (!sc.hasNext()){ Runs--; }
            else{
                line = sc.nextLine();
                if (line.equals("*")) { Runs--; }
            }
        }
        // write the rest of the file to the tempFile
        while (sc.hasNext()) {
            writer.println(sc.nextLine());
        }
        
        sc.close();
        writer.close();
        
        // load up the original file and overwrite it with the temp file contents.
        File originalfile = new File(filename);
        PrintWriter finalwriter = new PrintWriter(new FileWriter(originalfile));
        Scanner scan = new Scanner(tempFile);
        
        while (scan.hasNext()){
            finalwriter.println(scan.nextLine());
        }
        
        // remove the temp file
        tempFile.delete();
        
        // close the last of the scanners
        scan.close();
        finalwriter.close();
        
    }
    
    
    public static void polyPhase(int zero) throws IOException{
        Heap OutputHeads = new Heap(numfiles);
        List<ScannerNode> scanners = new ArrayList<ScannerNode>();
        
        // Find the file that will be open to input and create a writer for that file
        File input = new File(tempdir + File.separator + "output" + zero + ".txt");
        PrintWriter inputWriter = new PrintWriter(new FileWriter(input));
        
        // Create a scanner for each output file we will be using.
        for (int i = 0; i <= numfiles; i++) {
            File outputFile = new File(tempdir + File.separator + "output" + i + ".txt");
            Scanner sc = new Scanner(outputFile);
            ScannerNode sn = new ScannerNode(sc, i);
            scanners.add(sn);
        }
        
        // Remove text from a file until each file has processed an entire run
        int RunsToRemove = fibL.FibNum[0];
        
        while (RunsToRemove > 0){
            
            RunsToRemove--;
            
            // Create the heap by storing the head of each run that is open for output
            for (ScannerNode sn : scanners){
                if (sn.outputNum == zero){ continue; }
                if (sn.nextValue() != null) OutputHeads.push(sn.value, sn.outputNum);
            }
            
            // Continue while the heap still has values in it
            while(OutputHeads.getSize() != 0){
                
                // polyPop(); returns a Node not just a key, we need this to track what output file it came from
                Node n = OutputHeads.polyPop();
                
                if (!n.Key.equals("*")) { 
                    inputWriter.println(n.Key);
                    inputWriter.flush();
                    for (ScannerNode sn : scanners){
                        if (sn.outputNum == n.Output){
                            OutputHeads.push(sn.nextValue(), sn.outputNum);
                        }
                    }					
                }	
            }
            inputWriter.println("*");
            inputWriter.flush();
            
        }
        
        inputWriter.close();
        
        for (ScannerNode sn : scanners) {
            if (sn.outputNum != zero) {
                updateOutput(tempdir + File.separator + "output" + sn.outputNum + ".txt", fibL.FibNum[0]);
            }
            sn.sc.close();
        }
        
        zero--;
        if(zero < 0) zero = numfiles;
        
        
        
        if(fibL.FibNum[0] == 0) { 
            cleanUp();
            System.err.println("Sort Complete After " + operations + " Passes.");
        }
        else {
            fibL.lastFib();
            operations++;
            System.err.println("Sort Operations Completed: " + operations);
            polyPhase(zero);
        }
    }
    
    
    public static void createTempFiles() throws IOException{
        
        for (int i = 1; i <= numfiles; i++){
            File newFile = new File(tempdir + File.separator + "output" + i + ".txt");
            newFile.createNewFile();
        }		
    }
    
    public static void cleanUp() throws IOException{
        for (int i = 1; i <= numfiles + 1; i++){
            File newFile = new File(tempdir + File.separator + "output" + i + ".txt");
            newFile.delete();
        }
        
        File temp = new File(tempdir + File.separator +  "temp.txt");
        temp.delete();
        
        File file = new File(tempdir + File.separator + "output0.txt");
        File fileToRename = new File(outputfilename);
        file.renameTo(fileToRename);
        
    }
    
    public static void deleteTempFiles() throws IOException{	
        
        for (int i = 0; i <= numfiles; i++){
            File newFile = new File(tempdir + File.separator + "output" + i + ".txt");
            newFile.delete();
        }	
        
        File newFile = new File(tempdir + File.separator + "temp.txt");
        newFile.delete();
    }
}