package agh.model.filesManager;

import java.io.File;

public class ProjectSubfolderGetter {
    public static File get(String folderName) {
        File currentDir = new File(System.getProperty("user.dir"));

        File projectRoot = currentDir;

        if (!new File(currentDir, "src").exists()) {
            File[] subDirs = currentDir.listFiles(File::isDirectory);
            if (subDirs != null) {
                for (File subDir : subDirs) {
                    if (new File(subDir, "src").exists()) {
                        projectRoot = subDir;
                        break;
                    }
                }
            }
        }

        File targetFolder = new File(projectRoot, folderName);
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        return targetFolder;
    }
}
