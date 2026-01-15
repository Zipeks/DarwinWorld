package agh.model;

import agh.model.util.Genotype;
import agh.model.util.SimulationConfig;

import java.util.*;

public class HabsburgMap extends AbstractJungleMap {
    public HabsburgMap(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }

    @Override
    public synchronized List<Animal> animalReproduction(SimulationConfig config, int currentDay) {
        int energyNeeded = config.energyNeededToReproduce();
        int energyLost = config.energyLostToReproduce();
        int minMut = config.minimalMutationCount();
        int maxMut = config.maximalMutationCount();
        List<Animal> children = new ArrayList<>();
        Random PRNG = new Random();
        Comparator<Animal> animalComparator = Comparator.comparingInt(Animal::getEnergy)
                .thenComparing(Animal::getAge)
                .thenComparing(Animal::getChildrenCount)
                .reversed();
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalsAtP = animals.get(position);
            if (animalsAtP.size() < 2) {
                continue;
            }
            List<HabsburgAnimal> males = new ArrayList<>();
            List<HabsburgAnimal> females = new ArrayList<>();
            for (Animal animal : animalsAtP) {
                if (animal.getEnergy() >= energyNeeded && animal instanceof HabsburgAnimal hAnimal) {
                    if (hAnimal.getSex() == AnimalSex.MALE) {
                        males.add(hAnimal);
                    } else {
                        females.add(hAnimal);
                    }
                }
            }
            if (males.isEmpty() || females.isEmpty()) {
                continue;
            }
            males.sort(animalComparator);
            females.sort(animalComparator);

            HabsburgAnimal father = males.getFirst();
            HabsburgAnimal mother = females.getFirst();

            HabsburgAnimal child = new HabsburgAnimal(father.getPosition(), father, mother,
                    PRNG.nextInt(minMut, maxMut + 1), energyLost * 2, currentDay);
            children.add(child);
        }

        for (Animal animal : children) {
            place(animal);
        }
        return children;
    }

}
