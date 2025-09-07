package main.java.com.shortestpath.ui;

import javafx.scene.layout.Pane;
import main.java.com.shortestpath.TerrainGen.DFS;
import main.java.com.shortestpath.TerrainGen.*;
import main.java.com.shortestpath.algo.*;
import main.java.com.shortestpath.core.*;
import main.java.com.shortestpath.core.Cell;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.java.com.shortestpath.util.Heuristic;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisualizerController {

    @FXML private GridPane gridPane;
    @FXML private ChoiceBox<String> algorithmChoiceBox;
    @FXML private CheckBox movementCheckBox;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private Button terrainButton;
    @FXML private Button generateMazeButton;
    @FXML private Label pathCostLabel, nodesVisitedLabel, timeTakenLabel;
    @FXML private ToggleGroup terrainGroup;
    @FXML private ToggleGroup distCalcGroup;
    @FXML private RadioButton DFSRB;
    @FXML private RadioButton PrimRB;
    @FXML private RadioButton manhattanRB;
    @FXML private RadioButton euclideanRB;

    private static final int GRID_ROWS = 50;
    private static final int GRID_COLS = 50;

    private final Set<Point> toggleDuringDrag = new HashSet<>();

    private GridModel gridModel;
    private GridView gridView;
    private PathfindingAnimator animator;
    private boolean isAnimating = false;
    private boolean startExists = false;
    private boolean targetExists = false;
    String[] terrainSelect = {"Wall", "Sand", "Swamp"};
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        this.gridModel = new GridModel(GRID_ROWS, GRID_COLS);
        this.gridView = new GridView(gridPane, GRID_ROWS, GRID_COLS);
        this.animator = new PathfindingAnimator(gridView);

        setupUIControls();
        setupMouseHandlers();
        resetBoardState();
    }

    private void setupUIControls() {
        algorithmChoiceBox.getItems().addAll("A* Search", "Dijkstra's", "Breadth-First Search");
        algorithmChoiceBox.setValue("A* Search");
        euclideanRB.setToggleGroup(distCalcGroup);
        manhattanRB.setToggleGroup(distCalcGroup);
        DFSRB.setToggleGroup(terrainGroup);
        PrimRB.setToggleGroup(terrainGroup);

    }

    private void setupMouseHandlers() {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                final int r = row;
                final int c = col;
                Pane cellPane = gridView.getCellPane(r, c);

                cellPane.setOnMouseClicked(event -> handleMouseClick(event, r, c));
                cellPane.setOnDragDetected(event -> cellPane.startFullDrag());

                cellPane.setOnMouseDragEntered(event -> {
                    if (!isAnimating && event.getButton() == MouseButton.PRIMARY) {
                        Point currentPoint = new Point(r, c);
                        if (!toggleDuringDrag.contains(currentPoint)) {
                            toggleDuringDrag.add(currentPoint);
                            handleMouseClick(event, r, c);
                        }
                    }
                });
                cellPane.setOnMouseReleased(event -> toggleDuringDrag.clear());
            }
        }
    }


    private void handleMouseClick(MouseEvent event, int row, int col) {
        if (isAnimating) return;
        Point clickedPoint = new Point(row, col);
        Point start = gridModel.getStartPoint();
        Point target = gridModel.getTargetPoint();

        if(event.getButton() == MouseButton.PRIMARY) {
            if(!startExists && !clickedPoint.equals(target)) {
                startExists = true;
                gridModel.setStartPoint(row, col);
                gridView.updateCellUI(row, col, GridView.CellType.START);
            }else if (clickedPoint.equals(start)) {
                startExists = false;
                gridModel.setStartPoint(-1, -1);
                gridView.updateCellUI(row, col, GridView.CellType.EMPTY);
            }else if (!clickedPoint.equals(target)) {
                switch (currentIndex) {
                    case 0 -> {
                        gridModel.toggleWall(row, col);
                        gridView.updateCellUI(row, col, gridModel.getCell(row, col).isWalkable() ? GridView.CellType.EMPTY : GridView.CellType.WALL);
                    }
                    case 1 -> {
                        if(!gridModel.getCell(row, col).isWalkable()) gridModel.toggleWall(row, col);
                        gridModel.setCost(row, col, 5);
                        gridView.updateCellUI(row, col, GridView.CellType.SAND);
                    }
                    case 2 -> {
                        if(!gridModel.getCell(row, col).isWalkable()) gridModel.toggleWall(row, col);
                        gridModel.setCost(row, col, 10);
                        gridView.updateCellUI(row, col, GridView.CellType.SWAMP);
                    }
                }

            }
        }else if(event.getButton() == MouseButton.SECONDARY) {
            if(!targetExists && !clickedPoint.equals(start)) {
                targetExists = true;
                gridModel.setTargetPoint(row, col);
                gridView.updateCellUI(row, col, GridView.CellType.TARGET);
            } else if(clickedPoint.equals(target)) {
                targetExists = false;
                gridModel.setTargetPoint(-1, -1);
                gridView.updateCellUI(row, col, GridView.CellType.EMPTY);
            }
        }
    }

    @FXML
    private void handleStart() {
        if (isAnimating) return;
        if(gridModel.getStartPoint() == null || gridModel.getTargetPoint() == null) return;

        handleClearPath();
        setControlsEnabled(false);
        isAnimating = true;

        Pathfinder pathfinder = createPathfinder();
        long startTime = System.nanoTime();
        pathfinder.findPath(gridModel.getGrid());
        long endTime = System.nanoTime();

        updateStats(pathfinder, (endTime - startTime) / 1_000_000.0);

        animator.play(pathfinder.getVisitedOrder(),
                pathfinder.shortestPath(),
                gridModel.getStartPoint(),
                gridModel.getTargetPoint(),
                () -> {
            isAnimating = false;
            setControlsEnabled(true);
        });
    }

    private Pathfinder createPathfinder() {
        String algoName = algorithmChoiceBox.getValue();
        Heuristic heuristic = null;

        if(algoName.equals("A* Search")) {
            RadioButton selected = (RadioButton) distCalcGroup.getSelectedToggle();

            if(selected.equals(euclideanRB)) {
                heuristic = Heuristic.EUCLIDEAN;
            }else {
                heuristic = Heuristic.MANHATTAN;
            }
        }

        Movement movement = movementCheckBox.isSelected() ? Movement.ALL : Movement.CARDINAL;
        Point start = gridModel.getStartPoint();
        Point target = gridModel.getTargetPoint();

        return switch (algoName) {
            case "Dijkstra's" -> new DijkstraPathfinder(start, target, movement);
            case "Breadth-First Search" -> new BFSPathfinder(start, target, movement);
            default -> new AStarPathfinder(start, target, movement, heuristic);
        };
    }

    private RandomTerrainGenerator createTerrain() {
        RadioButton selected = (RadioButton) terrainGroup.getSelectedToggle();

        if (selected.equals(DFSRB)) {
            return new DFS(Movement.JUMP);
        }else if(selected.equals(PrimRB)){
            return new Prim(Movement.JUMP);
        }else { return null; }
    }

    @FXML
    private void handleTerrain() {
        currentIndex = (currentIndex + 1) % terrainSelect.length;
        terrainButton.setText(terrainSelect[currentIndex]);

    }

    @FXML
    private void handleRandomGenerate() {
        animator.stop();
        isAnimating = false;
        resetBoardState();
        RandomTerrainGenerator rt = createTerrain();
        if(rt != null) {
            rt.createMaze(gridModel.getGrid());
            gridView.updateGridUI(gridModel.getGrid());
        }
        setControlsEnabled(true);
    }

    @FXML
    private void handleResetBoard() {
        animator.stop();
        isAnimating = false;
        resetBoardState();
        resetStats();
        setControlsEnabled(true);
    }

    @FXML
    private void handleClearPath() {
        animator.stop();
        isAnimating = false;
        gridModel.clearPath();
        gridView.clearPathAndVisited();
        gridView.updateGridUI(gridModel.getGrid());
        resetStats();
        setControlsEnabled(true);
    }

    private void resetBoardState() {
        gridModel.reset();
        for(int r = 0; r < GRID_ROWS; r++) {
            for (int c = 0; c < GRID_COLS; c++) {
                gridView.updateCellUI(r, c, GridView.CellType.EMPTY);
            }
        }

        Point start = gridModel.getStartPoint();
        if(start != null && start.getRow() >= 0 && start.getCol() >= 0) {
            gridView.updateCellUI(start.getRow(), start.getCol(), GridView.CellType.START);

        }

        Point target = gridModel.getTargetPoint();
        if(target != null && target.getRow() >= 0 && target.getCol() >= 0) {
            gridView.updateCellUI(target.getRow(), target.getCol(), GridView.CellType.TARGET);
        }
    }

    private void updateStats(Pathfinder pathfinder, double timeMS) {
        timeTakenLabel.setText(String.format("Time: %.2f ms", timeMS));
        nodesVisitedLabel.setText("Nodes Visited: " + pathfinder.getVisitedOrder().size());

        List<Cell> path = pathfinder.shortestPath();
        if (!path.isEmpty()) {
            double cost = (path.stream().mapToDouble(Cell::getCost).sum() - 2);
            pathCostLabel.setText("Path Cost: " + (int) cost);
        } else {
            pathCostLabel.setText("Path Cost: Not Found");
        }
    }

    private void resetStats() {
        timeTakenLabel.setText("Time: N/A");
        nodesVisitedLabel.setText("Nodes Visited: N/A");
        pathCostLabel.setText("Path Cost: N/A");
    }

    private void setControlsEnabled(boolean enabled) {
        startButton.setDisable(!enabled);
        resetButton.setDisable(!enabled);
        algorithmChoiceBox.setDisable(!enabled);
        movementCheckBox.setDisable(!enabled);
        DFSRB.setDisable(!enabled);
        PrimRB.setDisable(!enabled);
        manhattanRB.setDisable(!enabled);
        euclideanRB.setDisable(!enabled);
        generateMazeButton.setDisable(!enabled);
        terrainButton.setDisable(!enabled);

    }
}