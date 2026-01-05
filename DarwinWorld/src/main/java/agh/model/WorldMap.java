package agh.model;

import agh.model.util.Boundary;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * The interface responsible for interacting with the setMap of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {
    UUID getUuid();

    void addObserver(MapChangeListener mapChangeListener);

    void removeObserver(MapChangeListener mapChangeListener);

    void notifyObservers(String message);


    void place(Animal animal);

    void moveAnimals(int moveCost);

    List<Animal> animalsAt(Vector2d position);
    Collection<WorldElement> getElements();

    Boundary getCurrentBounds();
}