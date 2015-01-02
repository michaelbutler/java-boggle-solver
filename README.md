java-boggle-solver
==================

Boggle solver written in Java.

Currently it does not support inputting the boggle grid (unfortunately). It simply generates a real boggle grid at random, and then solves it. It would be a trivial matter to accept the boggle grid as a command-line parameter and input into the memory before the algorithm runs.

Supports parallel processing to search the boggle grid for words. Filters the dictionary prior to searching by using the available letters on the boggle grid.

Other optimizations are possible, but it's pretty good for now.

License
==================
GPLv2
