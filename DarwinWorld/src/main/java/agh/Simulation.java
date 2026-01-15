package agh;

import agh.model.*;
import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;
import agh.model.util.SimulationStats;
import agh.model.StatsListener;

import java.util.*;

public class Simulation implements Runnable {
    protected final List<StatsListener> observers = new ArrayList<>();
    private final List<Animal> animals;
    private final AbstractJungleMap worldMap;
    private final SimulationConfig config;
    private final SimulationStats stats = new SimulationStats();
    private boolean isRunning = true;

    public Simulation(SimulationConfig config, AbstractJungleMap jungleMap) {
        this.config = config;
        this.animals = new ArrayList<>();
        this.worldMap = jungleMap;
        generateAnimals();
    }

    public void addObserver(StatsListener statsListener) {
        observers.add(statsListener);
    }

    public void removeObserver(StatsListener statsListener) {
        observers.remove(statsListener);
    }

    public void notifyObservers() {
        for (StatsListener observer : observers) {
            IO.println(observer);
            observer.statsChanged(stats);
        }
    }

    private void generateAnimals() {
        Random PRNG = new Random();
        for (int i = 0; i < config.startAnimalCount(); i++) {
            Vector2d position = new Vector2d(PRNG.nextInt(config.mapWidth()), PRNG.nextInt(config.mapHeight()));
            Animal animal = new Animal(position, new Genotype(config.genotypeLength()), config.startEnergy(), stats.getCurrentDate());
            this.worldMap.place(animal);
            animals.add(animal);
        }
    }

    public void changeState(){
        isRunning=!isRunning;
        if(isRunning) run();
    }
    public boolean getIsRunning(){
        return isRunning;
    }


    public void stop() {
        isRunning = false;
    }
//    public void start() {
//        isRunning = true;
//        run();
//    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public SimulationStats getStats() {
        return stats;
    }

    // Nie jestem pewny czy synchronized jest potrzebne, ale przy zmianie stanu wydaje mi się, że mogłoby się wysypać
    @Override
    public synchronized void run() {
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
                    worldMap.animalReproduction(config, stats.getCurrentDate())
            );
            worldMap.placeGrasses(config.newGrassesDaily());
            worldMap.nextDay(stats.getCurrentDate());
            updateStats();
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
            if(animal.isAlive() && animal.getEnergy()<=0) animal.die(stats.getCurrentDate());
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
            stats.setAvgChildCount(aliveAnimalsCount == 0 ? 0 : totalAliveAnimalsChildrenCount / aliveAnimalsCount);
        if (deadAnimalsCount > 0) {
            stats.setAvgLifeTime(totalDeadAnimalsLifeLength / deadAnimalsCount);
        }
        stats.setAnimalsCount(aliveAnimalsCount);
        stats.setAvgEnergyLevel(aliveAnimalsCount == 0 ? 0 : totalAliveAnimalsEnergy / aliveAnimalsCount);
        stats.setMostPopularGenotype(mostPopularGenotype);
        stats.setGrassesCount(worldMap.getGrassCount());
        stats.setEmptyFields(worldMap.getEmptyCellsCount());
        stats.increaseCurrentDate();
        IO.println("POWIADAMIAM");
        notifyObservers();
    }
}
