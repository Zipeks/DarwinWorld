package agh;

import agh.model.*;
import agh.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("configurationScreen.fxml"));
//
        StackPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }
//
    private void configureStage(Stage primaryStage, StackPane viewRoot) {
        // Do posprzątania
        Font firaFont = Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Regular.ttf"), 12);
        Font firaFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Bold.ttf"), 12);
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // ustawienie sceny w oknie
        primaryStage.setScene(scene);

        // konfiguracja okna
        primaryStage.setTitle("Simulation app");
//        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
//        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
