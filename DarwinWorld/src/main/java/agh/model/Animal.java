package agh.model;

import agh.model.util.AnimalStats;
import agh.model.util.Genotype;

import java.util.*;

public class Animal implements WorldElement {
    private final UUID id = UUID.randomUUID();
    private final Genotype genotype;
    private final List<Animal> children = new ArrayList<>();
    private final AnimalStats animalStats = new AnimalStats();
    private MapDirection direction;
    private Vector2d position;

    public Animal(Vector2d position, Animal parentOne, Animal parentTwo, int mutationsCnt, int startEnergy) {
        this(position, new Genotype(parentOne, parentTwo, mutationsCnt), startEnergy);
    }

    public Animal(Vector2d position, Genotype genotype, int energy) {
        this.direction = MapDirection.randomDirection();
        this.position = position;
        this.genotype = genotype;
        animalStats.setEnergy(energy);
    }


    public boolean isAt(Vector2d otherPosition) {
        return Objects.equals(position, otherPosition);
    }

    public void move(MoveValidator moveValidator, int moveCost) {
        direction = direction.rotateBy(genotype.next());

        IO.println("---------------");
        IO.println(position);

        Vector2d moveVector = direction.toUnitVector();
        Vector2d newPosition = moveValidator.moveOnMap(position, moveVector);
        if (newPosition.getY() == position.getY() && direction.toUnitVector().getY() != 0) {
            direction = direction.opposite();
        } else {
            position = newPosition;
        }

        IO.println(position);
        IO.println("---------------");
        animalStats.setEnergy(animalStats.getEnergy() - moveCost);
    }

    public void addChild(Animal child, int energyCost) {
        animalStats.increaseChildrenCount();
        children.add(child);
        animalStats.setEnergy(animalStats.getEnergy() - energyCost);
    }

    public void eatenGrass(int energy) {
        animalStats.increaseGrassesEaten();
        animalStats.setEnergy(animalStats.getEnergy() + energy);
    }


    public int getDescendantCount() {
        HashSet<Animal> uniqueDescendants = new HashSet<>();
        DFS_UniqueDescendants(this, uniqueDescendants);
        return uniqueDescendants.size();
    }

    private void DFS_UniqueDescendants(Animal animal, HashSet<Animal> unique) {
        for (Animal child : animal.getChildren()) {
            if (unique.add(child)) {
                DFS_UniqueDescendants(child, unique);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
        return animalStats.getEnergy();
    }

    public int getAge() {
        return animalStats.getAge();
    }

    public List<Animal> getChildren() {
        return children;
    }

    public int getChildrenCount() {
        return children.size();
    }
}
