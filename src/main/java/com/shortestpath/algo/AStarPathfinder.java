package main.java.com.shortestpath.algo;

import main.java.com.shortestpath.core.*;
import main.java.com.shortestpath.util.Heuristic;

import java.util.*;

public class AStarPathfinder implements Pathfinder {
    private final Point startPoint;
    private final Point targetPoint;
    private Cell start;
    private Cell target;
    private final Movement movement;
    private final Queue<Cell> pq;
    private final List<Cell> visitedOrder;
    private final Map<Cell, Cell> restructMap;
    private final Map<Cell, Double> costSoFar;
    private final Map<Cell, Double> priorityMap;
    private final Heuristic heuristic;


    public AStarPathfinder(Point start, Point target, Movement move, Heuristic heuristic){
        this.startPoint = start;
        this.targetPoint = target;
        this.movement = move;
        this.heuristic = heuristic;
        costSoFar = new HashMap<>();
        priorityMap = new HashMap<>();
        visitedOrder = new ArrayList<>();
        pq = new PriorityQueue<>(Comparator.comparingDouble(priorityMap :: get));
        restructMap = new HashMap<>();
    }

    @Override
    public void findPath(Grid grid) {
        this.start = grid.getCell(startPoint);
        this.target = grid.getCell(targetPoint);

        pq.clear();
        restructMap.clear();
        costSoFar.clear();
        priorityMap.clear();
        visitedOrder.clear();

        costSoFar.put(start, 0.0);
        priorityMap.put(start, this.heuristic.calculate(start, target));
        restructMap.put(start, null);
        pq.add(start);

        while(!pq.isEmpty()){
            Cell cell = pq.poll();

            if(cell.equals(target)) break;

            if(!cell.equals(start)) visitedOrder.add(cell);

            for(Cell neigh : grid.getNeighbors(cell, movement.getDirections())) {
                double newCost = costSoFar.get(cell) + neigh.getCost();
                if(!costSoFar.containsKey(neigh) || newCost < costSoFar.get(neigh)) {
                    priorityMap.put(neigh, newCost + this.heuristic.calculate(neigh, target));
                    costSoFar.put(neigh, newCost);
                    restructMap.put(neigh, cell);
                    pq.add(neigh);
                }
            }
        }
    }

    @Override
    public List<Cell> shortestPath(){
        List<Cell> path = new ArrayList<>();
        Cell current = target;
        if(!restructMap.containsKey(target)) return path;

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