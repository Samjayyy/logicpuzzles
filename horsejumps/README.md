# Horse jumps

## Problem:
```
OOOO
OOOO
OOOO
#OO#
```
You can choose where the horse starts. Jump on each tile exactly once using L-jumps. Like a horse in chess.

## Program:
### IN:
 * L number of lines
 * L lines containing a valid board using characters (# = No board, O = Tile)
```
4
OOOO
OOOO
OOOO
#OO#
```
### OUT:
```
Time: 32ms
XXXX
XXXX
XXXX
#XX#
------------------
OXXX
XXXX
XXXX
#XX#
------------------
OXXX
XXXX
XOXX
#XX#
------------------
OXXX
XXXO
XOXX
#XX#
------------------
OOXX
XXXO
XOXX
#XX#
------------------
OOXX
XXXO
XOOX
#XX#
------------------
OOXO
XXXO
XOOX
#XX#
------------------
OOXO
XOXO
XOOX
#XX#
------------------
OOXO
XOXO
XOOO
#XX#
------------------
OOOO
XOXO
XOOO
#XX#
------------------
OOOO
OOXO
XOOO
#XX#
------------------
OOOO
OOXO
XOOO
#OX#
------------------
OOOO
OOOO
XOOO
#OX#
------------------
OOOO
OOOO
OOOO
#OX#
```

