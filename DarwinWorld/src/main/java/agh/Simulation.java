package agh;

import agh.model.*;
import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;
import agh.model.util.SimulationStats;
import agh.model.StatsListener;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Simulation implements Runnable {
    protected final List<StatsListener> observers = new CopyOnWriteArrayList<>();
    private final List<Animal> animals;
    private final AbstractJungleMap worldMap;
    private final SimulationConfig config;
    private SimulationStats stats;
    private volatile boolean isRunning = true;

    public Simulation(SimulationConfig config, AbstractJungleMap jungleMap) {
        this.config = config;
        this.animals = new ArrayList<>();
        this.worldMap = jungleMap;
        stats = new SimulationStats(config.startAnimalCount(), 0, 0, 0, 0, 0, 0, new Genotype(config.genotypeLength()));
        generateAnimals();
    }

    public void addObserver(StatsListener statsListener) {
        observers.add(statsListener);
    }

    public void removeObserver(StatsListener statsListener) {
        observers.remove(statsListener);
    }

    public void notifyObservers(SimulationStats dailyStats) {
        for (StatsListener observer : observers) {
            IO.println(observer);
            observer.statsChanged(dailyStats);
        }
    }

    private void generateAnimals() {
        Random PRNG = new Random();
        if (!config.habsburgsOn()) {
            for (int i = 0; i < config.startAnimalCount(); i++) {
                Vector2d position = new Vector2d(PRNG.nextInt(config.mapWidth()), PRNG.nextInt(config.mapHeight()));
                Animal animal = new Animal(position, new Genotype(config.genotypeLength()), config.startEnergy(), stats.currentDate());
                this.worldMap.place(animal);
                animals.add(animal);
            }
        } else {
            for (int j = 0; j < 2; j++) {
                AnimalSex sex;
                int toGen;
                if (j == 0) {
                    sex = AnimalSex.MALE;
                    toGen = config.startingMales();
                } else {
                    sex = AnimalSex.FEMALE;
                    toGen = config.startingFemales();
                }
                for (int i = 0; i < toGen; i++) {
                    Vector2d position = new Vector2d(PRNG.nextInt(config.mapWidth()), PRNG.nextInt(config.mapHeight()));
                    HabsburgAnimal animal = new HabsburgAnimal(position, new Genotype(config.genotypeLength()), config.startEnergy(), stats.currentDate(), sex);
                    this.worldMap.place(animal);
                    animals.add(animal);
                }
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void pause() {
        isRunning = false;
    }

    public void resume() {
        isRunning = true;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public SimulationStats getStats() {
        return stats;
    }

    @Override
    public synchronized void run() {
        while (isRunning) {
            if (stats.animalsCount() == 0) {
                pause();
                break;
            }
            worldMap.removeDeadAnimals();
            worldMap.moveAnimals(config.energyLostDaily());
            worldMap.grassConsumption(config.energyFromEatingGrass());
            animals.addAll(
                    worldMap.animalReproduction(config, stats.currentDate())
            );
            worldMap.placeGrasses(config.newGrassesDaily());
            worldMap.nextDay(stats.currentDate());
            updateStats();
            try {
                Thread.sleep(config.timeBetweenDays());
            } catch (InterruptedException e) {
                IO.println(e.getMessage());
                break;
            }
        }
    }

    private synchronized void updateStats() {
        int aliveAnimalsCount = 0;
        int deadAnimalsCount = 0;
        int totalDeadAnimalsLifeLength = 0;
        int totalAliveAnimalsEnergy = 0;
        Genotype mostPopularGenotype = null;
        int maxDescendants = 0;
        int totalAliveAnimalsChildrenCount = 0;
        int avgChildCount;
        int avgLifeTime = 0;
        for (Animal animal : animals) {
            if (animal.isAlive() && animal.getEnergy() <= 0) animal.die(stats.currentDate());
            if (animal.isAlive()) {
                aliveAnimalsCount++;
                totalAliveAnimalsEnergy += animal.getEnergy();
                int descendants = animal.getDescendantCount();
                if (maxDescendants < descendants) {
                    maxDescendants = descendants;
                    mostPopularGenotype = animal.getGenotype();
                }
                totalAliveAnimalsChildrenCount += animal.getChildrenCount();
                animal.increaseAge();
            } else {
                deadAnimalsCount++;
                totalDeadAnimalsLifeLength += animal.getAge();
            }
        }
        avgChildCount = (aliveAnimalsCount == 0 ? 0 : totalAliveAnimalsChildrenCount / aliveAnimalsCount);
        if (deadAnimalsCount > 0) {
            avgLifeTime = (totalDeadAnimalsLifeLength / deadAnimalsCount);
        }
        stats = new SimulationStats(
                aliveAnimalsCount,
                worldMap.getGrassCount(),
                worldMap.getEmptyCellsCount(),
                aliveAnimalsCount == 0 ? 0 : totalAliveAnimalsEnergy / aliveAnimalsCount,
                avgLifeTime,
                avgChildCount,
                stats.currentDate() + 1,
                mostPopularGenotype
        );
        notifyObservers(stats);
    }

}
