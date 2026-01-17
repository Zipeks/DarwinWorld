package agh.model;

import agh.model.util.Genotype;

import java.util.Random;

public class HabsburgAnimal extends Animal {
    private static final Random PRNG = new Random();
    private final AnimalSex animalSex;

    public HabsburgAnimal(Vector2d position, HabsburgAnimal parentOne, HabsburgAnimal parentTwo, int mutationsCnt, int startEnergy, int birthDate) {
        super(position, parentOne, parentTwo, mutationsCnt, startEnergy, birthDate);
        this.animalSex = determineSex(parentOne, parentTwo);
    }

    public HabsburgAnimal(Vector2d position, Genotype genotype, int energy, int birthDate, AnimalSex animalSex) {
        this.animalSex = animalSex;
        super(position, genotype, energy, birthDate);
    }

    private AnimalSex determineSex(HabsburgAnimal p1, HabsburgAnimal p2) {
        int totalEnergy = p1.getEnergy() + p2.getEnergy();

        double p1Contribution = (double) p1.getEnergy() / totalEnergy;

        return (PRNG.nextDouble() < p1Contribution) ? p1.getSex() : p2.getSex();
    }

    public AnimalSex getSex() {
        return animalSex;
    }
}
