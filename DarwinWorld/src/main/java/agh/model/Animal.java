package agh.model;

import agh.model.util.Genotype;

import java.util.*;

public class Animal implements WorldElement {
    private final Genotype genotype;
    private final List<Animal> children = new ArrayList<>();
    private int age;
    private int deathDate;
    private MapDirection direction;
    private Vector2d position;
    private int eatenGrassesCount;
    private int energy;

    public Animal(Vector2d position, Animal parentOne, Animal parentTwo, int mutationsCnt, int startEnergy) {
        this(position, new Genotype(parentOne, parentTwo, mutationsCnt), startEnergy);
    }

    public Animal(Vector2d position, Genotype genotype, int energy) {
        this.direction = MapDirection.randomDirection();
        this.position = position;
        this.genotype = genotype;
        this.energy = energy;
    }


    public boolean isAt(Vector2d otherPosition) {
        return Objects.equals(position, otherPosition);
    }

    public void move(MoveValidator moveValidator, int moveCost) {
        direction = direction.rotateBy(genotype.next());
        position =  moveValidator.moveOnMap(position, direction.toUnitVector()) ;
        energy -= moveCost;
    }

    public void addChild(Animal child, int energyCost) {
        children.add(child);
        energy -= energyCost;
    }
    public void eatenGrass(int energy) {
        this.energy += energy;
        eatenGrassesCount++;
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
        return age == animal.age && energy == animal.energy && Objects.equals(genotype, animal.genotype) && Objects.equals(children, animal.children) && direction == animal.direction && Objects.equals(position, animal.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genotype, children, age, direction, position, energy);
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

    public int getAge() {
        return age;
    }

    public List<Animal> getChildren() {
        return children;
    }
    public int getChildrenCount() {
        return children.size();
    }
}
