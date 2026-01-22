package agh.presenter;

import agh.Simulation;
import agh.model.*;
import agh.model.InvalidConfigException;
import agh.model.filesManager.JsonLoader;
import agh.model.filesManager.JsonSaver;
import agh.model.filesManager.DirectoryCreationException;
import agh.model.util.SimulationConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.json.JsonObject;

import java.io.*;
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
    @FXML
    private TextField dayLength;
    @FXML
    private TextField males;
    @FXML
    private TextField females;
    @FXML
    private TextField inbreedingPenaltyField;
    @FXML
    private Button loadPreset;
    @FXML
    private Button saveConfig;
    @FXML
    private CheckBox saveStats;

    @FXML
    public void initialize() {
        initialAnimals.textProperty().addListener((obs, old, newText) -> {
            animalsUpdated();
        });
        males.textProperty().addListener((obs, old, newText) -> {
            animalsUpdated();
        });
    }

    public void onStartClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("simulation.fxml")
            );
            BorderPane viewRoot = loader.load();
            Stage stage = new Stage();

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            double width=primaryScreenBounds.getWidth();
            double height= primaryScreenBounds.getHeight();
            Simulation simulation = getSimulation(loader,width,height);
            stage.setTitle("Simulation");
            stage.setScene(new Scene(viewRoot));
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());

            stage.setOnCloseRequest(event -> {
                simulation.pause();
            });
            stage.show();

            executorService.execute(simulation);
        } catch (InvalidConfigException e) {
            showAlert(new Alert(Alert.AlertType.ERROR), "Invalid configuration", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(new Alert(Alert.AlertType.ERROR), "Invalid configuration", "Values must be numeric");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR), "ERROR", "Something went wrong");
        }
    }

    private void showAlert(Alert alert, String title, String msg) {
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private Simulation getSimulation(FXMLLoader loader,double width,double height) throws InvalidConfigException {
        SimulationPresenter presenter = loader.getController();
        presenter.setHeight(height);
        presenter.setWidth(width);
        SimulationConfig config = getSimulationConfig();
        config.validate();
        AbstractJungleMap map;
        if (!config.habsburgsOn()) {
            map = new ClassicalMap(config.startGrassesCount(), config.mapWidth(), config.mapHeight());
        } else {
            map = new HabsburgMap(config.startGrassesCount(), config.mapWidth(), config.mapHeight());
        }
        map.addObserver(presenter);

        presenter.setMap(map);
        presenter.setConfig(config);


        Simulation simulation = new Simulation(config, map);
        presenter.setChangeState(() -> {
            if (simulation.isRunning()) {
                simulation.pause();
            } else {
                simulation.resume();
                executorService.execute(simulation);
            }
        });

        presenter.setSimulation(simulation);
        presenter.setSaveStats(saveStats.isSelected());
        simulation.addObserver(presenter);
        return simulation;
    }

    private void setConfig(SimulationConfig config) {
        initialMapWidth.setText(String.valueOf(config.mapWidth()));
        initialMapHeight.setText(String.valueOf(config.mapHeight()));
        initialEnergy.setText(String.valueOf(config.startEnergy()));
        reproductionCost.setText(String.valueOf(config.energyLostToReproduce()));
        initialGenomLength.setText(String.valueOf(config.genotypeLength()));
        energyLoss.setText(String.valueOf(config.energyLostDaily()));
        energyFromEatingGrass.setText(String.valueOf(config.energyFromEatingGrass()));
        mutationMin.setText(String.valueOf(config.minimalMutationCount()));
        mutationMax.setText(String.valueOf(config.maximalMutationCount()));
        initialAnimals.setText(String.valueOf(config.startAnimalCount()));
        fertilityEnergy.setText(String.valueOf(config.energyNeededToReproduce()));
        initialPlants.setText(String.valueOf(config.startGrassesCount()));
        dailyIncrease.setText(String.valueOf(config.newGrassesDaily()));
        habsburg.setSelected(config.habsburgsOn());
        males.setText(String.valueOf(config.startingMales()));
        females.setText(String.valueOf(config.startingFemales()));
        inbreedingPenaltyField.setText(String.valueOf(config.inbreedingPenalty()));
        dayLength.setText(String.valueOf(config.timeBetweenDays()));

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
        int day = Integer.parseInt(dayLength.getText());
        boolean isHabsburg = habsburg.isSelected();
        int startingMales = Integer.parseInt(males.getText());
        int startingFemales = Integer.parseInt(females.getText());
        int inbreedingPenalty = Integer.parseInt(inbreedingPenaltyField.getText());
        return new SimulationConfig(mapWidth, mapHeight,
                plants, energyFromGrass, dailyInc, animals, energy, energyLossValue,
                fertility, reproduction, mutationMinValue, mutationMaxValue,
                genomLength, day, isHabsburg, startingMales, startingFemales, inbreedingPenalty);
    }

    public void onSaveConfig() {
        try {
            SimulationConfig config = getSimulationConfig();
            config.validate();
            JsonObject obj = config.toJson();
            JsonSaver.saveConfig(obj, saveConfig.getScene().getWindow());
        } catch (InvalidConfigException e) {
            showAlert(new Alert(Alert.AlertType.ERROR), "Invalid configuration", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(new Alert(Alert.AlertType.ERROR), "Invalid configuration", "Values must be numeric");
        }
        catch (DirectoryCreationException e){
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR), "Preset directory error", e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR), "Write error", "Could not write to file");
        }
    }

    public void onLoadConfig() {
        try {
            SimulationConfig config = JsonLoader.loadConfig(loadPreset.getScene().getWindow());
            if(config!=null) {
                setConfig(config);
                changeHabsburgOptions();
            }
        }
        catch (DirectoryCreationException e){
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR), "Preset directory error", e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR), "Read error", "Could not read the file");
        }
    }

    public void changeHabsburgOptions() {
        boolean isSelected = habsburg.isSelected();
        males.setDisable(!isSelected);
        inbreedingPenaltyField.setDisable(!isSelected);
    }

    public void animalsUpdated() {
        if (initialAnimals.getText().isEmpty() || males.getText().isEmpty()) return;
        int animals = Integer.parseInt(initialAnimals.getText());
        int startingMales = Integer.parseInt(males.getText());
        int femalesCount = Math.max(animals - startingMales, 0);
        females.setText(String.valueOf(femalesCount));
    }

    public void closeApp() {
        executorService.shutdown();
    }
}
