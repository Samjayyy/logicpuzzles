# 8-puzzle

## Problem:
```
 5  7  4 
 3  X  8 
 1  6  2 
 
 1  2  3 
 4  5  6 
 7  8  X 
```
Slide the numbers on to the empty place untill they are all in the right position. As in the example above.
The solutions contains both an IDA* and A* implementation. At the start the parity is checked if it's solvable or not.

## Program:
### IN:
 * R C
 * R rows with C numbers describing the board. (0 = Empty place, numbers 1 to (R*C)-1)
```
3 3
 5  7  4 
 3  0  8 
 1  6  2 
```
### OUT:
```
Time: 65ms
>> Step 0
 5  7  4 
 3  X  8 
 1  6  2 

>> Step 1
 5  7  4 
 3  8  X 
 1  6  2 

>> Step 2
 5  7  4 
 3  8  2 
 1  6  X 

>> Step 3
 5  7  4 
 3  8  2 
 1  X  6 

>> Step 4
 5  7  4 
 3  X  2 
 1  8  6 

>> Step 5
 5  X  4 
 3  7  2 
 1  8  6 

>> Step 6
 5  4  X 
 3  7  2 
 1  8  6 

>> Step 7
 5  4  2 
 3  7  X 
 1  8  6 

>> Step 8
 5  4  2 
 3  7  6 
 1  8  X 

>> Step 9
 5  4  2 
 3  7  6 
 1  X  8 

>> Step 10
 5  4  2 
 3  X  6 
 1  7  8 

>> Step 11
 5  4  2 
 X  3  6 
 1  7  8 

>> Step 12
 5  4  2 
 1  3  6 
 X  7  8 

>> Step 13
 5  4  2 
 1  3  6 
 7  X  8 

>> Step 14
 5  4  2 
 1  3  6 
 7  8  X 

>> Step 15
 5  4  2 
 1  3  X 
 7  8  6 

>> Step 16
 5  4  2 
 1  X  3 
 7  8  6 

>> Step 17
 5  X  2 
 1  4  3 
 7  8  6 

>> Step 18
 X  5  2 
 1  4  3 
 7  8  6 

>> Step 19
 1  5  2 
 X  4  3 
 7  8  6 

>> Step 20
 1  5  2 
 4  X  3 
 7  8  6 

>> Step 21
 1  X  2 
 4  5  3 
 7  8  6 

>> Step 22
 1  2  X 
 4  5  3 
 7  8  6 

>> Step 23
 1  2  3 
 4  5  X 
 7  8  6 

>> Step 24
 1  2  3 
 4  5  6 
 7  8  X 
```

ref: [Wiki](https://en.wikipedia.org/wiki/15_puzzle)
