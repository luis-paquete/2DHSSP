This C implementation solves the Hypervolume Subset Selection problem for two 
dimensions.


Building:

The program can be compiled using the command line, if you have java sdk 1.7 installed. 
You need to invoke the compile script, inside the project directory.


Usage:

You can run it using the run script, with the following syntax:

./run <cardinality> <input_file>

Cardinality is the number of points that the algorithm must choose and input_file is the path to the file with the points to be used (starts with one line with 'n', the number of points, followed by n lines, each one with two integers, x_i and y_i). The reference point considered for calculations is (0,0).
Output includes result (maximum hypervolume value that is possible to obtain) and time spent.
