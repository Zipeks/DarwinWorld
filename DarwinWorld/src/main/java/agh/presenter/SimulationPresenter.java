package agh.presenter;

import agh.Simulation;
import agh.SimulationEngine;
import agh.model.*;
import agh.model.MapChangeListener;
import agh.model.util.Boundary;
import agh.model.util.SimulationConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Objects;

public class SimulationPresenter implements MapChangeListener {
    private static final int CELL_WIDTH = 65;
    private static final int CELL_HEIGHT = 65;
    private static final int BORDER_WIDTH = 2;
    private static final int BORDER_OFFSET = BORDER_WIDTH / 2;
    private JungleMap jungleMap;
    private SimulationConfig config;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private Label moveInfoLabel;
//    private final Font NotoEmojiFont;

//    public SimulationPresenter() {
//        String fontPath = Objects.requireNonNull(getClass().getResource("/NotoEmoji-VariableFont_wght.ttf")).toExternalForm();
//        NotoEmojiFont = Font.loadFont(fontPath, 40);
//    }

    public void setJungleMap(JungleMap jungleMap) {
        this.jungleMap = jungleMap;
    }
    public void drawMap() {
        Boundary bounds = jungleMap.getCurrentBounds();
        Boundary jungleBounds=jungleMap.getJungle();
        int numCols = bounds.topRight().getX() - bounds.bottomLeft().getX() + 1;
        int numRows = bounds.topRight().getY() - bounds.bottomLeft().getY() + 1;
        int colSize=1280/numCols;
        int rowSize=640/numRows;
        int cellSize=Math.max(8,Math.min(colSize,rowSize));
        int canvasWidth = cellSize * numCols + BORDER_WIDTH;
        int canvasHeight = cellSize * numRows + BORDER_WIDTH;
        mapCanvas.setWidth(canvasWidth);
        mapCanvas.setHeight(canvasHeight);
        clearGrid();

        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();

        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(2);

        IO.println("DZUNGLA");
        IO.println(jungleBounds);

        //Dżungla
        graphics.setFill(Color.DARKOLIVEGREEN);
        graphics.fillRect(0, (bounds.topRight().getY()-jungleBounds.topRight().getY())*cellSize,mapCanvas.getWidth(),(jungleBounds.topRight().getY()-jungleBounds.bottomLeft().getY()+1)*cellSize);

//         Pionowe kreski
//        for (int i = 0; i <= numCols; i++) {
//            double x = i * cellSize + BORDER_OFFSET;
//            graphics.strokeLine(x, 0, x, canvasHeight);
//        }
//        // Poziome kreski
//        for (int i = 0; i <= numRows; i++) {
//            double y = i * cellSize + BORDER_OFFSET;
//            graphics.strokeLine(0, y, canvasWidth, y);
//        }



        double halfCell = ((double) cellSize / 2);
        int fontSize = (int) (cellSize * 0.9);
        configureFont(graphics, new Font("Arial", fontSize), Color.BLACK);
        jungleMap.getElements().forEach(element -> {
            Vector2d position = element.getPosition();
            int xIndex = position.getX() - bounds.bottomLeft().getX();
            int yIndex = position.getY() - bounds.bottomLeft().getY();

            double xOnCanvas = (xIndex) * cellSize + halfCell;
            double yOnCanvas = mapCanvas.getHeight() - ((yIndex) * cellSize) - halfCell;

            if (element instanceof Animal) {
                graphics.save();
                graphics.translate(xOnCanvas, yOnCanvas);

                MapDirection mapDirection = ((Animal) element).getDirection();
                int rotation = mapDirection.ordinal() * 90;

                graphics.rotate(rotation);
//                graphics.setFont(new Font(cellSize-2));
//                configureFont(graphics, this.NotoEmojiFont, Color.BROWN);
                graphics.fillText("@", 0, 0);
                graphics.restore();
            } else {
//                configureFont(graphics, this.NotoEmojiFont, Color.ORANGE);
//                graphics.fillText("🍯", xOnCanvas, yOnCanvas);
                graphics.fillText("#", xOnCanvas, yOnCanvas);
            }
        });
    }
//    public void drawMap() {
//        Boundary bounds = jungleMap.getCurrentBounds();
//        Boundary jungleBounds=jungleMap.getJungle();
//        int numCols = bounds.topRight().getX() - bounds.bottomLeft().getX() + 1;
//        int numRows = bounds.topRight().getY() - bounds.bottomLeft().getY() + 1;
//        int canvasWidth = CELL_WIDTH * numCols + BORDER_WIDTH;
//        int canvasHeight = CELL_HEIGHT * numRows + BORDER_WIDTH;
//        mapCanvas.setWidth(canvasWidth);
//        mapCanvas.setHeight(canvasHeight);
//        clearGrid();
//
//        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
//
//        graphics.setStroke(Color.BLACK);
//        graphics.setLineWidth(2);
//
//        IO.println(jungleBounds);
//
//        //Dżungla
//        graphics.setFill(Color.DARKOLIVEGREEN);
//        graphics.fillRect(0, jungleBounds.topRight().getY()*CELL_HEIGHT,mapCanvas.getWidth(),(jungleBounds.topRight().getY()-jungleBounds.topRight().getY()+1)*CELL_HEIGHT);
//
//        // Pionowe kreski
//        for (int i = 0; i <= numCols; i++) {
//            double x = i * CELL_WIDTH + BORDER_OFFSET;
//            graphics.strokeLine(x, 0, x, canvasHeight);
//        }
//        // Poziome kreski
//        for (int i = 0; i <= numRows; i++) {
//            double y = i * CELL_HEIGHT + BORDER_OFFSET;
//            graphics.strokeLine(0, y, canvasWidth, y);
//        }
//
//
//
//        double halfWidth = ((double) CELL_WIDTH / 2);
//        double halfHeight = ((double) CELL_HEIGHT / 2);
//
//        configureFont(graphics, new Font("Arial", 30), Color.BLACK);
//        jungleMap.getElements().forEach(element -> {
//            Vector2d position = element.getPosition();
//            int xIndex = position.getX() - bounds.bottomLeft().getX();
//            int yIndex = position.getY() - bounds.bottomLeft().getY();
//
//            double xOnCanvas = (xIndex) * CELL_WIDTH + halfWidth;
//            double yOnCanvas = mapCanvas.getHeight() - ((yIndex) * CELL_HEIGHT) - halfHeight;
//
//            if (element instanceof Animal) {
//                graphics.save();
//                graphics.translate(xOnCanvas, yOnCanvas);
//
//                MapDirection mapDirection = ((Animal) element).getDirection();
//                int rotation = mapDirection.ordinal() * 90;
//
//                graphics.rotate(rotation);
//
////                configureFont(graphics, this.NotoEmojiFont, Color.BROWN);
//                graphics.fillText("@", 0, 0);
//                graphics.restore();
//            } else {
////                configureFont(graphics, this.NotoEmojiFont, Color.ORANGE);
////                graphics.fillText("🍯", xOnCanvas, yOnCanvas);
//                graphics.fillText("#", xOnCanvas, yOnCanvas);
//            }
//        });
//    }

    private void configureFont(GraphicsContext graphics, Font font, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(font);
        graphics.setFill(color);
    }

    private void clearGrid() {
        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setFill(Color.LIGHTGOLDENRODYELLOW);
        graphics.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        //            moveInfoLabel.setText(message);
        Platform.runLater(this::drawMap);
    }
//
    public void setConfig(SimulationConfig config){
        this.config=config;
        start();
    }
//
    public void start() {
        try {
            drawMap();
//            simulation.run();
//            List<Simulation> simulations = List.of(simulation);
//            SimulationEngine simulationEngine = new SimulationEngine();
//
//            simulationEngine.runAsyncInThreadPool();
//            IO.println(config.mapWidth());
//            IO.println(config.mapHeight());

//            List<Vector2d> positions = List.of(new Vector2d(5, 5), new Vector2d(5, 4), new Vector2d(3, 3));
//            drawMap();
//            Simulation simulation = new Simulation(positions, directions, worldMap);
//            List<Simulation> simulations = List.of(simulation);
//            SimulationEngine simulationEngine = new SimulationEngine(simulations);

//            simulationEngine.runAsyncInThreadPool();
//        } catch (IncorrectPositionException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid position");
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
        } catch (IllegalArgumentException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid moves");
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
        }
    }
}
