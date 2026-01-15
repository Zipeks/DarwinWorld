package agh.presenter;

import agh.Simulation;
import agh.model.*;
import agh.model.MapChangeListener;
import agh.model.util.Boundary;
import agh.model.util.SimulationConfig;
import agh.model.util.SimulationStats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.*;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationPresenter implements MapChangeListener, StatsListener {
    private static final double MAX_WIDTH = 1280;
    private static final double MAX_HEIGHT = 640;
    private static final int BORDER_WIDTH = 2;
    private static final int BORDER_OFFSET = BORDER_WIDTH / 2;

    private String fontPath;
    private Font notoEmojiFont;

    private int numCols;
    private int numRows;
    private double canvasWidth;
    private double canvasHeight;
    private double cellSize;


    private AbstractJungleMap map;
    private SimulationConfig config;

    private Simulation simulation;
    private Runnable changeState;

    @FXML
    private Canvas mapCanvas;
    @FXML
    private Label animalsCount;
    @FXML
    private Label grassCount;
    @FXML
    private Label emptyFields;
    @FXML
    private Label avgAge;
    @FXML
    private Label avgEnergy;
    @FXML
    private Label genotype;
    @FXML
    private Label avgChildCount;
    @FXML
    private Label day;
    @FXML
    private Button controlSimulation;

    @FXML
    private HBox animalsCountBox;
    @FXML
    private HBox grassCountBox;
    @FXML
    private HBox emptyFieldsBox;
    @FXML
    private HBox avgEnergyBox;
    @FXML
    private HBox avgChildCountBox;
    @FXML
    private HBox avgAgeBox;
    @FXML
    private HBox genotypeBox;

    public SimulationPresenter() {
        try {
            fontPath = Objects.requireNonNull(getClass().getResource("/fonts/NotoEmoji-VariableFont_wght.ttf")).toExternalForm();
        } catch (NullPointerException e) {
            System.err.println("Font not found");
        }
    }

    public void setMap(AbstractJungleMap map) {
        this.map = map;
        initializeView();
    }
    // Idk czy to działa, u mnie się może sypać przez WM które stosuję xD
    @FXML
    public void initialize() {
        //Przeniose to potem do osobnej

        Tooltip animalTooltip=new Tooltip("Animals count");
        animalTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(animalsCountBox,animalTooltip);

        Tooltip grassTooltip=new Tooltip("Grass count");
        grassTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(grassCountBox,grassTooltip);

        Tooltip emptyTooltip=new Tooltip("Empty fields");
        emptyTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(emptyFieldsBox,emptyTooltip);

        Tooltip avgEnergyTooltip=new Tooltip("Average energy");
        avgEnergyTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(avgEnergyBox,avgEnergyTooltip);

        Tooltip childCountTooltip=new Tooltip("Average child count");
        childCountTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(avgChildCountBox,childCountTooltip);

        Tooltip avgAgeTooltip=new Tooltip("Average age");
        avgAgeTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(avgAgeBox,avgAgeTooltip);

        Tooltip genoTypeTooltip=new Tooltip("Most popular genotype");
        genoTypeTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(genotypeBox,genoTypeTooltip);
    }
    private void initializeView() {
        if (map == null) return;

        Boundary bounds = map.getCurrentBounds();

        numCols = bounds.topRight().getX() - bounds.bottomLeft().getX() + 1;
        numRows = bounds.topRight().getY() - bounds.bottomLeft().getY() + 1;

        double colSize =  MAX_WIDTH / numCols;
        double rowSize =  MAX_HEIGHT / numRows;

        cellSize = Math.max(8, Math.min(colSize, rowSize));

        canvasWidth = cellSize * numCols + BORDER_WIDTH;
        canvasHeight = cellSize * numRows + BORDER_WIDTH;

        mapCanvas.setWidth(canvasWidth);
        mapCanvas.setHeight(canvasHeight);

        if (fontPath != null) {
            notoEmojiFont = Font.loadFont(fontPath, cellSize * 0.9);
        }
    }

    public void drawMap() {
        Boundary bounds = map.getCurrentBounds();
        Boundary jungleBounds = map.getJungle();
        clearGrid();

        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();

        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(1);

        //Dżungla
        graphics.setFill(Color.DARKGREEN);
        graphics.fillRect(0, (bounds.topRight().getY() - jungleBounds.topRight().getY()) * cellSize, mapCanvas.getWidth(), (jungleBounds.topRight().getY() - jungleBounds.bottomLeft().getY() + 1) * cellSize);


        double halfCell = (cellSize / 2);
        configureFont(graphics, this.notoEmojiFont, Color.BLACK);
        Set<Vector2d> animalsPosition = new HashSet<>();

        map.getElements().forEach(element -> {
            Vector2d position = element.getPosition();
            int xIndex = position.getX() - bounds.bottomLeft().getX();
            int yIndex = position.getY() - bounds.bottomLeft().getY();

            double xOnCanvas = (xIndex) * cellSize + halfCell;
            double yOnCanvas = mapCanvas.getHeight() - ((yIndex) * cellSize) - halfCell;

            if (element instanceof Animal) {
                graphics.save();
                graphics.translate(xOnCanvas, yOnCanvas);
                MapDirection mapDirection = ((Animal) element).getDirection();
                if (animalsPosition.contains(element.getPosition())) {
                    boolean isJungle = position.follows(jungleBounds.bottomLeft()) && position.precedes(jungleBounds.topRight());

                    graphics.setFill(isJungle ? Color.DARKGREEN : Color.SADDLEBROWN);
                    graphics.fillRect(-halfCell, -halfCell, cellSize, cellSize);

                    graphics.rect(0, 0, cellSize, cellSize);
                    graphics.setFill(Color.RED);
                    graphics.fillText("\uD83D\uDC9E", 0, 0);
                } else {
                    graphics.setFill(getAnimalColor(((Animal) element).getEnergy(), config.energyLostDaily()));
                    graphics.fillText("\uD83D\uDC3B", 0, 0);
                    animalsPosition.add(element.getPosition());
                }
                graphics.restore();
            } else {
                graphics.setFill(Color.YELLOWGREEN);
                graphics.fillText("\uD83C\uDF33", xOnCanvas, yOnCanvas);
            }
        });
//         Pionowe kreski
        for (int i = 0; i <= numCols; i++) {
            double x = i * cellSize + BORDER_OFFSET;
            graphics.strokeLine(x, 0, x, canvasHeight);
        }
        // Poziome kreski
        for (int i = 0; i <= numRows; i++) {
            double y = i * cellSize + BORDER_OFFSET;
            graphics.strokeLine(0, y, canvasWidth, y);
        }
    }

//    private Boolean hasTheMostPopularGenotype(Animal animal, Genotype mostPopularGenotype){
//
//    }
    private Color getAnimalColor(int animalEnergy, int dailyLoss) {
        int moves = animalEnergy / dailyLoss;
        if (moves <= 0) return Color.BLACK;
        else if (moves <= 3) return Color.RED;
        else if (moves <= 6) return Color.YELLOW;
        else if (moves <= 10) return Color.VIOLET;
        else return Color.PEACHPUFF;
    }

    private void configureFont(GraphicsContext graphics, Font font, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(font);
        graphics.setFill(color);
    }

    private void clearGrid() {
        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setFill(Color.SADDLEBROWN);
        graphics.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
    }

    public void setConfig(SimulationConfig config) {
        this.config = config;
        drawMap();
    }

    public void onCanvasClicked(MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();
        int x = (int) (mouseX / cellSize);
        int y = (int) ((mapCanvas.getHeight() - mouseY) / cellSize);
        List<Animal> animals = map.animalsAt(new Vector2d(x, y));
        IO.println(animals);
        for (Animal animal : animals) {
            openAnimalWindow(animal);
        }
        IO.println("x=" + x + ", y=" + y);
//        IO.println("CANVAS CLICKED");
    }


    public void updateStats(SimulationStats stats) {
        emptyFields.setText(String.valueOf(stats.emptyFields()));
        grassCount.setText(String.valueOf(stats.grassesCount()));
        avgEnergy.setText(String.valueOf(stats.avgEnergyLevel()));
        avgAge.setText(String.valueOf(stats.avgLifeTime()));
        avgChildCount.setText(String.valueOf(stats.avgChildCount()));
        genotype.setText(stats.mostPopularGenotype() != null ? String.valueOf(stats.mostPopularGenotype()) : "-");
        animalsCount.setText(String.valueOf(stats.animalsCount()));
        day.setText("Dzień " + stats.currentDate());

    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setChangeState(Runnable changeState) {
        this.changeState = changeState;
    }

    @FXML
    public void onPauseResumeClicked() {
        if (changeState != null) {
            changeState.run();
        }
    }
    @Override
    public void statsChanged(SimulationStats stats) {
        Platform.runLater(() -> updateStats(stats));
    }

    private void openAnimalWindow(Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("animalStats.fxml")
            );
            Pane viewRoot = loader.load();
            Stage stage = new Stage();
            AnimalStatsPresenter presenter = loader.getController();
            animal.addObserver(presenter);
            stage.setTitle("Animal information");
            stage.setScene(new Scene(viewRoot));
            stage.setOnCloseRequest(event -> {
                animal.removeObserver(presenter);
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
