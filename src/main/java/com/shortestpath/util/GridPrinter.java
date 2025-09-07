package main.java.com.shortestpath.util;

import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Grid;
import main.java.com.shortestpath.core.Point;

import java.util.*;

//Used for debugging
public class GridPrinter {

    public void printGrid(Grid grid, List<Cell> path) {
        Set<Cell> pathSet = new HashSet<>(path);
        Point start = grid.getStart();
        Point target = grid.getTarget();

        int maxRow = 0;
        int maxCol = 0;
        for(Cell c : grid.getAllCells()) {
            maxRow = Math.max(maxRow, c.getRow());
            maxCol = Math.max(maxCol, c.getCol());
        }

        for(int row = 0; row <= maxRow; row++) {
            for(int col = 0; col <= maxCol; col++) {
                if(!grid.hasCell(row, col)) {
                    System.out.print(" ");
                    continue;
                }

                Cell c = grid.getCell(row, col);
                Point p = new Point(row, col);

                if(p.equals(start)) {
                    System.out.print("S");
                }else if(p.equals(target)) {
                    System.out.print("T");

                }else if(pathSet.contains(c)) {
                    System.out.print("#");
                }else {
                    System.out.print(c.toString());
                }
            }
            System.out.println();
        }
    }
}
