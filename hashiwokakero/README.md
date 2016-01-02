# Hashiwokakero

## Problem:
Hashiwokakero (橋をかけろ Hashi o kakero; lit. "build bridges!") is a type of logic puzzle published by Nikoli.
It has also been published in English under the name Bridges or Chopsticks (based on a mistranslation: the hashi of the title, 橋, means bridge; hashi written with another character, 箸, means chopsticks). 
It has also appeared in The Times under the name Hashi. In France, Denmark, the Netherlands, and Belgium it is published under the name Ai-Ki-Ai.

Hashiwokakero is played on a rectangular grid with no standard size, although the grid itself is not usually drawn. Some cells start out with (usually encircled) numbers from 1 to 8 inclusive; these are the islands. The rest of the cells are empty.

The goal is to connect all of the islands by drawing a series of bridges between the islands. The bridges must follow certain criteria:

- They must begin and end at distinct islands, travelling a straight line in between.
- They must not cross any other bridges or islands.
- They may only run orthogonally (parallel to the grid edges).
- At most two bridges connect a pair of islands.
- The number of bridges connected to each island must match the number on that island.
- The bridges must connect the islands into a single connected group.

###For Example:

![Example puzzle](https://upload.wikimedia.org/wikipedia/commons/d/d4/Val42-Bridge1n.png)

###Example solved:

![Example solved](https://upload.wikimedia.org/wikipedia/commons/f/f6/Val42-Bridge1.png)


## Program:
### IN:
 * R C (Size of the board)
 * R lines with C columns
 * Character . is empty or number X for the number of connected bridges 
```
25 25
2.....5..........3....3..
.......3....4..2........1
2.....5..........1.......
........1....2........4..
4.....6..................
........1...3........3..3
3...2..5......5.....3....
......4.4....4.....1.....
.4.3..........2.....2...4
......2......3.....2.....
...1....4............4...
.6..6.1..................
........................4
.........................
....3.1..................
...................1.....
.........................
.6..4.1..................
...1....3........2.......
....2.1..................
.4.4.1..2..2.1...3.3.....
4.6....8.............4...
.........................
1..1...5..4......3...1...
..4...................4.3

```
### OUT:
```
Time: 9ms

2=====5----------3----3..
......#3====4==2.|....#.1
2-----5|.........1....#.|
|.....#|1----2--------4.|
4=====6|..............|.|
|.....#|1---3========3|.3
3---2.#5======5-----3||.#
|...|.4#4====4#....1#||.#
|4=3|.###....#2....|2||.4
|#.||.2##....3-----2.||.#
|#.1|..#4------------4|.#
|6==6-1#|............#|.#
|#..#..#|............#|.4
|#..#..#|............#|.#
|#..3-1#|............#|.#
|#.....#|..........1.#|.#
|#.....#|..........|.#|.#
|6==4-1#|..........|.#|.#
|#.1|..#3--------2.|.#|.#
|#.|2-1#|........|.|.#|.#
|4=4-1.#2--2-1...3=3.#|.#
4=6====8=============4|.#
|.#....#..............|.#
1.#1---5==4======3---1|.#
..4===================4-3
```

ref: 
 - [WIKI](https://en.wikipedia.org/wiki/Hashiwokakero)
 - [Puzzles](http://puzzle-bridges.com)
