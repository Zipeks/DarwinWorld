package agh.model.filesManager;

import javafx.stage.Window;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonSaver {
    public static void saveConfig(JsonObject obj, Window window) throws IOException, DirectoryCreationException {
        File file = JsonChooser.choose().showSaveDialog(window);
        if (file != null) {
            try (OutputStream os = new FileOutputStream(file);
                 JsonWriter writer = Json.createWriter(os)) {
                writer.writeObject(obj);
            }
        }
    }
}
