package agh.model;

import agh.model.util.Genotype;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalBiologicalProcesesTest {

    @Test
    void movingShouldConsumeEnergy() {
        ClassicalMap map = new ClassicalMap(0, 10, 10);

        int startEnergy = 100;
        int moveCost = 5;
        Animal animal = new Animal(new Vector2d(5, 5), new Genotype(5), startEnergy, 0);

        animal.move(map, moveCost);

        assertEquals(startEnergy - moveCost, animal.getEnergy());
    }

    @Test
    void animalShouldEatGrassAndGainEnergy() {
        int width = 5;
        int height = 5;
        int totalFields = width * height;
        ClassicalMap map = new ClassicalMap(0, width, height);

        map.placeGrasses(totalFields + 1);

        assertEquals(totalFields, map.getGrassCount());

        Vector2d position = new Vector2d(2, 2);
        int startEnergy = 50;
        int grassEnergy = 20;
        Animal animal = new Animal(position, new Genotype(5), startEnergy, 0);
        map.place(animal);

        map.grassConsumption(grassEnergy);

        assertEquals(startEnergy + grassEnergy, animal.getEnergy());

        assertEquals(totalFields - 1, map.getGrassCount());

        assertEquals(1, animal.getStats().getGrassesEaten());
    }
}