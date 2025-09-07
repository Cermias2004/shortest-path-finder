package main.java.com.shortestpath.util;

import main.java.com.shortestpath.core.Grid;
import java.io.IOException;

public interface GridLoader {
    Grid load(String filename) throws IOException;
}
