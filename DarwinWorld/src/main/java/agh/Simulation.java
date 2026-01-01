package agh;

import agh.model.*;
import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;
import agh.model.util.SimulationStats;

import java.util.*;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final JungleMap worldMap;
    private final SimulationConfig config;
    private final SimulationStats stats = new SimulationStats();
    private boolean isRunning = true;

    public Simulation(SimulationConfig config, JungleMap jungleMap) {
        this.config = config;
        this.animals = new ArrayList<>();
        this.worldMap = jungleMap;
        generateAnimals();
    }

    private void generateAnimals() {
        Random PRNG = new Random();
        Genotype startingGenotype = new Genotype(config.genotypeLength());

        for (int i = 0; i < config.startAnimalCount(); i++) {
            Vector2d position = new Vector2d(PRNG.nextInt(config.mapWidth()), PRNG.nextInt(config.mapHeight()));
            Animal animal = new Animal(position, new Genotype(startingGenotype), config.startEnergy(), stats.getCurrentDate());
            this.worldMap.place(animal);
            animals.add(animal);
        }
    }

    public void stop() {
        isRunning = false;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public SimulationStats getStats() {
        return stats;
    }

    @Override
    public void run() {
        int i = 0;
        while (isRunning) {
            try {
                Thread.sleep(config.timeBetweenDays());
            } catch (InterruptedException e) {
                IO.println(e.getMessage());
            }
            worldMap.removeDeadAnimals();
            worldMap.moveAnimals(config.energyLostDaily());
            worldMap.grassConsumption(config.energyFromEatingGrass());
            animals.addAll(
                    worldMap.animalReproduction(
                            config.energyNeededToReproduce(),
                            config.energyLostToReproduce(),
                            config.minimalMutationCount(),
                            config.maximalMutationCount(),
                            stats.getCurrentDate())
            );
            worldMap.placeGrasses(config.newGrassesDaily());
            worldMap.nextDay(i);
            updateStats();
            i++;
        }
    }

    private void updateStats() {
        // Nie jestem pewny czy "Liczba wszystkich zwierząt" uwzględnia martwe, tak samo najpopularniejszy genotyp
        int aliveAnimalsCount = 0;
        int deadAnimalsCount = 0;
        int totalDeadAnimalsLifeLength = 0;
        int totalAliveAnimalsEnergy = 0;
        HashMap<Genotype, Integer> genotypes = new HashMap<>();
        Genotype mostPopularGenotype = null;
        int mostPopularGenotypeCount = 0;
        int totalAliveAnimalsChildrenCount = 0;

        for (Animal animal : animals) {
            if (animal.isAlive()) {
                aliveAnimalsCount++;
                totalAliveAnimalsEnergy += animal.getEnergy();
                genotypes.computeIfAbsent(animal.getGenotype(), k -> 1);
                int currentGenotypeCount = genotypes.get(animal.getGenotype());
                if (mostPopularGenotypeCount < currentGenotypeCount) {
                    mostPopularGenotypeCount = currentGenotypeCount;
                    mostPopularGenotype = animal.getGenotype();
                }
                totalAliveAnimalsChildrenCount += animal.getChildrenCount();
                animal.increaseAge();
            } else {
                deadAnimalsCount++;
                totalDeadAnimalsLifeLength += animal.getAge();
            }
        }
        stats.setAvgChildCount(totalAliveAnimalsChildrenCount / aliveAnimalsCount);
        if (deadAnimalsCount > 0) {
            stats.setAvgLifeTime(totalDeadAnimalsLifeLength / deadAnimalsCount);
        }
        stats.setAvgEnergyLevel(totalAliveAnimalsEnergy / aliveAnimalsCount);
        stats.setMostPopularGenotype(mostPopularGenotype);
        stats.setGrassesCount(worldMap.getGrassCount());
        stats.setEmptyFields(0); // wolne pole, czyli takie gdzie nie ma zwierząt, trawy, obu ????

        stats.increaseCurrentDate();
    }
}
