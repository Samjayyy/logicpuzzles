# Nonogram

## Problem:
Nonograms, also known as Hanjie, Picross or Griddlers, are picture logic puzzles in which cells in a grid must be colored or left blank according to numbers at the side of the grid to reveal a hidden picture. In this puzzle type, the numbers are a form of discrete tomography that measures how many unbroken lines of filled-in squares there are in any given row or column. For example, a clue of "4 8 3" would mean there are sets of four, eight, and three filled squares, in that order, with at least one blank square between successive groups.

These puzzles are often black and white, describing a binary image, but they can also be colored. If colored, the number clues are also colored to indicate the color of the squares. Two differently colored numbers may have a space in between them. For example, a black four followed by a red two could mean four black boxes, some empty spaces, and two red boxes, or it could simply mean four black boxes followed immediately by two red ones.

Nonograms have no theoretical limits on size, and are not restricted to square layouts.

For Example:

![Example puzzle](http://www.gchq.gov.uk/SiteCollectionImages/grid-shading-puzzle.jpg)


## Program:
### IN:
 * R C (Size of the board)
 * R lines (for each row top to bottom) space separated numbers with the block sizes in sequence (left to right)
 * C lines (for each col left to right) space separated numbers with the block sizes in sequence (top to bottom)
 * R lines with C characters describing the start board. With 0 = UNKNOWN, # = FILLED

```
25 25
7 3 1 1 7
1 1 2 2 1 1
1 3 1 3 1 1 3 1
1 3 1 1 6 1 3 1
1 3 1 5 2 1 3 1
1 1 2 1 1
7 1 1 1 1 1 7
3 3
1 2 3 1 1 3 1 1 2
1 1 3 2 1 1
4 1 4 2 1 2
1 1 1 1 1 4 1 3
2 1 1 1 2 5
3 2 2 6 3 1
1 9 1 1 2 1
2 1 2 2 3 1
3 1 1 1 1 5 1
1 2 2 5
7 1 2 1 1 1 3
1 1 2 1 2 2 1
1 3 1 4 5 1
1 3 1 3 10 2
1 3 1 1 6 6
1 1 2 1 1 2
7 2 1 2 5
7 2 1 1 7
1 1 2 2 1 1
1 3 1 3 1 3 1 3 1
1 3 1 1 5 1 3 1
1 3 1 1 4 1 3 1
1 1 1 2 1 1
7 1 1 1 1 1 7
1 1 3
2 1 2 1 8 2 1
2 2 1 2 1 1 1 2
1 7 3 2 1
1 2 3 1 1 1 1 1
4 1 1 2 6
3 3 1 1 1 3 1
1 2 5 2 2
2 2 1 1 1 1 1 2 1
1 3 3 2 1 8 1
6 2 1
7 1 4 1 1 3
1 1 1 1 4
1 3 1 3 7 1
1 3 1 1 1 2 1 1 4
1 3 1 4 3 3
1 1 2 2 2 6 1
7 1 3 2 1 1
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
000##0000000##0000000#000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
000000##00#000##00#000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
000000#0000#0000#000#0000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
000##0000##0000#0000##000
0000000000000000000000000
0000000000000000000000000
0000000000000000000000000
```
### OUT:
```
Time: 46ms
#######0###000#0#0#######
#00000#0##0##00000#00000#
#0###0#00000###0#0#0###0#
#0###0#0#00######0#0###0#
#0###0#00#####0##0#0###0#
#00000#00##0000000#00000#
#######0#0#0#0#0#0#######
00000000###000###00000000
#0##0###00#0#0###0#00#0##
#0#000000###0##0000#000#0
0####0#0####0##0#0000##00
0#0#000#000#0#0####0#0###
00##00#0#0#000000##0#####
000###0##0##0######0###0#
#0#########0#0#00##0000#0
0##0#00##000##0###00000#0
###0#0#0#00#0000#####0#00
00000000#000##0##000#####
#######0#00##000#0#0#0###
#00000#0##00#00##000##0#0
#0###0#000####00#####00#0
#0###0#0###0##########0##
#0###0#0#00######0######0
#00000#00##000000#0#0##00
#######0##000#0##000#####
```

For now the column size is limited to the number of bits in signed long (=63)

ref: 
 - [WIKI](https://en.wikipedia.org/wiki/Nonogram)
 - [StackOverflow](http://stackoverflow.com/questions/34469538/efficient-nonogram-solver)
 - [Puzzles](http://www.puzzle-nonograms.com/)
