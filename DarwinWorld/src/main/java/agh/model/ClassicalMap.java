package agh.model;

import agh.model.util.Boundary;

import java.util.*;

public class ClassicalMap extends AbstractJungleMap {

    public ClassicalMap(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }


    @Override
    public List<Animal> animalReproduction(int energyNeededToReproduce, int energyLostToReproduction, int minMutations, int maxMutation, int date) {
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
                Animal child = new Animal(position, firstAnimal, secondAnimal,
                        PRNG.nextInt(minMutations, maxMutation + 1), energyLostToReproduction * 2, date);

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