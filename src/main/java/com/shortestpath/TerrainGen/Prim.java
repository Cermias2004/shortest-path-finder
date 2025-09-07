package main.java.com.shortestpath.TerrainGen;

import main.java.com.shortestpath.core.*;
import main.java.com.shortestpath.ui.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class Prim implements RandomTerrainGenerator{
    private final Movement movement;

    public Prim(Movement movement) {
        this.movement = movement;
    }
    @Override
    public void createMaze(Grid grid) {
        grid.blackout();

        int boundRow = grid.getAllCells().stream().mapToInt(Cell::getRow).max().orElse(0);
        int boundCol = grid.getAllCells().stream().mapToInt(Cell::getCol).max().orElse(0);
        int randomRow = ThreadLocalRandom.current().nextInt(boundRow);
        int randomCol = ThreadLocalRandom.current().nextInt(boundCol);

        Cell current = grid.getCell(randomRow, randomCol);
        current.setWalkable(true);

        List<Cell> frontier = new ArrayList<>(grid.getUnwalkableNeighbors(current, movement.getDirections()));

        while(!frontier.isEmpty()) {
            Cell frontierCell = frontier.remove(ThreadLocalRandom.current().nextInt(frontier.size()));
            List<Cell> passageNeighbors = grid.getNeighbors(frontierCell, movement.getDirections());

            if(passageNeighbors.isEmpty()) continue;

            Cell neighbor = passageNeighbors.get(ThreadLocalRandom.current().nextInt(passageNeighbors.size()));
            Cell middleCell = grid.getMiddleCell(frontierCell, neighbor);
            middleCell.setWalkable(true);
            frontierCell.setWalkable(true);


            frontier.addAll(grid.getUnwalkableNeighbors(frontierCell, movement.getDirections()));
        }
    }
}
