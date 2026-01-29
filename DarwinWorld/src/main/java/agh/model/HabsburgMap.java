package agh.model;

import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;

import java.util.*;

public class HabsburgMap extends AbstractJungleMap {
    private final Random PRNG = new Random();

    public HabsburgMap(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }

    @Override
    public Optional<Parents> getParents(List<Animal> candidates, SimulationConfig config) {
        List<Animal> males = new ArrayList<>();
        List<Animal> females = new ArrayList<>();
        for (Animal animal : candidates) {
            if (animal.getEnergy() >= config.energyNeededToReproduce() && animal instanceof HabsburgAnimal hAnimal) {
                if (hAnimal.getSex() == AnimalSex.MALE) {
                    males.add(hAnimal);
                } else {
                    females.add(hAnimal);
                }
            }
        }
        if (males.isEmpty() || females.isEmpty()) {
            return Optional.empty();
        }
        sortAnimals(males);
        sortAnimals(females);
        return Optional.of(new Parents(males.getFirst(), females.getFirst()));
    }

    @Override
    public Animal createChild(Parents parents, SimulationConfig config, int currentDay) {
        HabsburgAnimal parentOne = (HabsburgAnimal) parents.parentOne();
        HabsburgAnimal parentTwo = (HabsburgAnimal) parents.parentOne();
        int mutations = PRNG.nextInt(config.minimalMutationCount(), config.maximalMutationCount() + 1);
        double kinshipLevel = Genotype.kinshipLevel(parentOne, parentTwo);
        int startingEnergy = config.energyLostToReproduce() * 2;
        if (kinshipLevel >= 0.25) {
            startingEnergy = startingEnergy * (1-(config.inbreedingPenalty()/100));
        }
        return new HabsburgAnimal(parentOne.getPosition(), parentOne, parentTwo, mutations, startingEnergy, currentDay);
    }


}
