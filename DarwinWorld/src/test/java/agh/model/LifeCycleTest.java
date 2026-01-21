package agh.model;

import agh.model.util.Genotype;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LifeCycleTest {

    @Test
    void deadAnimalsShouldBeRemovedFromMap() {
        ClassicalMap map = new ClassicalMap(0, 10, 10);
        Vector2d pos = new Vector2d(1, 1);

        Animal alive = new Animal(pos, new Genotype(5), 10, 0);
        Animal dead = new Animal(pos, new Genotype(5), 0, 0);

        map.place(alive);
        map.place(dead);

        map.removeDeadAnimals(5);

        assertEquals(1, map.animalsAt(pos).size());
        assertEquals(alive, map.animalsAt(pos).get(0));

        assertTrue(dead.getStats().getDeathDate().isPresent());
        assertEquals(5, dead.getStats().getDeathDate().get());
    }

    @Test
    void animalShouldDieAtZeroEnergy() {
        ClassicalMap map = new ClassicalMap(0, 10, 10);
        Animal animal = new Animal(new Vector2d(0,0), new Genotype(5), 1, 0);
        map.place(animal);

        map.moveAnimals(2);

        assertTrue(animal.getEnergy() <= 0);

        map.removeDeadAnimals(1);

        assertEquals(0, map.animalsAt(new Vector2d(0,0)).size());
    }
}