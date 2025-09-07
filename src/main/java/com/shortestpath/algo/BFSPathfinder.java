package main.java.com.shortestpath.algo;

import main.java.com.shortestpath.core.*;

import java.util.*;

public class BFSPathfinder implements Pathfinder {
    private final Point startPoint;
    private final Point targetPoint;
    private Cell start;
    private Cell target;
    private final Movement movement;
    private final Queue<Cell> queue;
    private final List<Cell> visitedOrder;
    private final Map<Cell, Cell> restructMap;

    public BFSPathfinder(Point start, Point target, Movement move) {
        this.startPoint = start;
        this.targetPoint = target;
        this.movement = move;
        visitedOrder = new ArrayList<>();
        queue = new ArrayDeque<>();
        restructMap = new HashMap<>();
    }

    @Override
    public void findPath(Grid grid) {
        this.start = grid.getCell(startPoint);
        this.target = grid.getCell(targetPoint);

        queue.clear();
        visitedOrder.clear();
        restructMap.clear();

        restructMap.put(start, null);
        queue.add(start);

        while (!queue.isEmpty()) {
            Cell cell = queue.poll();
            cell.setVisited(true);

            if (cell.equals(target)) break;
            if (!cell.equals(start)) visitedOrder.add(cell);

            for (Cell neigh : grid.getNeighbors(cell, movement.getDirections())) {
                if (!neigh.isVisited() && !restructMap.containsKey(neigh)) {
                    restructMap.put(neigh, cell);
                    queue.add(neigh);
                }
            }
        }
    }

    @Override
    public List<Cell> shortestPath(){
        List<Cell> path = new ArrayList<>();
        Cell current = target;

        if(!restructMap.containsKey(current)) return path;
        while(current != null){
            path.add(current);
            current = restructMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    @Override
    public List<Cell> getVisitedOrder() {
        return visitedOrder;
    }
}

