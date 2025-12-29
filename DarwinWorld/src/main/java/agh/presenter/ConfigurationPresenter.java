package agh.presenter;

import agh.Simulation;
import agh.model.*;
import agh.model.util.Boundary;
import agh.model.util.SimulationConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigurationPresenter {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    @FXML
    private TextField initialMapWidth;
    @FXML
    private TextField initialMapHeight;
    @FXML
    private TextField initialEnergy;
    @FXML
    private TextField reproductionCost;
    @FXML
    private TextField initialGenomLength;
    @FXML
    private TextField energyLoss;
    @FXML
    private TextField mutationMin;
    @FXML
    private TextField mutationMax;
    @FXML
    private TextField initialAnimals;
    @FXML
    private TextField fertilityEnergy;
    @FXML
    private TextField initialPlants;
    @FXML
    private TextField energyFromEatingGrass;
    @FXML
    private TextField dailyIncrease;
    @FXML
    private CheckBox habsburg;

    public void onStartClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("simulation.fxml")
            );
            BorderPane viewRoot = loader.load();
            Stage stage = new Stage();
            SimulationPresenter presenter = loader.getController();
            SimulationConfig config = getSimulationConfig();

            JungleMap jungleMap = new JungleMap(config.startGrassesCount(), new Boundary(new Vector2d(0, 0),
                    new Vector2d(config.mapWidth() - 1, config.mapHeight() - 1)));
            Simulation simulation = new Simulation(config, jungleMap);

            simulation.generateAnimals();
//            simulation.getAnimals();
            presenter.setJungleMap(jungleMap);
            presenter.setConfig(config);
            jungleMap.addObserver(presenter);
            stage.setTitle("Simulation");
            stage.setScene(new Scene(viewRoot));
            stage.show();
            executorService.execute(simulation);
//            executorService.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Niepoprawne wartości");
            alert.setContentText("Podaj poprawne wartości");
            alert.showAndWait();
        }
    }

    private SimulationConfig getSimulationConfig() {
        int mapWidth = Integer.parseInt(initialMapWidth.getText());
        int mapHeight = Integer.parseInt(initialMapHeight.getText());
        int energy = Integer.parseInt(initialEnergy.getText());
        int reproduction = Integer.parseInt(reproductionCost.getText());
        int genomLength = Integer.parseInt(initialGenomLength.getText());
        int energyLossValue = Integer.parseInt(energyLoss.getText());
        int energyFromGrass = Integer.parseInt(energyFromEatingGrass.getText());
        int mutationMinValue = Integer.parseInt(mutationMin.getText());
        int mutationMaxValue = Integer.parseInt(mutationMax.getText());
        int animals = Integer.parseInt(initialAnimals.getText());
        int fertility = Integer.parseInt(fertilityEnergy.getText());
        int plants = Integer.parseInt(initialPlants.getText());
        int dailyInc = Integer.parseInt(dailyIncrease.getText());
        boolean isHabsburg = habsburg.isSelected();
        SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, isHabsburg,
                plants, energyFromGrass, dailyInc, animals, energy, energyLossValue,
                fertility, reproduction, mutationMinValue, mutationMaxValue,
                genomLength, 1000); // Czy czas też podajemy?
        return config;
    }
}
