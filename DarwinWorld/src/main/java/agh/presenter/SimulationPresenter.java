package agh.presenter;

import agh.Simulation;
import agh.SimulationEngine;
import agh.model.*;
import agh.model.MapChangeListener;
import agh.model.util.Boundary;
import agh.model.util.MapBoundaryException;
import agh.model.util.SimulationConfig;
import agh.model.util.SimulationStats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SimulationPresenter implements MapChangeListener,StatsListener {
    private static final int CELL_WIDTH = 65;
    private static final int CELL_HEIGHT = 65;
    private static final int BORDER_WIDTH = 2;
    private static final int BORDER_OFFSET = BORDER_WIDTH / 2;
    private final Font NotoEmojiFont;
    private int cellSize=10;
    private JungleMap jungleMap;
    private SimulationConfig config;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private Label animalsCount ;
    @FXML
    private Label grassCount ;
    @FXML
    private Label emptyFields;
    @FXML
    private Label avgAge;
    @FXML
    private Label avgEnergy ;
    @FXML
    private Label genotype;
    @FXML
    private Label avgChildCount;


    public SimulationPresenter() {
        String fontPath = Objects.requireNonNull(getClass().getResource("/fonts/NotoEmoji-VariableFont_wght.ttf")).toExternalForm();
        NotoEmojiFont = Font.loadFont(fontPath, 40);
    }

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
        cellSize=Math.max(8,Math.min(colSize,rowSize));
        int canvasWidth = cellSize * numCols + BORDER_WIDTH;
        int canvasHeight = cellSize * numRows + BORDER_WIDTH;
        mapCanvas.setWidth(canvasWidth);
        mapCanvas.setHeight(canvasHeight);
        clearGrid();

        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();

        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(2);

//        IO.println("DZUNGLA");
//        IO.println(jungleBounds);

        //Dżungla
        graphics.setFill(Color.DARKGREEN);
        graphics.fillRect(0, (bounds.topRight().getY()-jungleBounds.topRight().getY())*cellSize,mapCanvas.getWidth(),(jungleBounds.topRight().getY()-jungleBounds.bottomLeft().getY()+1)*cellSize);

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



        double halfCell = ((double) cellSize / 2);
        int fontSize = (int) (cellSize * 0.9);
        configureFont(graphics, new Font("notoEmojiFamily",fontSize), Color.BLACK);
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
                graphics.setFill(getAnimalColor(((Animal) element).getEnergy(),config.energyLostDaily()));
                graphics.fillText("\uD83D\uDC3B", 0, 0);
//                graphics.setFill(getAnimalColor(((Animal) element).getEnergy(),config.energyLostDaily()));
//                graphics.fillText("\uD83D\uDC3B", 0, 0);
//                graphics.fillText("@", 0, 0);
                graphics.restore();
            } else {
//                configureFont(graphics, this.NotoEmojiFont, Color.ORANGE);
//                graphics.fillText("🍯", xOnCanvas, yOnCanvas);
                graphics.setFill(Color.YELLOWGREEN);
                graphics.fillText("\uD83C\uDF33", xOnCanvas, yOnCanvas);
//                graphics.fillText("#", xOnCanvas, yOnCanvas);
            }
        });
    }
    private Color getAnimalColor(int animalEnergy,int dailyLoss){
        int moves=animalEnergy/dailyLoss;
        if(moves<=0) return  Color.BLACK;
        else if(moves<=3) return Color.RED;
        else if(moves<=6) return Color.YELLOW;
        else if(moves<=10) return Color.VIOLET;
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
        //            moveInfoLabel.setText(message);
        Platform.runLater(this::drawMap);
    }
//
    public void setConfig(SimulationConfig config){
        this.config=config;
        drawMap();
    }
//

    public void onCanvasClicked(MouseEvent e){
        double mouseX = e.getX();
        double mouseY = e.getY();
        int x= (int) mouseX/cellSize;
        int y= (int) (mapCanvas.getHeight()-mouseY)/cellSize;
        List<Animal> animals=jungleMap.animalsAt(new Vector2d(x,y));
        IO.println(animals);
        IO.println("x=" + x + ", y=" + y);
//        IO.println("CANVAS CLICKED");
    }


    public void updateStats(SimulationStats stats){
        emptyFields.setText(String.valueOf(stats.getEmptyFields()));
        grassCount.setText(String.valueOf(stats.getGrassesCount()));
        avgEnergy.setText(String.valueOf(stats.getAvgEnergyLevel()));
        avgAge.setText(String.valueOf(stats.getAvgLifeTime()));
        avgChildCount.setText(String.valueOf(stats.getAvgChildCount()));
        genotype.setText(String.valueOf(stats.getMostPopularGenotype()));
        animalsCount.setText(String.valueOf(stats.getAnimalsCount()));
    }
    @Override
    public void statsChanged(SimulationStats stats) {
        Platform.runLater(() -> updateStats(stats));
    }
//    private void openAnimalWindow(){
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getClassLoader().getResource("simulation.fxml")
//            );
//            BorderPane viewRoot = loader.load();
//            Stage stage = new Stage();
//            AnimalStatsPresenter presenter = loader.getController();
//            animalStats.addObserver(presenter);
//            stage.setTitle("Animal information");
//            stage.setScene(new Scene(viewRoot));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
