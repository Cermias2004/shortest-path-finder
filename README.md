# Shortest Path Finder

A JavaFX-based pathfinding algorithm that demonstrates how different algorithms (A*, Dijkstra's, BFS) find the shortest path through a grid with obstacles and varied terrain costs.

## Features

- **Multiple Pathfinding Algorithms**: A* Search, Dijkstra's Algorithm, Breadth-First Search
- **Interactive Gird Editior**: Click and drag to create walls, terrain, start/target points
- **Real-time Animation**: Watch algorithms explore the grid step-by-step
- **Terrain Types**: Walls, Sand (cost: 5), Swamp (cost: 10)
- **Maze Generation**: Random Maze Generation using Prim's Algorithm and Depth-First Search
- **Movement Options**: 4-Directional or 8-Directional (diagonal) movement
- **Multiple Input Formats**: Support for both text files and JSON input
- **Image Export**: Generate PNG visualization of pathfinding results
- **Performance Statistics**: Path cost, cells visited, and execution time

## Prerequisites

- **Java 17** (recommended: Java 17.0.16)
- No need to install Gradle - the project includes the Gradle wrapper

## Quick Start

### Option 1: Using Git Command Line:

(If you have Git Bash installed)
```bash
   git clone https://github.com/Cermias2004/shortest-path-finder.git
   cd shortest-path-finder
```
### Option 2: Without Git

#### GitHub Desktop Users:

1. Download and install GitHub Desktop
2. Click "Clone a repository from the Internet"
3. Enter the repository URL: https://github.com/Cermias2004/shortest-path-finder.git
4. Choose your local path and click "Clone"

#### Manual Download:

1. Go to https://github.com/Cermias2004/shortest-path-finder
2. Click the green "Code" button
3. Select "Download ZIP"
4. Extract the ZIP file to your desired location
5. Open a terminal/command prompt in the extracted folder

### Running the Application

Once you have the project files, run the GUI application:

```bash
# Windows (Command Prompt or PowerShell)
gradlew.bat run

# Mac/Linux (Terminal)
./gradlew run
```

**Note for Windows users without Git Bash:** Use Command Prompt or PowerShell instead of Git Bash. The `gradlew.bat` file works in these environments.

That's it! The interactive visualizer will launch immediately.

## Using the GUI

1. **Place Start Point**: Left-click on the grid (creates green 'S')
2. **Place Target Point**: Right-click on the grid (creates red 'T')
3. **Draw Walls/Terrain**: Left-click and drag to create obstacles
4. **Select Algorithm**: Choose from dropdown (A*, Dijkstra's, BFS)
5. **Configure Options**:
   - Check "Diagonals" for 8-directional movement
   - Select heuristic (Manhattan/Euclidean) for A*
6. **Click "Visualize"**: Watch the pathfinding animation

### Additional GUI Controls

- **Terrain Button**: Cycle between Wall/Sand/Swamp terrain types
- **Generate Maze**: Create random mazes using DFS or Prim's algorithms
- **Clear Path**: Remove path visualization while keeping obstacles
- **Reset Board**: Clear entire grid

### Visual Legend

- **Green (S)**: Start point
- **Red (T)**: Target point
- **Black**: Walls (impassable)
- **Light Blue**: Visited cells during search
- **Yellow**: Final shortest path
- **Beige**: Sand terrain (movement cost: 5)
- **Dark Green**: Swamp terrain (movement cost: 10)

## Command Line Usage

For processing input files and generating PNG visualizations.

### File Input Format

Create input files with these symbols:
- `s` - Start point
- `t` - Target point
- `.` - Empty cell (cost: 1)
- `x` - Wall (impassable)
- `integer` - Terrain with movement cost

Example `maze.txt`:
```
s . . x . . t
. x . x . . .
. x . x x x .
. . . . . . .
```

### JSON Input Format

Example `maze.json`:
```json
{
  "start": [0, 0],
  "target": [0, 6],
  "tokens": [
    ["s", ".", ".", "x", ".", ".", "t"],
    [".", "x", ".", "x", ".", ".", "."],
    [".", "x", ".", "x", "x", "x", "."],
    [".", ".", ".", ".", ".", ".", "."]
  ]
}
```

### Running Command Line Mode

**Important**: Use absolute file paths or place input files in the project root directory.

```bash
# Text file input (use full path to file)
./gradlew run --args="file C:/path/to/your/maze.txt astar manhattan"
./gradlew run --args="file /Users/username/maze.txt dijkstra dia"

# JSON file input (use full path to file)  
./gradlew run --args="json C:/path/to/your/maze.json astar euclidean"

# If file is in project directory, you can use just the filename
./gradlew run --args="file maze.txt bfs"
./gradlew run --args="json puzzle.json dijkstra dia"
```

**For Windows users without Git Bash**, replace `./gradlew` with `gradlew.bat`:

```cmd
gradlew.bat run --args="file maze.txt astar manhattan"
gradlew.bat run --args="json puzzle.json dijkstra dia"
```

#### Command Parameters

- **Algorithm**: `astar`, `dijkstra`, `bfs`
- **Heuristic** (A* only): `manhattan`, `euclidean`
- **Movement** (optional): `dia` for diagonal movement

#### Examples

```bash
# A* with Manhattan heuristic
./gradlew run --args="file maze.txt astar manhattan"

# Dijkstra's with diagonal movement
./gradlew run --args="file grid.txt dijkstra dia"

# BFS (unweighted search)
./gradlew run --args="json puzzle.json bfs"

# A* with Euclidean heuristic and diagonals
./gradlew run --args="file terrain.txt astar euclidean dia"
```

**Output**: PNG images are saved to the `output/` directory with pathfinding visualization.

## Algorithm Comparison

| Algorithm | Optimality | Speed | Memory Usage | Best Use Case |
|-----------|------------|-------|--------------|---------------|
| A* | Optimal | Fast | Moderate | General pathfinding with heuristics |
| Dijkstra's | Optimal | Slower | High | Weighted graphs, guaranteed shortest path |
| BFS | Optimal* | Fast | High | Unweighted graphs only |

*BFS is only optimal for unweighted graphs where all moves have equal cost.

### Heuristic Comparison

- **Manhattan Distance**: Better for grid-based movement (4-directional)
- **Euclidean Distance**: Better when diagonal movement is allowed

## Project Structure

```
src/main/java/com/shortestpath/
├── Main.java                     # Entry point and CLI handler
├── algo/                         # Pathfinding algorithms
│   ├── AStarPathfinder.java
│   ├── BFSPathfinder.java  
│   ├── DijkstraPathfinder.java
│   └── Pathfinder.java
├── core/                         # Core data structures
│   ├── Cell.java                 # Grid cell with cost/walkability
│   ├── Direction.java            # Movement directions  
│   ├── Grid.java                 # Grid management
│   ├── Movement.java             # Movement patterns
│   └── Point.java                # Coordinate system
├── TerrainGen/                   # Maze generation algorithms
│   ├── DFS.java                  # Depth-first search maze
│   └── Prim.java                 # Prim's algorithm maze
├── ui/                           # JavaFX user interface
│   ├── GridModel.java            # Grid data model
│   ├── GridView.java             # Visual representation
│   ├── PathfindingAnimator.java  # Animation controller
│   ├── PathfindingVisualizerApp.java
│   └── VisualizerController.java
└── util/                         # File I/O and utilities
    ├── GridData.java             # JSON data structure
    ├── GridLoader.java           # File loading interface
    ├── GridPrinter.java          # Console output
    ├── Heuristic.java            # Distance calculations
    ├── LoadFile.java             # Text file parser
    └── LoadJSON.java             # JSON file parser
src/main/resources/com/shortestpath/
├── visualizer.fxml
```

## Building from Source

The project uses Gradle with the wrapper included:

```bash
# Build the project  
./gradlew build

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean
```

**Windows users without Git Bash:**
```cmd
gradlew.bat build
gradlew.bat test
gradlew.bat clean
```

## Dependencies

- **JavaFX 17.0.2**: GUI framework
- **Gson 2.10.1**: JSON parsing library
- **JUnit 5**: Testing framework (development only)

## Troubleshooting

### Java Version Issues
```bash
java -version
# Should show Java 17 or higher
```

### File Path Issues (Command Line)
- Use absolute paths: `C:/Users/username/maze.txt`
- Or place files in the project root directory
- Check that the file exists and is readable

### GUI Won't Launch
- Ensure Java 17+ is installed
- Check that JavaFX modules are available
- Try running: `./gradlew clean build run`

### Build Failures
- Run: `./gradlew clean`
- Ensure stable internet connection (for dependency downloads)
- Check Java version compatibility

### For Users Without Git or Command Line Experience

If you're uncomfortable with command line tools:

1. **Use an IDE like IntelliJ IDEA or Eclipse:**
   - Import the project as a Gradle project
   - Use the IDE's built-in terminal or run configurations

2. **Use VS Code:**
   - Install the Java Extension Pack
   - Open the project folder
   - Use the integrated terminal for commands

3. **Alternative Terminals:**
   - **Windows**: Use PowerShell or Command Prompt instead of Git Bash
   - **Mac**: Use the built-in Terminal application
   - **Linux**: Use your distribution's terminal application

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes and test thoroughly
4. Submit a pull request with a clear description

## License

This project is open source. Feel free to use, modify, and distribute according to your needs.
