package agh;

import agh.model.*;
import agh.presenter.ConfigurationPresenter;
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
        BorderPane viewRoot = loader.load();
        ConfigurationPresenter presenter = loader.getController();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/FiraCode-Bold.ttf"), 12);
        var scene = new Scene(viewRoot);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
//        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
//        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
