package main.java.com.shortestpath.core;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UPLEFT(-1, -1),
    UPRIGHT(-1, 1),
    DOWNLEFT(1, -1),
    DOWNRIGHT(1, 1),
    UPUP(-2, 0),
    DOWNDOWN(2, 0),
    LEFTLEFT(0, -2),
    RIGHTRIGHT(0, 2);

    private final int rowOffset;
    private final int colOffset;

    Direction(int rowOffset, int colOffset){
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    public int getRowOffset(){
        return rowOffset;
    }

    public int getColOffset(){
        return colOffset;
    }

    public static Direction[] cardinalDirections(){
        return new Direction[]{UP, DOWN, LEFT, RIGHT};
    }

    public static Direction[] allDirections() {
        return new Direction[] {UP, DOWN, LEFT, RIGHT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT};
    }

    public static Direction[] skipOne() {
        return new Direction[] {UPUP, DOWNDOWN, LEFTLEFT, RIGHTRIGHT};
    }
}
