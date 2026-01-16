package agh.model.filesManager;

import agh.model.util.SimulationConfig;
import javafx.stage.Window;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JsonLoader {
    public static SimulationConfig loadConfig(Window window) throws IOException {
        File file = JsonChooser.choose().showOpenDialog(window);
        if (file != null) {
            try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
                JsonObject obj = reader.readObject();
                return new SimulationConfig(obj);
            }
        }
        else{
            throw new IOException("Nie znaleziono pliku");
        }
    }
}
