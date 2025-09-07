package main.java.com.shortestpath.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Grid;

public class GridView {

    private final Pane[][] cellPanes;
    private final int rows;
    private final int cols;
    private static final int CELL_SIZE = 20;

    public GridView(GridPane gridPane, int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cellPanes = new Pane[rows][cols];
        initializeGrid(gridPane);
    }

    private void initializeGrid(GridPane gridPane) {
        gridPane.getChildren().clear();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Pane cellPane = new Pane();
                cellPane.setPrefSize(CELL_SIZE, CELL_SIZE);
                gridPane.add(cellPane, col, row);
                cellPanes[row][col] = cellPane;
            }
        }
    }

    public void updateGridUI(Grid grid) {
        for(Cell c :  grid.getAllCells()) {
            if(c == null) continue;
            if (c.equals(grid.getCell(grid.getStart()))) {
                updateCellUI(c.getRow(), c.getCol(), GridView.CellType.START);
            } else if (c.equals(grid.getCell(grid.getTarget()))) {
                updateCellUI(c.getRow(), c.getCol(), GridView.CellType.TARGET);
            } else if (!c.isWalkable()) {
                updateCellUI(c.getRow(), c.getCol(), GridView.CellType.WALL);
            }else if(c.getCost() == 5) {
                updateCellUI(c.getRow(), c.getCol(), GridView.CellType.SAND);
            }else if(c.getCost() == 10){
                updateCellUI(c.getRow(), c.getCol(), GridView.CellType.SWAMP);
            }else{
                updateCellUI(c.getRow(), c.getCol(), GridView.CellType.EMPTY);
            }
        }
    }

    public Pane getCellPane(int row, int col) {
        return cellPanes[row][col];
    }

    public void updateCellUI(int row, int col, CellType type) {
        cellPanes[row][col].setStyle(type.getStyle());
    }

    public void clearPathAndVisited() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String currentStyle = cellPanes[row][col].getStyle();
                if (currentStyle.contains(CellType.VISITED.getColor()) || currentStyle.contains(CellType.PATH.getColor())) {
                    cellPanes[row][col].setStyle(GridView.CellType.EMPTY.getStyle());
                }
            }
        }
    }

    public enum CellType {
        START("#4CAF50"),
        TARGET("#F44336"),
        WALL("#212121"),
        SWAMP("#556B2F"),
        SAND("#EDC9AF"),
        VISITED("#add8e6"),
        PATH("#FFEB3B"),
        EMPTY("#fafafa");

        private final String color;

        CellType(String color) { this.color = color; }

        public String getColor() { return color; }

        public String getStyle() {
            return "-fx-border-color: #444; -fx-background-color: " + color + ";";
        }
    }
}