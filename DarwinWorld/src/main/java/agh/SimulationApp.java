package agh;

import agh.model.*;
import agh.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
//
//        BorderPane viewRoot = loader.load();
//        SimulationPresenter presenter = loader.getController();
//        configureStage(primaryStage, viewRoot);
//        primaryStage.show();
//    }
//
//    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
//        // stworzenie sceny (panelu do wyświetlania wraz zawartością z FXML)
//        var scene = new Scene(viewRoot);
//
//        // ustawienie sceny w oknie
//        primaryStage.setScene(scene);
//
//        // konfiguracja okna
//        primaryStage.setTitle("Simulation app");
//        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
//        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
//    }
}
}
