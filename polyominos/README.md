# Polyominos

## Problem:
Not really a logic puzzle. But caught my attention on a programming contest question. 
We just want to know how many unique blocks you can make out of `N` squares.

Unique means that every block has different shape. (even after rotation)
For instance all blocks in the game tetris consist out of 4 squares. 7 in total.
```
XXXX
----
X
XXX
----
 X
XXX
----
  X
XXX
----
XX
 XX
----
XX
XX
----
X
XX
 X
```


## Program:
### IN:
 * N (number of squares)
```
12
```
### OUT:
```
Time: 814ms
There are 126759 blocks of size 12
```

I also added the possibility to print them all out. Which is not very interesting for `N > 10`.

So far not able to compute for `N >= 15`

For `N = 14`, time: 16473ms

ref: 
 - [WIKI](https://en.wikipedia.org/wiki/Polyomino)
 - [StackOverflow](http://stackoverflow.com/questions/4650762/programming-contest-question-counting-polyominos/34003753#34003753)
 - [OEIS](https://oeis.org/A000988/list)
 - [VPW](http://www.vlaamseprogrammeerwedstrijd.be/2015/oefenvragen.php)
