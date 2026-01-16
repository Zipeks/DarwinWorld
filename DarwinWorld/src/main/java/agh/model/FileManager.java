package agh.model;

import agh.model.util.ConfigParser;
import agh.model.util.SimulationConfig;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import java.io.*;

public class FileManager {

    private static FileChooser JsonFileChooser(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File projectDir = new File(System.getProperty("user.dir"));
        if (projectDir.exists() && projectDir.isDirectory()) {
            File presetDir = new File(projectDir, "src/main/resources/presets");
            if (presetDir.exists() && presetDir.isDirectory()) {
                fc.setInitialDirectory(presetDir);
            } else {
                fc.setInitialDirectory(projectDir);
            }
        }
        return fc;
    }


    public static void saveConfig(JsonObject obj, Window window) throws IOException {
            File file = JsonFileChooser().showSaveDialog(window);
            if (file != null) {
                try (OutputStream os = new FileOutputStream(file);
                     JsonWriter writer = Json.createWriter(os)) {
                    writer.writeObject(obj);
                }
            }
            else{
                throw new IOException("Nie znaleziono pliku");
            }
    }

    public static SimulationConfig loadConfig(Window window) throws IOException {
        File file = JsonFileChooser().showOpenDialog(window);
        if (file != null) {
            try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
                JsonObject obj = reader.readObject();
                return ConfigParser.jsonToConfig(obj);
            }
        }
        else{
            throw new IOException("Nie znaleziono pliku");
        }
    }
}
