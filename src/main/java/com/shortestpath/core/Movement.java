package main.java.com.shortestpath.core;

public enum Movement {
    CARDINAL(Direction.cardinalDirections()),
    ALL(Direction.allDirections()),
    JUMP(Direction.skipOne());

    private final Direction[] directions;

    Movement(Direction[] dirs) {
        this.directions = dirs;
    }

    public Direction[] getDirections() {
        return directions;
    }
}
