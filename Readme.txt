Description: Implement an external polyphase sort-merge program for sorting large files. The program must be written in Java (unless prior consent for another language is obtained from the marking tutor), compile and run on machines in Lab 6 within R Block. This assignment must be done in pairs, so find a partner or ask the tutor to help you find one. A forum will also be opened on Moodle to assist in finding partners.

Input/Output: Your program should run from the command-line (i.e. no GUI) with the command xsort and accept the following command-line arguments:

-r runsize — where "runsize" is a positive integer not less than 7 that specifies the minimum run length (i.e. heap size) to be used for creating the initial runs, and the runs are to be created using the replacement selection strategy. If this argument is omitted by the user then the default should be 7.
-k numfiles — where "numfiles" is the total number of temporary files to be used for input and output, with a default value of 7 if this argument is omitted by the user.
-d tempdir — where "tempdir" is the name of the directory where temporary files are created. If this argument is omitted, the default should be the current directory.
-o outputfilename — where outputfilename is the name of the file where the final sorted data is to be stored. If omitted then final output is to be standard output.
inputfilename — which is the name of the file containing the data to be sorted. If omitted, then data is obtained from standard input.
Each line of input is one datum, and the output must be the entire input after having been sorted line-by-line in ascending dictionary order (i.e. as per the judgment given by a standard library string comparison function). In addition, your program must print to standard error the following performance metrics:
the total number of initial runs and their initial distribution prior to the first pass.
the total number of passes to achieve the final sort (i.e. the total number of times a file is opened for output).
Priority queue: You must implement your own (min)heap/priority-queue class. Do not use a library class for this. Your heap class should be used for creating initial runs and merging runs on each pass. How the initial runs are to be distributed is left as an exercise to the student.