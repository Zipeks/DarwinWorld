package agh.model;

import agh.model.util.Boundary;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

abstract class AbstractWorldMap implements agh.model.WorldMap {
    protected final List<MapChangeListener> observers = new CopyOnWriteArrayList<>();
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>() {
    };
    protected final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void addObserver(MapChangeListener mapChangeListener) {
        observers.add(mapChangeListener);
    }

    @Override
    public void removeObserver(MapChangeListener mapChangeListener) {
        observers.remove(mapChangeListener);
    }

    @Override
    public void notifyObservers(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    @Override
    public synchronized void place(Animal animal) {
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
    }

    @Override
    public List<Animal> animalsAt(Vector2d position) {
        return animals.getOrDefault(position, Collections.emptyList());
    }

    @Override
    public List<WorldElement> getElements() {
        return animals.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    abstract public Boundary getCurrentBounds();

}
