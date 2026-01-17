package agh.model;

import agh.model.util.Boundary;
import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;

import java.util.*;

public class ClassicalMap extends AbstractJungleMap {
    private static final Random PRNG = new Random();

    public ClassicalMap(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }

    @Override
    public Optional<Parents> getParents(List<Animal> candidates, SimulationConfig config) {
        sortAnimals(candidates);
        Animal parentOne = candidates.get(0);
        Animal parentTwo = candidates.get(1);

        if (parentOne.getEnergy() >= config.energyNeededToReproduce() && parentTwo.getEnergy() >= config.energyNeededToReproduce()) {
            return Optional.of(new Parents(parentOne, parentTwo));
        }
        return Optional.empty();
    }

    @Override
    public Animal createChild(Parents parents, SimulationConfig config, int currentDay) {
        Animal parentOne = parents.parentOne();
        Animal parentTwo = parents.parentTwo();

        int mutations = PRNG.nextInt(config.minimalMutationCount(), config.maximalMutationCount() + 1);

        return new Animal(parentOne.getPosition(), parentOne, parentTwo, mutations, config.energyLostToReproduce() * 2, currentDay);
    }
}