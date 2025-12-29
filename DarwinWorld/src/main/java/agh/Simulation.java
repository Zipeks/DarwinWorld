package agh;

import agh.model.*;
import agh.model.util.Boundary;
import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;
import agh.model.util.SimulationStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Simulation implements Runnable {
    private final List<Animal> animals;
    private final JungleMap worldMap;
    private final boolean isRunning = true;
    private final SimulationConfig config;

    public Simulation(SimulationConfig config,JungleMap jungleMap) {
        this.config = config;
        this.animals = new ArrayList<>();
        this.worldMap=jungleMap;
//        this.worldMap = new JungleMap(
//                config.startGrassesCount()
//                , new Boundary(new Vector2d(0, 0), new Vector2d(config.mapWidth() - 1, config.mapHeight() - 1)));
//        for (Vector2d position : positions) {
//            Animal animal = new Animal(position);
//            this.worldMap.place(animal);
//            animals.add(animal);
//        }
    }

    public void generateAnimals() {
        Random PRNG = new Random();
        Genotype startingGenotype = new Genotype(config.genotypeLength());

        for (int i = 0; i < config.startAnimalCount(); i++) {
            Vector2d position = new Vector2d(PRNG.nextInt(config.mapWidth()), PRNG.nextInt(config.mapHeight()));
            Animal animal = new Animal(position, new Genotype(startingGenotype), config.startEnergy());
            this.worldMap.place(animal);
            animals.add(animal);
        }
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(config.timeBetweenDays());
        } catch (InterruptedException e) {
            IO.println(e.getMessage());
        }
        worldMap.removeDeadAnimals();
        worldMap.moveAnimals(config.energyLostDaily());
        worldMap.grassConsumption(config.energyFromEatingGrass());
        animals.addAll(worldMap.animalReproduction(config.energyNeededToReproduce(),
                config.energyLostToReproduce(),
                config.minimalMutationCount(),
                config.maximalMutationCount()));
        worldMap.placeGrasses(config.newGrassesDaily());
    }

}
