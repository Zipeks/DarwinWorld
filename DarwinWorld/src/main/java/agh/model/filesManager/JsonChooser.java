package agh.model.filesManager;

import javafx.stage.FileChooser;

import java.io.File;

public class JsonChooser {
    public static FileChooser choose() throws DirectoryCreationException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File presets = new File("presets");
        if(!presets.exists()){
            boolean created= presets.mkdir();
            if(!created)
                throw new DirectoryCreationException("Could not create a preset directory");
        }
        fc.setInitialDirectory(presets);
        return fc;
    }
}
