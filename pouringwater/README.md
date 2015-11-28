# Pouring water

## Problem:
You have three jugs:

 - A 10-liter jug, filled with water
 - A 7-liter jug, empty
 - A 3-liter jug, empty

Your objective is to end up having 5 liters of water in the 10-liter jug, and 5 liters of water in the 7-liter jug.
Note that you have nothing else at your disposal other than these three jugs and that you cannot perform measurements by eye or based on the shape of the jugs.

## Program:
### IN:
 * N R (where N is the number of glasses, R is the expected result)
 * N numbers with size of each glass
 * N numbers with initial size
```
3 5
10 7 3
10 0 0
```
### OUT:
```
Time: 18ms
[2, 5, 3]
------------------
[2, 7, 1]
------------------
[9, 0, 1]
------------------
[9, 1, 0]
------------------
[6, 1, 3]
------------------
[6, 4, 0]
------------------
[3, 4, 3]
------------------
[3, 7, 0]
------------------
[10, 0, 0]
------------------
> Solution found in 9 steps.
```

ref: [Braingle](http://www.braingle.com/brainteasers/teaser.php?op=2&id=14200&comm=0)
