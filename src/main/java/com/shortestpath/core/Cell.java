package main.java.com.shortestpath.core;

import java.util.Objects;

public class Cell {
    private final int row;
    private final int col;
    private double cost;
    private boolean walkable;
    private boolean visited;

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        this.cost = 1.0;
        this.walkable = true;
    }

    public Cell(int row, int col, int cost, boolean walkable){
        this.row = row;
        this.col = col;
        this.walkable = walkable;
        this.cost = walkable ? cost : Double.POSITIVE_INFINITY;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public double getCost(){
        return cost;
    }

    public boolean isWalkable(){
        return walkable;
    }

    public void setWalkable(boolean walkable){
        this.walkable = walkable;
        if(!walkable){
            this.cost = Double.POSITIVE_INFINITY;
        }else if(this.cost == Double.POSITIVE_INFINITY){
            this.cost = 1.0;
        }
    }

    public boolean isVisited(){
        return visited;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    public void setCost(double cost){
        if(!walkable){
            throw new IllegalStateException("Can not set cost on non-Walkable Cell");
        }
        if(cost <= 0){
            throw new IllegalStateException("Cost must be greater than zero");
        }
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        // Check if it's the exact same object instance
        if (this == o) return true;

        // Check if the other object is null or a different class
        if (o == null || getClass() != o.getClass()) return false;

        // Cast the object and compare the fields that define equality
        Cell cell = (Cell) o;
        return this.row == cell.row && this.col == cell.col;
    }

    @Override
    public int hashCode() {
        // Generate a hash code from the same fields used in equals()
        return Objects.hash(this.row, this.col);
    }

    public String toString(){
        if(!walkable) return "X";
        if(cost == 1.0) return ".";
        return String.valueOf((int) cost);
    }
}
