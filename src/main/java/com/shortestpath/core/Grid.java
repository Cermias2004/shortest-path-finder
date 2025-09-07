package main.java.com.shortestpath.core;

import java.util.*;

public class Grid {
    private final Map<Point, Cell> gridMap;
    private Point start;
    private Point target;

    public Grid(Point start, Point target){
        this.start = start;
        this.target = target;
        gridMap = new HashMap<>();
    }

    public void addCell(Cell cell){
        Point point = new Point(cell.getRow(), cell.getCol());
        gridMap.put(point, cell);
    }

    public Cell getCell(Point point){
        return gridMap.get(point);
    }

    public Cell getCell(int row, int col){
        return gridMap.get(new Point(row, col));
    }

    public boolean hasCell(int row, int col){
        return gridMap.containsKey(new Point(row, col));
    }

    public void setStart(int row, int col){
        this.start = new Point(row, col);
    }

    public void setTarget(int row, int col){
        this.target = new Point(row, col);
    }

    public Point getStart(){
        return start;
    }

    public Point getTarget(){
        return target;
    }

    public void resetVisited() {
        for(Cell c : getAllCells()) {
            c.setVisited(false);
        }
    }

    public void blackout() {
        for(Cell c : getAllCells()){
            Point p = new Point(c.getRow(), c.getCol());
            if (!p.equals(getStart()) && !p.equals(getTarget())) {
                c.setWalkable(false);
            }
        }
    }

    public List <Cell> getNeighbors(Cell cell, Direction[] directions) {
        List <Cell> neighbors = new ArrayList<>();
        for(Direction dir : directions){
            int neighRow = cell.getRow() + dir.getRowOffset();
            int neighCol = cell.getCol() + dir.getColOffset();
            Point neighPoint = new Point(neighRow, neighCol);

            Cell neighCell = gridMap.get(neighPoint);

            if (neighCell != null && neighCell.isWalkable()) {
                neighbors.add(neighCell);
            }
        }
        return neighbors;
    }

    public List <Cell> getUnwalkableNeighbors(Cell cell, Direction[] directions) {
        List <Cell> neighbors = new ArrayList<>();
        for(Direction dir : directions){
            int neighRow = cell.getRow() + dir.getRowOffset();
            int neighCol = cell.getCol() + dir.getColOffset();
            Point neighPoint = new Point(neighRow, neighCol);

            Cell neighCell = gridMap.get(neighPoint);

            if (neighCell != null && !neighCell.isWalkable()) {
                neighbors.add(neighCell);
            }
        }
        return neighbors;
    }

    public Cell getMiddleCell(Cell cell, Cell neigh) {
        return getCell(((cell.getRow() + neigh.getRow()) / 2), ((cell.getCol() + neigh.getCol()) / 2));
    }

    public Collection<Cell> getAllCells(){
        return Collections.unmodifiableCollection(gridMap.values());
    }


}
