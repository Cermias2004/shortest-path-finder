package main.java.com.shortestpath;

import javafx.application.Application;
import main.java.com.shortestpath.algo.*;
import main.java.com.shortestpath.core.*;
import main.java.com.shortestpath.ui.PathfindingVisualizerApp;
import main.java.com.shortestpath.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.awt.Color.*;

public class Main {

    enum Mode { JSON, FILE }
    enum Algorithm { ASTAR, BFS, DIJKSTRA }
    enum HeuristicType { MANHATTAN, EUCLIDEAN }

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            Application.launch(PathfindingVisualizerApp.class, args);
            return;
        }

        if (args.length < 3) {
            throw new IllegalArgumentException(
                    "Usage: <mode: json|file> <filename> <algorithm: astar|bfs|dijkstra> " +
                            "[heuristic: manhattan|euclidean] [dia]"
            );
        }

        Mode mode = parseMode(args[0]);
        String filename = args[1];
        Algorithm algo = parseAlgorithm(args[2]);

        Grid grid;
        switch (mode) {
            case JSON -> grid = new LoadJSON().load(filename);
            case FILE -> grid = new LoadFile().load(filename);
            default -> throw new IllegalStateException("Unexpected mode: " + mode);
        }

        Pathfinder pathfinder = switch (algo) {
            case ASTAR -> createAStar(grid, args);
            case DIJKSTRA -> createDijkstra(grid, args);
            case BFS -> createBFS(grid, args);
        };

        pathfinder.findPath(grid);
        ExportToImage(grid, pathfinder.shortestPath(), pathfinder.getVisitedOrder(), filename);
    }

    private static Pathfinder createAStar(Grid grid, String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("A* requires a heuristic: manhattan or euclidean");
        }

        HeuristicType heuristic = parseHeuristic(args[3]);
        Movement movement = Movement.CARDINAL;

        if (args.length > 4 && args[4].equalsIgnoreCase("dia")) {
            movement = Movement.ALL;
        } else if (args.length > 4) {
            throw new IllegalArgumentException("Invalid movement option: " + args[4] + ". Use 'dia' or omit.");
        }

        return switch (heuristic) {
            case MANHATTAN -> new AStarPathfinder(grid.getStart(), grid.getTarget(), movement, Heuristic.MANHATTAN);
            case EUCLIDEAN -> new AStarPathfinder(grid.getStart(), grid.getTarget(), movement, Heuristic.EUCLIDEAN);
        };
    }

    private static Pathfinder createDijkstra(Grid grid, String[] args) {
        Movement movement = Movement.CARDINAL;
        if (args.length > 3) {
            if (args[3].equalsIgnoreCase("dia")) {
                movement = Movement.ALL;
            } else {
                throw new IllegalArgumentException("Invalid movement option: " + args[3] + ". Use 'dia' or omit.");
            }
        }
        return new DijkstraPathfinder(grid.getStart(), grid.getTarget(), movement);
    }

    private static Pathfinder createBFS(Grid grid, String[] args) {
        Movement movement = Movement.CARDINAL;
        if (args.length > 3) {
            if (args[3].equalsIgnoreCase("dia")) {
                movement = Movement.ALL;
            } else {
                throw new IllegalArgumentException("Invalid movement option: " + args[3] + ". Use 'dia' or omit.");
            }
        }
        return new BFSPathfinder(grid.getStart(), grid.getTarget(), movement);
    }

    private static void ExportToImage(Grid grid, List<Cell> path, List<Cell> visited, String filename) throws IOException {
        int cellSize = 20;
        int maxRow = grid.getAllCells().stream().mapToInt(Cell::getRow).max().orElse(0);
        int maxCol = grid.getAllCells().stream().mapToInt(Cell::getCol).max().orElse(0);

        BufferedImage image = new BufferedImage(
                (maxCol + 1) * cellSize,
                (maxRow + 1) * cellSize,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Optimize lookups with sets
        Set<Cell> pathSet = new HashSet<>(path);
        Set<Cell> visitedSet = new HashSet<>(visited);

        for (Cell c : grid.getAllCells()) {
            int x = c.getCol() * cellSize;
            int y = c.getRow() * cellSize;

            if (!c.isWalkable()) {
                graphics.setColor(Color.decode("#212121")); // walls
            } else if (pathSet.contains(c)) {
                graphics.setColor(Color.decode("#FFEB3B")); // path
            } else if (visitedSet.contains(c)) {
                graphics.setColor(Color.decode("#add8e6")); // visited
            } else {
                graphics.setColor(LIGHT_GRAY); // empty
            }

            if(c.equals(grid.getCell(grid.getStart()))) graphics.setColor(Color.decode("#4CAF50"));
            if(c.equals(grid.getCell(grid.getTarget()))) graphics.setColor(Color.decode("#F44336"));

            graphics.fillRect(x, y, cellSize, cellSize);
            graphics.setColor(DARK_GRAY);
            graphics.drawRect(x, y, cellSize, cellSize);
        }
        graphics.dispose();

        File outputDir = new File("output");
        if (!outputDir.exists()) outputDir.mkdir();

        String outputFileName = filename.replaceAll("\\.(txt|json)$", ".png");

        File outputFile = new File(outputDir, new File(outputFileName).getName());
        ImageIO.write(image, "png", outputFile);
        System.out.println("Image saved to: " + outputFile.getAbsolutePath());
    }

    private static Mode parseMode(String arg) {
        return switch (arg.toLowerCase()) {
            case "json" -> Mode.JSON;
            case "file" -> Mode.FILE;
            default -> throw new IllegalArgumentException("Invalid mode: " + arg + ". Use 'json' or 'file'.");
        };
    }

    private static Algorithm parseAlgorithm(String arg) {
        return switch (arg.toLowerCase()) {
            case "astar" -> Algorithm.ASTAR;
            case "bfs" -> Algorithm.BFS;
            case "dijkstra" -> Algorithm.DIJKSTRA;
            default -> throw new IllegalArgumentException("Invalid algorithm: " + arg + ". Use astar, bfs, or dijkstra.");
        };
    }

    private static HeuristicType parseHeuristic(String arg) {
        return switch (arg.toLowerCase()) {
            case "manhattan" -> HeuristicType.MANHATTAN;
            case "euclidean" -> HeuristicType.EUCLIDEAN;
            default -> throw new IllegalArgumentException("Invalid heuristic: " + arg + ". Use 'manhattan' or 'euclidean'.");
        };
    }
}
