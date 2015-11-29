# Hourglasses

## Problem:
With a 7-minute hourglass and an 11-minute hourglass, can you explain the quickest way to time a boiling egg for 15 minutes?

## Program:
### IN:
 * N T (where N is number of hourglasses and T is the time to meet)
 * N numbers with the time it takes to complete
```
2 15
7 11
```

### OUT:

```
Time: 5ms
[15, 0, 7]
------------------
[11, 4, 11]
------------------
[11, 4, 0]
------------------
[11, 3, 0]
------------------
[7, 7, 4]
------------------
[7, 0, 4]
------------------
[0, 7, 11]
------------------
[0, 7, 0]
------------------
[0, 0, 0]
------------------
> Solution found in 9 steps.
```
[timeleft, time left for the resting sand of hourglass1, hourglass2]

Note that when we turn the hourglass of size 7 after 3 minutes, it becomes 4 minutes.

ref: [Braingle](http://www.braingle.com/brainteasers/29696/hour-glass.html)
