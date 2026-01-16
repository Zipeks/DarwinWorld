package agh.presenter;

import agh.Simulation;
import agh.model.*;
import agh.model.InvalidConfigException;
import agh.model.util.ConfigParser;
import agh.model.util.SimulationConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

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

    public void onStartClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("simulation.fxml")
            );
            BorderPane viewRoot = loader.load();
            Stage stage = new Stage();
            Simulation simulation = getSimulation(loader);
            stage.setTitle("Simulation");
            stage.setScene(new Scene(viewRoot));

            stage.setOnCloseRequest(event -> {
                simulation.pause();
            });
            stage.show();

            executorService.execute(simulation);
        }
        catch(InvalidConfigException e){
            showAlert(new Alert(Alert.AlertType.ERROR),"Nieprawidłowa konfiguracja",e.getMessage());
        }
        catch (NumberFormatException e) {
            showAlert(new Alert(Alert.AlertType.ERROR),"Nieprawidłowa konfiguracja","Wartości muszą być liczbami");
        }
        catch (IOException e) {
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR),"Wystąpił błąd","Coś poszło nie tak");
        }
    }
    private void showAlert(Alert alert,String title,String msg){
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private Simulation getSimulation(FXMLLoader loader) throws InvalidConfigException {
        SimulationPresenter presenter = loader.getController();
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
        //Zrobiłem tu tak, żeby przy zapisie nie trzeba było kombinować z dopisywaniem pól Habsburgów,
        //a myślę że to nie problem że te wartości tu będziemy mieć
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
            FileManager.saveConfig(obj,saveConfig.getScene().getWindow());
            showAlert(new Alert(Alert.AlertType.CONFIRMATION),"Zapisano konfigurację","Poprawnie zapisano konfigurację");
        }catch(InvalidConfigException e){
            showAlert(new Alert(Alert.AlertType.ERROR),"Nieprawidłowa konfiguracja",e.getMessage());
        }
        catch (NumberFormatException e) {
            showAlert(new Alert(Alert.AlertType.ERROR),"Nieprawidłowa konfiguracja","Wartości muszą być liczbami");
        }
        catch (IOException e) {
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR),"Błąd zapisu","Coś poszło nie tak podczas zapisywania konfiguracji");
        }
    }
    public void onLoadConfig() {
        try{
            SimulationConfig config =FileManager.loadConfig(loadPreset.getScene().getWindow());
            setConfig(config);
            changeHabsburgOptions();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(new Alert(Alert.AlertType.ERROR),"Błąd odczytu","Coś poszło nie tak podczas wczytywania konfiguracji");
        }
    }

    public void changeHabsburgOptions(){
        boolean isSelected=habsburg.isSelected();
        males.setDisable(!isSelected);
        females.setDisable(!isSelected);
        inbreedingPenaltyField.setDisable(!isSelected);
    }

    public void closeApp() {
        executorService.shutdown();
    }
}
