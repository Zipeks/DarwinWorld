package agh.model.filesManager;

import javafx.stage.FileChooser;

import java.io.File;

public class JsonChooser {
    public static FileChooser choose(){
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
}
