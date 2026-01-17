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
        if(!presets.exists()){
            boolean created= presets.mkdir();
            if(!created)
                return null;
        }
        fc.setInitialDirectory(presets);
        return fc;
    }
}
