package main.java.com.shortestpath.util;

import main.java.com.shortestpath.core.Cell;

public enum Heuristic {
    MANHATTAN {
        @Override
        public double calculate(Cell from, Cell to) {
            return Math.abs(from.getRow() - to.getRow()) + Math.abs(from.getCol() - to.getCol());
        }
    },
    EUCLIDEAN {
        @Override
        public double calculate(Cell from, Cell to) {
            return Math.hypot(from.getRow() - to.getRow(), from.getCol() - to.getCol());
        }
    };

    public abstract double calculate(Cell from, Cell to);
}
