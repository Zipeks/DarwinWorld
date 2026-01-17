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
    private final UUID id = UUID.randomUUID();
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
            for (AnimalSex sex : AnimalSex.values()) {
                int toGen;
                if (sex == AnimalSex.MALE) {
                    toGen = config.startingMales();
                } else {
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
            try {
                if (stats.animalsCount() == 0) {
                    pause();
                    break;
                }
                if(stats.currentDate()==0) Thread.sleep(config.timeBetweenDays());
                worldMap.removeDeadAnimals(stats.currentDate());
                worldMap.moveAnimals(config.energyLostDaily());
                worldMap.grassConsumption(config.energyFromEatingGrass());
                animals.addAll(worldMap.animalReproduction(config, stats.currentDate()));
                worldMap.placeGrasses(config.newGrassesDaily());
                worldMap.nextDay(stats.currentDate());
                updateStats();
                Thread.sleep(config.timeBetweenDays());
            } catch (InterruptedException e) {
                IO.println(e.getMessage());
                break;
            }
        }
    }

    private synchronized void updateStats() {
        stats = SimulationStats.updateStats(animals, stats, worldMap.getStats());
        notifyObservers(stats);
    }

    public UUID getId() {
        return id;
    }
}
