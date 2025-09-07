package main.java.com.shortestpath.util;

import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Grid;
import main.java.com.shortestpath.core.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadFile implements GridLoader {

    @Override
    public Grid load(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename can not be empty or null");
        }

        try {
            return parseFile(filename);
        } catch (Exception e) {
            throw new IOException("Failed to parse file '" + filename + "' ", e);
        }
    }

    public Grid parseFile(String filename) throws IOException {
        List<Cell> cells = new ArrayList<>();
        Point start = null;
        Point target = null;
        Grid grid;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] tokens = line.trim().split("\\s+");

                for (int col = 0; col < tokens.length; col++) {
                    String rawToken = tokens[col];

                    if(rawToken == null){
                        throw new IllegalArgumentException("Null token at: (" + row + ", " + col + ")");
                    }

                    String token = rawToken.toLowerCase();
                    Cell cell = createCell(token, row, col);

                    if ("s".equals(token)) {
                        start = new Point(row, col);
                    }
                    if ("t".equals(token)) {
                        target = new Point(row, col);
                    }
                    cells.add(cell);
                }
                row++;
            }
            validateData(cells, start, target);
            grid = new Grid(start, target);
            for (Cell c : cells) {
                grid.addCell(c);
            }
            return grid;
        }
    }

    private void validateData(List<Cell> cells, Point start, Point target) {
        if (cells.isEmpty()) {
            throw new IllegalArgumentException("File is empty or has no valid grid data.");
        }
        if (start == null) {
            throw new IllegalArgumentException("Start point 's' not found in grid.");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target point 't' not found in grid.");
        }
    }

    private static Cell createCell(String token, int row, int col) {
        switch (token) {
            case "s":
            case "t":
            case ".":
                return new Cell(row, col);
            case "x":
                return new Cell(row, col, 0, false);
            default:
                try {
                    int cost = Integer.parseInt(token);
                    if (cost < 0) {
                        throw new IllegalArgumentException("Negative token at: [" + row + ", " + col + "]: " + cost);
                    }
                    return new Cell(row, col, cost, true);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid token '" + token + "' at: [" + row + ", " + col + "]");
                }
        }
    }
}