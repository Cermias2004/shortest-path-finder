package main.java.com.shortestpath.algo;

import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Grid;
import main.java.com.shortestpath.core.Movement;
import main.java.com.shortestpath.core.Point;

import java.util.*;

public class DijkstraPathfinder implements Pathfinder {
    private final Point startPoint;
    private final Point targetPoint;
    private Cell start;
    private Cell target;
    private final Movement movement;
    private final Queue<Cell> pq;
    private final List<Cell> visitedOrder;
    private final Map<Cell, Cell> restructMap;
    private final Map<Cell, Double> distMap;

    public DijkstraPathfinder(Point start, Point target, Movement move) {
        this.startPoint = start;
        this.targetPoint = target;
        this.movement = move;
        restructMap = new HashMap<>();
        visitedOrder = new ArrayList<>();
        distMap = new HashMap<>();
        pq = new PriorityQueue<>(Comparator.comparingDouble(distMap :: get));
    }

    @Override
    public void findPath(Grid grid) {
        this.start = grid.getCell(startPoint);
        this.target = grid.getCell(targetPoint);

        pq.clear();
        visitedOrder.clear();
        restructMap.clear();
        distMap.clear();

        pq.add(start);
        restructMap.put(start, null);
        distMap.put(start, 0.0);

        while (!pq.isEmpty()){
            Cell cell = pq.poll();

            if(cell.equals(target)) break;
            if(!cell.equals(start)) visitedOrder.add(cell);

            for(Cell neigh : grid.getNeighbors(cell, movement.getDirections())) {
                double newCost = distMap.get(cell) + neigh.getCost();
                if(!distMap.containsKey(neigh) || newCost < distMap.get(neigh)){
                    distMap.put(neigh, newCost);
                    restructMap.put(neigh, cell);
                    pq.offer(neigh);
                }
            }
        }
    }

    @Override
    public List<Cell> shortestPath() {
        List<Cell> path = new ArrayList<>();
        Cell current = target;

        if(!restructMap.containsKey(current)) return path;

        while(current != null) {
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
