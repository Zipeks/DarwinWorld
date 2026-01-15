package agh.model;

import agh.model.util.Boundary;
import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;

import java.util.*;

public class ClassicalMap extends AbstractJungleMap {

    public ClassicalMap(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }


    @Override
    public synchronized List<Animal> animalReproduction(SimulationConfig config, int date) {
        int energyNeededToReproduce = config.energyNeededToReproduce();
        int energyLostToReproduction = config.energyLostToReproduce();
        int minMutations = config.minimalMutationCount();
        int maxMutation = config.maximalMutationCount();
        List<Animal> children = new ArrayList<>();
        Random PRNG = new Random();

        for (Vector2d position : animals.keySet()) {
            List<Animal> animalsAtP = animals.get(position);
            if (animalsAtP.size() < 2) {
                continue;
            }
            animalsAtP.sort(Comparator.comparingInt(Animal::getEnergy)
                    .thenComparing(Animal::getAge)
                    .thenComparing(Animal::getChildrenCount)
                    .reversed());

            Animal firstAnimal = animalsAtP.getFirst();
            Animal secondAnimal = animalsAtP.get(1);

            if (firstAnimal.getEnergy() > energyNeededToReproduce && secondAnimal.getEnergy() > energyNeededToReproduce) {
                double kinship = Genotype.kinshipLevel(firstAnimal, secondAnimal);
                int inbreedingPenalty = 0;
                if (kinship > .5) {
                    inbreedingPenalty = config.inbreedingPenalty();
                } else if (kinship > 0.25) {
                    inbreedingPenalty = config.inbreedingPenalty() / 2;
                }
                Animal child = new Animal(position, firstAnimal, secondAnimal,
                        PRNG.nextInt(minMutations, maxMutation + 1), (1 - inbreedingPenalty) * (energyLostToReproduction * 2), date);

                firstAnimal.addChild(child, energyLostToReproduction);
                secondAnimal.addChild(child, energyLostToReproduction);

                children.add(child);
            }
        }
        for (Animal child : children) {
            place(child);
        }

        return children;
    }
}