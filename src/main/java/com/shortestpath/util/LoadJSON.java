package main.java.com.shortestpath.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Grid;
import main.java.com.shortestpath.core.Point;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class LoadJSON implements GridLoader {

    private final Gson gson;

    public LoadJSON() {
        this.gson = new Gson();
    }

    @Override
    public Grid load(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("File Name can not be null or empty.");
        }

        try {
            GridData data = parseJson(filename);
            return buildGrid(data);
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON grid file: " + filename, e);
        }
    }

    private GridData parseJson(String filename) throws IOException {
        try (Reader reader = new FileReader(filename)) {
            GridData data = gson.fromJson(reader, GridData.class);
            if (data == null) {
                throw new IllegalArgumentException("Invalid JSON Format -- could not parse grid data");
            }

            validateData(data);
            return data;
        } catch (JsonSyntaxException e) {
            throw new IOException("Invalid JSON syntax in file: " + filename, e);
        } catch (JsonIOException e) {
            throw new IOException("JSON parsing error in file: " + filename, e);
        }
    }

    private Grid buildGrid(GridData data) {
        int rows = data.tokens.length;
        int cols = data.tokens[0].length;

        if(data.start[0] < 0 || data.start[0] >= rows || data.start[1] < 0 || data.start[1] >= cols) {
            throw new IllegalArgumentException (
                    "Invalid start points: (" + data.start[0] + ", " + data.start[1] + ")");
        }

        if(data.target[0] < 0 || data.target[0] >= rows || data.target[1] < 0 || data.target[1] >= cols) {
            throw new IllegalArgumentException (
                    "Invalid target points: (" + data.target[0] + ", " + data.target[1] + ")");
        }

        Point start = new Point(data.start[0], data.start[1]);
        Point target = new Point(data.target[0], data.target[1]);
        Grid grid = new Grid(start, target);

        for (int row = 0; row < data.tokens.length; row++) {
            for (int col = 0; col < data.tokens[row].length; col++) {

                String rawToken = data.tokens[row][col];
                if (rawToken == null) {
                    throw new IllegalArgumentException("Token can not be null at [" + row + ", " + col + "]");
                }
                String token = rawToken.toLowerCase();

                grid.addCell(createCell(token, row, col));
            }
        }
        return grid;
    }

    private Cell createCell(String token, int row, int col) {
        Cell cell;
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
                        throw new IllegalArgumentException("Token can not be negative at: [" + row + ", " + col + "]: " + cost);
                    }
                    return new Cell(row, col, cost, true);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Invalid cost value at (" + row + ", " + col + "): " + token);
                }
        }
    }

    private void validateData(GridData data) {
        if (data.start == null || data.start.length != 2) {
            throw new IllegalArgumentException("Invalid or missing start point in JSON");
        }
        if (data.target == null || data.target.length != 2) {
            throw new IllegalArgumentException("Invalid or missing target point in JSON");
        }
        if (data.tokens == null || data.tokens.length == 0) {
            throw new IllegalArgumentException("Invalid or missing tokens in grid in JSON");
        }

        int expectedCols = data.tokens[0].length;
        for (int row = 0; row < data.tokens.length; row++) {
            if (data.tokens[row] == null) {
                throw new IllegalArgumentException("Null row at index: " + row);
            }
            if (data.tokens[row].length != expectedCols) {
                throw new IllegalArgumentException("Inconsistent row length at: " + row);
            }
        }
    }
}