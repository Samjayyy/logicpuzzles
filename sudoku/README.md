# Sudoku

## Problem:
You are given a 9 by 9 grid. You should fill all empty spaces with numbers from 1 to 9.

Constraints:
 - all numbers in a row should be distinct
 - all numbers in a column should be distinct
 - all numbers in the blocks of 3x3 should be distinct
 
Example:
```
1 2 3 4 5 6 7 8 9 
4 5 6 7 8 9 1 2 3 
7 8 9 1 2 3 4 5 6 
2 1 4 3 6 5 8 9 7 
3 6 5 8 9 7 2 1 4 
8 9 7 2 1 4 3 6 5 
5 3 1 6 4 2 9 7 8 
6 4 2 9 7 8 5 3 1 
9 7 8 5 3 1 6 4 2
```


 
## Program:
### IN:
 * 9 lines with 9 numbers. Representing the board. 0 if it's an unknown number.
```
0 7 0 2 3 8 0 0 0
0 0 0 7 4 0 8 0 9
0 6 8 1 0 9 0 0 2
0 3 5 4 0 0 0 0 8
6 0 7 8 0 2 5 0 1
8 0 0 0 0 5 7 6 0
2 0 0 6 0 3 1 9 0
7 0 9 0 2 1 0 0 0
0 0 0 9 7 4 0 8 0
```
### OUT:
```
Time: 1ms
9 7 1 2 3 8 4 5 6 
5 2 3 7 4 6 8 1 9 
4 6 8 1 5 9 3 7 2 
1 3 5 4 6 7 9 2 8 
6 4 7 8 9 2 5 3 1 
8 9 2 3 1 5 7 6 4 
2 5 4 6 8 3 1 9 7 
7 8 9 5 2 1 6 4 3 
3 1 6 9 7 4 2 8 5 
```


ref: [Wiki](https://en.wikipedia.org/wiki/Sudoku)
