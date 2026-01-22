package agh.model;

import agh.model.util.AnimalStats;
import agh.model.util.Genotype;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Animal implements WorldElement {
    protected final List<AnimalListener> observers = new CopyOnWriteArrayList<>();
    private final UUID id = UUID.randomUUID();
    private final Genotype genotype;
    private final List<Animal> children = new ArrayList<>();
    private final AnimalStats animalStats;
    private final ArrayList<Animal> parents = new ArrayList<>();
    private MapDirection direction;
    private Vector2d position;

    public Animal(Vector2d position, Animal parentOne, Animal parentTwo, int mutationsCnt, int startEnergy, int birthDate) {
        this(position, new Genotype(parentOne, parentTwo, mutationsCnt), startEnergy, birthDate);
        parents.add(parentOne);
        parents.add(parentTwo);
    }

    public Animal(Vector2d position, Genotype genotype, int energy, int birthDate) {
        this.direction = MapDirection.randomDirection();
        this.position = position;
        this.genotype = genotype;
        animalStats = new AnimalStats(birthDate);
        animalStats.setEnergy(energy);
    }

    public void addObserver(AnimalListener animalListener) {
        observers.add(animalListener);
        animalListener.animalChanged(animalStats, genotype, getDescendantCount(), position);
    }

    public void removeObserver(AnimalListener animalListener) {
        observers.remove(animalListener);
    }

    public void notifyObservers() {
        for (AnimalListener observer : observers) {
            observer.animalChanged(animalStats, genotype, getDescendantCount(), position);
        }
    }

    public void move(MoveValidator moveValidator, int moveCost) {
        direction = direction.rotateBy(genotype.next());
        Vector2d moveVector = direction.toUnitVector();
        position = moveValidator.moveOnMap(position, moveVector);
        animalStats.setEnergy(animalStats.getEnergy() - moveCost);
        notifyObservers();
    }

    public void addChild(Animal child, int energyCost) {
        animalStats.increaseChildrenCount();
        children.add(child);
        animalStats.setEnergy(animalStats.getEnergy() - energyCost);
        notifyObservers();
    }

    public void eatenGrass(int energy) {
        animalStats.increaseGrassesEaten();
        animalStats.setEnergy(animalStats.getEnergy() + energy);
        notifyObservers();
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

    public List<Animal> getChildren() {
        return children;
    }

    public int getChildrenCount() {
        return children.size();
    }

    public boolean isAlive() {
        return animalStats.getDeathDate().isEmpty();
    }

    public int lifeLength(int currentDate) {
        int endDate = animalStats.getDeathDate().orElse(currentDate);
        return endDate - animalStats.getBirthDate();
    }

    public int getAge() {
        return animalStats.getAge();
    }

    public void increaseAge() {
        animalStats.increaseAge();
        notifyObservers();
    }

    public void die(int day) {
        animalStats.setDeathDate(day);
        notifyObservers();
    }

    protected AnimalStats getStats(){
        return animalStats;
    }




}
