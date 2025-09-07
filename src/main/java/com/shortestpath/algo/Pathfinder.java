package main.java.com.shortestpath.algo;

import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Grid;

import java.util.List;

public interface Pathfinder {
    void findPath(Grid grid);
    List<Cell> shortestPath();
    List<Cell> getVisitedOrder();
}
