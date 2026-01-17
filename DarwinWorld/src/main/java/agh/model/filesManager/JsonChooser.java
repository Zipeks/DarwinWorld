package agh.model.filesManager;

import javafx.stage.FileChooser;

import java.io.File;

public class JsonChooser {
    public static FileChooser choose() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File presets = new File("presets");
        fc.setInitialDirectory(presets);
        return fc;
    }
}
