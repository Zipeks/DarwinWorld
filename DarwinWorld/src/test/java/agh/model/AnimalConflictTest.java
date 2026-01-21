package agh.model;

import agh.model.util.Genotype;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalConflictTest {

    @Test
    void animalsShouldBeSortedByEnergyAgeChildren() {
        ClassicalMap map = new ClassicalMap(0, 10, 10);
        Vector2d pos = new Vector2d(2, 2);
        Genotype genotype = new Genotype(5);

        Animal weakEnergy = new Animal(pos, genotype, 90, 0);
        for (int i = 0; i < 10; i++) weakEnergy.increaseAge();

        Animal strongYoung = new Animal(pos, genotype, 100, 0);

        Animal strongOldChildless = new Animal(pos, genotype, 100, 0);
        strongOldChildless.increaseAge();
        strongOldChildless.increaseAge();

        Animal strongOldParent = new Animal(pos, genotype, 100, 0);
        strongOldParent.increaseAge();
        strongOldParent.increaseAge();

        strongOldParent.addChild(new Animal(pos, genotype, 10, 0), 0);
        strongOldParent.addChild(new Animal(pos, genotype, 10, 0), 0);


        map.place(weakEnergy);
        map.place(strongYoung);
        map.place(strongOldChildless);
        map.place(strongOldParent);


        List<Animal> animalsAtPos = map.animalsAt(pos);

        map.sortAnimals(animalsAtPos);

        assertAll(
                () -> assertEquals(strongOldParent, animalsAtPos.get(0)),
                () -> assertEquals(strongOldChildless, animalsAtPos.get(1)),
                () -> assertEquals(strongYoung, animalsAtPos.get(2)),
                () -> assertEquals(weakEnergy, animalsAtPos.get(3))
        );
    }
}