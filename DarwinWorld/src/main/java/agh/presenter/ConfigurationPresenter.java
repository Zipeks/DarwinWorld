package agh.presenter;

import agh.Simulation;
import agh.model.*;
import agh.model.util.Boundary;
import agh.model.util.MapBoundaryException;
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
        catch (MapBoundaryException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zła mapa");
            alert.setContentText("Rozmiar mapy od 5x5 do 160x80");
            alert.showAndWait();
        }
    }
    public void onLoadPresetClick(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File projectDir = new File(System.getProperty("user.dir"));
        IO.println(projectDir);
        if (projectDir.exists() && projectDir.isDirectory()) {
            File presetDir= new File(projectDir,"src/main/resources/presets");
            if (presetDir.exists() && presetDir.isDirectory()) {
                fc.setInitialDirectory(presetDir);
            }
            else{
                fc.setInitialDirectory(projectDir);
            }
        }
        File file = fc.showOpenDialog(loadPreset.getScene().getWindow());
        if(file!=null){
            IO.println(file);
            try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
                JsonObject obj = reader.readObject();
                SimulationConfig parsedConfig=parseConfig(obj);
                setConfig(parsedConfig);
            }
            catch (Exception e){
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd odczytu pliku");
                alert.setContentText("Przykro mi, ale coś posło nie tak przy odczycie");
                alert.showAndWait();
            }
        }
    }

    private SimulationConfig parseConfig(JsonObject obj){
        int mapWidth = obj.getInt("mapWidth");
        int mapHeight = obj.getInt("mapHeight");
        int energy = obj.getInt("energy");
        int reproduction = obj.getInt("reproduction");
        int genomLength = obj.getInt("genomLength");
        int energyLossValue = obj.getInt("energyLoss");
        int energyFromGrass = obj.getInt("energyFromGrass");
        int mutationMinValue = obj.getInt("mutationMinValue");
        int mutationMaxValue = obj.getInt("mutationMaxValue");
        int animals = obj.getInt("animals");
        int fertility = obj.getInt("fertility");
        int plants = obj.getInt("plants");
        int dailyInc = obj.getInt("dailyInc");
        int day = obj.getInt("day");
        boolean isHabsburg = obj.getBoolean("isHabsburg");
        SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, isHabsburg,
                plants, energyFromGrass, dailyInc, animals, energy, energyLossValue,
                fertility, reproduction, mutationMinValue, mutationMaxValue,
                genomLength, day);
        IO.println(config);
        return config;
    }

    //Tu wiem, że trochę ta konwersja na stringi niepotrzebna, bo można od razu z Jsona, ale wolałem zamienić na config wcześniej

    private void setConfig(SimulationConfig config){
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
        SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, isHabsburg,
                plants, energyFromGrass, dailyInc, animals, energy, energyLossValue,
                fertility, reproduction, mutationMinValue, mutationMaxValue,
                genomLength, day);
        if(mapWidth>160 || mapHeight>80 || mapWidth<5 || mapHeight<5)  throw new MapBoundaryException("Map is too big");
        return config;
    }

    public void onSaveConfig(){
        try{
            SimulationConfig config=getSimulationConfig();
            JsonObject obj=prepareJsonObject(config);

            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );
            File projectDir = new File(System.getProperty("user.dir"));
            if (projectDir.exists() && projectDir.isDirectory()) {
                File presetDir= new File(projectDir,"src/main/resources/presets");
                if (presetDir.exists() && presetDir.isDirectory()) {
                    fc.setInitialDirectory(presetDir);
                }
                else{
                    fc.setInitialDirectory(projectDir);
                }
            }
            File file = fc.showSaveDialog(saveConfig.getScene().getWindow());

            if(file!=null){
                try (OutputStream os = new FileOutputStream(file);
                     JsonWriter writer = Json.createWriter(os)) {
                    writer.writeObject(obj);
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Zapisano konfiguracje");
                alert.setContentText(file.getAbsolutePath());
                alert.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Niepoprawne wartości");
            alert.setContentText("Podaj poprawne wartości");
            alert.showAndWait();
        }
        catch (MapBoundaryException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Zła mapa");
            alert.setContentText("Rozmiar mapy od 5x5 do 160x80");
            alert.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd przy zapisie konfiguracji");
            alert.setContentText("Spróbuj jeszcze raz");
            alert.showAndWait();
        }
    }
    private JsonObject prepareJsonObject(SimulationConfig config){
        JsonObject obj = Json.createObjectBuilder()
                .add("mapWidth", config.mapWidth())
                .add("mapHeight", config.mapHeight())
                .add("energy", config.startEnergy())
                .add("reproduction",config.energyLostToReproduce())
                .add("genomLength",config.genotypeLength())
                .add("energyLoss",config.energyLostDaily())
                .add("energyFromGrass",config.energyFromEatingGrass())
                .add("mutationMinValue",config.minimalMutationCount())
                .add("mutationMaxValue",config.maximalMutationCount())
                .add("animals",config.startAnimalCount())
                .add("fertility",config.energyNeededToReproduce())
                .add("plants",config.startGrassesCount())
                .add("dailyInc",config.newGrassesDaily())
                .add("day",config.timeBetweenDays())
                .add("isHabsburg",config.habsburgsOn())
                .build();
        return obj;
    }


}
