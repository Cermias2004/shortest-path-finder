package main.java.com.shortestpath.ui;

import main.java.com.shortestpath.core.*;

// Manages the grid's data and state
public class GridModel {

    private final int rows;
    private final int cols;
    private Grid grid;
    private Point startPoint;
    private Point targetPoint;

    public GridModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.startPoint = null;
        this.targetPoint = null;
        reset();
    }

    public void setCost(int row, int col, double cost) {
        grid.getCell(row, col).setCost(cost);
    }

    public void setStartPoint(int row, int col) {
        if(row == -1 && col == -1) {
            startPoint = null;
        }else {
            grid.setStart(row, col);
            this.startPoint = new Point(row, col);
        }
    }

    public void setTargetPoint(int row, int col) {
        if(row == -1 && col == -1) {
            targetPoint = null;
        }else {
            grid.setTarget(row, col);
            this.targetPoint = new Point(row, col);
        }
    }

    public void reset() {
        this.grid = new Grid(startPoint, targetPoint);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid.addCell(new Cell(row, col));
            }
        }
    }

    public void toggleWall(int row, int col) {
        Cell cell = grid.getCell(row, col);
        cell.setWalkable(!cell.isWalkable());
    }

    public void clearPath() {
        grid.resetVisited();
    }

    public Grid getGrid() { return grid; }
    public Point getStartPoint() { return startPoint; }
    public Point getTargetPoint() { return targetPoint; }
    public Cell getCell(int row, int col) { return grid.getCell(row, col); }
}