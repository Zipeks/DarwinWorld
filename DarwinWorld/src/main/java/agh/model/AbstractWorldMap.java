package agh.model;

import agh.model.util.Boundary;

import java.util.*;

abstract class AbstractWorldMap implements agh.model.WorldMap {
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
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
    public void place(Animal animal)  {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            notifyObservers("Animal has been placed at: " + animal.getPosition());
        }
    }

    @Override
    public void move(Animal animal, MoveDirection moveDirection) {
        Vector2d old_position = animal.getPosition();
        MapDirection old_direction = animal.getDirection();
        animal.move(moveDirection, this);
        if (!Objects.equals(old_position, animal.getPosition())) {
            animals.remove(old_position);
            animals.put(animal.getPosition(), animal);
            notifyObservers("Animal has moved from: " + old_position + " to: " + animal.getPosition());
        } else if (!Objects.equals(old_direction, animal.getDirection())) {
            notifyObservers("Animal at: " + animal.getPosition() + " has changed direction from: "
                    + old_direction + " to: " + animal.getDirection());
        } else {
            notifyObservers("Animal at: " + animal.getPosition() + " tried to move "
                    + moveDirection.toString().toLowerCase() + " but can't.");
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    abstract public Boundary getCurrentBounds();

}
