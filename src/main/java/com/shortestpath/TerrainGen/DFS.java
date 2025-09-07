package main.java.com.shortestpath.TerrainGen;

import main.java.com.shortestpath.core.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DFS implements RandomTerrainGenerator{

    private final Stack<Cell> s;
    private final Movement movement;

    public DFS(Movement movement) {
        this.movement = movement;
        s = new Stack<>();

    }

    @Override
    public void createMaze(Grid grid) {
        grid.blackout();

        int boundRow = grid.getAllCells().stream().mapToInt(Cell::getRow).max().orElse(0);
        int boundCol = grid.getAllCells().stream().mapToInt(Cell::getCol).max().orElse(0);
        int randomRow = ThreadLocalRandom.current().nextInt(boundRow);
        int randomCol = ThreadLocalRandom.current().nextInt(boundCol);

        Cell cell = grid.getCell(randomRow, randomCol);
        cell.setWalkable(true);
        s.push(cell);

        while(!s.isEmpty()) {
            cell = s.peek();

            List<Cell> neighbors = grid.getUnwalkableNeighbors(cell, movement.getDirections());

            if(!neighbors.isEmpty()) {
                Cell nextNeigh = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
                Cell middleCell = grid.getMiddleCell(cell, nextNeigh);

                middleCell.setWalkable(true);
                nextNeigh.setWalkable(true);
                s.push(nextNeigh);
            }else { s.pop(); }
        }
    }
}
