package agh.presenter;

import agh.model.*;
import agh.model.MapChangeListener;

import java.util.List;
import java.util.Objects;

public class SimulationPresenter implements MapChangeListener {
    @Override
    public void mapChanged(WorldMap worldMap, String message) {

    }
//    private WorldMap worldMap;
//    @FXML
//    private Canvas mapCanvas;
//    @FXML
//    private TextField movesTextField;
//    @FXML
//    private Label moveInfoLabel;
//    private static final int CELL_WIDTH = 65;
//    private static final int CELL_HEIGHT = 65;
//    private static final int BORDER_WIDTH = 2;
//    private static final int BORDER_OFFSET = BORDER_WIDTH / 2;
//    private final Font NotoEmojiFont;

//    public SimulationPresenter() {
//        String fontPath = Objects.requireNonNull(getClass().getResource("/NotoEmoji-VariableFont_wght.ttf")).toExternalForm();
//        NotoEmojiFont = Font.loadFont(fontPath, 40);
//    }
//
//    public void setWorldMap(WorldMap worldMap) {
//        this.worldMap = worldMap;
//    }
//
//    public void drawMap() {
//        Boundary bounds = worldMap.getCurrentBounds();
//        int numCols = bounds.topRight().getX() - bounds.bottomLeft().getX() + 2;
//        int numRows = bounds.topRight().getY() - bounds.bottomLeft().getY() + 2;
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
//        double halfWidth = ((double) CELL_WIDTH / 2);
//        double halfHeight = ((double) CELL_HEIGHT / 2);
//
//        configureFont(graphics, new Font("Arial", 30), Color.BLACK);
//        graphics.fillText("y/x", CELL_WIDTH - halfWidth, canvasHeight - halfHeight);
//        // Koordynaty X
//        for (int i = 0; i <= numCols; i++) {
//            String labelVal = String.valueOf(bounds.bottomLeft().getX() + i);
//            graphics.fillText(labelVal, (i + 1) * CELL_WIDTH + halfWidth, canvasHeight - halfHeight);
//        }
//        // Koordynaty Y
//        for (int i = 0; i < numRows; i++) {
//            String labelVal = String.valueOf(bounds.bottomLeft().getY() + i);
//            graphics.fillText(labelVal, halfWidth, canvasHeight - ((i + 1) * CELL_HEIGHT) - halfHeight);
//        }
//
//        worldMap.getElements().forEach(element -> {
//            Vector2d position = element.getPosition();
//            int xIndex = position.getX() - bounds.bottomLeft().getX();
//            int yIndex = position.getY() - bounds.bottomLeft().getY();
//
//            double xOnCanvas = (xIndex + 1) * CELL_WIDTH + halfWidth;
//            double yOnCanvas = mapCanvas.getHeight() - ((yIndex + 1) * CELL_HEIGHT) - halfHeight;
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
//                configureFont(graphics, this.NotoEmojiFont, Color.BROWN);
//                graphics.fillText("🐻", 0, 0);
//                graphics.restore();
//            } else {
//                configureFont(graphics, this.NotoEmojiFont, Color.ORANGE);
//                graphics.fillText("🍯", xOnCanvas, yOnCanvas);
//            }
//        });
//    }
//
//    private void configureFont(GraphicsContext graphics, Font font, Color color) {
//        graphics.setTextAlign(TextAlignment.CENTER);
//        graphics.setTextBaseline(VPos.CENTER);
//        graphics.setFont(font);
//        graphics.setFill(color);
//    }
//
//    private void clearGrid() {
//        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
//        graphics.setFill(Color.WHITE);
//        graphics.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
//    }
//
//    @Override
//    public void mapChanged(WorldMap worldMap, String message) {
//        Platform.runLater(() -> {
//            drawMap();
//            moveInfoLabel.setText(message);
//        });
//    }
//
//
//    public void onSimulationStartClicked() {
//        try {
//            List<MoveDirection> directions = OptionsParser.parse(movesTextField.getText().split("\\s+"));
//            worldMap = new GrassField(15);
//            List<Vector2d> positions = List.of(new Vector2d(5, 5), new Vector2d(5, 4), new Vector2d(3, 3));
//            Simulation simulation = new Simulation(positions, directions, worldMap);
//            List<Simulation> simulations = List.of(simulation);
//            SimulationEngine simulationEngine = new SimulationEngine(simulations);
//            worldMap.addObserver(this);
//            simulationEngine.runAsyncInThreadPool();
//        } catch (IncorrectPositionException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid position");
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
//        } catch (IllegalArgumentException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid moves");
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
//        }
//    }
}
