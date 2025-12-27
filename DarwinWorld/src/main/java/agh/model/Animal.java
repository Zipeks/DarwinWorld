package agh.model;

import agh.model.util.Genotype;

import java.util.Objects;

public class Animal implements WorldElement {
    private final Genotype genotype;
    int id;
    int age;
    private MapDirection direction;
    private Vector2d position;
    private int energy;

    public Animal(Vector2d position, Animal parentOne, Animal parentTwo, int mutationsCnt, int startEnergy) {
        this(position, new Genotype(parentOne,parentTwo,mutationsCnt),startEnergy);
    }

    public Animal(Vector2d position, int genomSize, int startEnergy) {
        this(position, new Genotype(genomSize), startEnergy);
    }

    private Animal(Vector2d position, Genotype genotype, int energy) {
        this.direction = MapDirection.randomDirection();
        this.position = position;
        this.genotype = genotype;
        this.energy = energy;
    }

    public MapDirection getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public boolean isAt(Vector2d otherPosition) {
        return Objects.equals(position, otherPosition);
    }

    public void move(MoveDirection dir, MoveValidator moveValidator) {
        switch (dir) {
            case RIGHT -> direction = direction.next();
            case LEFT -> direction = direction.previous();
            case FORWARD -> {
                Vector2d v = position.add(direction.toUnitVector());
                if (moveValidator.canMoveTo(v))
                    position = v;
            }
            case BACKWARD -> {
                Vector2d v = position.add(direction.toUnitVector().opposite());
                if (moveValidator.canMoveTo(v))
                    position = v;
            }
        }
    }
}
