# Milestone 6 - Refactoring Report

**Team members:** Jalaj Signh & Megan Larson

**Github team/repo:** CS4500-S21/Laressea


## Plan
We want to change List<ArrayList<Tile> tiles to Tile[][] in the Room class for more efficiency. It is also more consistent with our design in level.


## Changes

In order to change the tile field in Room from List<ArrayList<Tile> tiles to Tile[][], we needed to update the Room class and the methods renderRoom, toString, and getNextPossibleCardinalMove. We also amended our testing data in testUtils and the testing harness testRoom.
Code clean up! Added in missing docs, removed old imports, removed duplicate methods, and substituted in helper methods where needed

## Future Work

Code cleanup is an ongoing process
More work will no doubt reveal itself in coming Milestones

## Conclusion

This refactor week was a great idea!
