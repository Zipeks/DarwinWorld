package agh.model.filesManager;

import agh.model.util.SimulationStats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class CsvWriter {
    public static void saveConfigStats(SimulationStats stats, UUID id) {
        File logs=new File("logs");
        if(!logs.exists()){
            boolean created= logs.mkdir();
            if(!created)
                return;
        }

        File file = new File(logs,"simulation" + id + ".csv");
        boolean exists = file.exists();

        try (FileWriter writer = new FileWriter(file, true)) {
            if (!exists) {
                writer.append("day,animalsCount,grassesCount,emptyFields,avgEnergyLevel,avgLifeTime,avgChildCount,mostPopularGenotype\n");
            }
            writer.append(String.format("%d,%d,%d,%d,%d,%d,%d,\"%s\"\n",
                    stats.currentDate(),
                    stats.animalsCount(),
                    stats.grassesCount(),
                    stats.emptyFields(),
                    stats.avgEnergyLevel(),
                    stats.avgLifeTime(),
                    stats.avgChildCount(),
                    stats.mostPopularGenotype() != null ? stats.mostPopularGenotype() : "-")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
