package main.java.com.shortestpath.ui;

import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import main.java.com.shortestpath.core.Cell;
import main.java.com.shortestpath.core.Point;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;

public class PathfindingAnimator {

    private final GridView gridView;
    private SequentialTransition animation;

    public PathfindingAnimator(GridView gridView) {
        this.gridView = gridView;
    }

    public void play(List<Cell> visited, List<Cell> path, Point start, Point target, Runnable onFinished) {
        List<Cell> visitedToAnimate = visited.stream()
                .filter(cell -> {
                    Point currentPoint = new Point(cell.getRow(), cell.getCol());
                    return !currentPoint.equals(start) && !currentPoint.equals(target);
                })
                .collect(Collectors.toList());

        Timeline visitedAnimation = createAnimation(visitedToAnimate, GridView.CellType.VISITED, 10);

        List<Cell> pathOnly;
        if(path.size() <= 2) {
            pathOnly = Collections.emptyList();
        }else {
            pathOnly = path.subList(1, path.size() - 1);
        }

        Timeline pathAnimation = createAnimation(pathOnly, GridView.CellType.PATH, 40);

        animation = new SequentialTransition(visitedAnimation, pathAnimation);
        animation.setOnFinished(e -> onFinished.run());
        animation.play();
    }

    private Timeline createAnimation(List<Cell> cells, GridView.CellType type, int durationMs) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            double time  = (i + 1) * durationMs;
            KeyFrame kf = new KeyFrame(Duration.millis(time), e -> {
                gridView.updateCellUI(cell.getRow(), cell.getCol(), type);
            });
            timeline.getKeyFrames().add(kf);
        }
        return timeline;
    }

    public void stop() {
        if (animation != null) {
            animation.stop();
        }
    }
}