package agh.model;

import agh.model.util.SimulationConfig;
import agh.model.util.Genotype;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReproductionTest {

    @Test
    void reproductionShouldCostEnergyAndCreateChild() {
        SimulationConfig config = new SimulationConfig(
                10, 10, 0, 0, 0, 0,
                100, 1, 20, 10,
                0, 0, 5, 100
        );
        ClassicalMap map = new ClassicalMap(0, 10, 10);
        Vector2d pos = new Vector2d(3, 3);

        Animal p1 = new Animal(pos, new Genotype(5), 50, 0);
        Animal p2 = new Animal(pos, new Genotype(5), 60, 0);

        map.place(p1);
        map.place(p2);

        List<Animal> children = map.animalReproduction(config, 10);

        assertEquals(1, children.size());
        assertEquals(3, map.animalsAt(pos).size());

        assertEquals(40, p1.getEnergy());
        assertEquals(50, p2.getEnergy());

        Animal child = children.get(0);
        assertEquals(20, child.getEnergy());

        assertTrue(p1.getChildren().contains(child));
        assertTrue(p2.getChildren().contains(child));
    }

    @Test
    void animalsShouldNotReproduceWithoutEnoughEnergy() {
        SimulationConfig config = new SimulationConfig(
                10, 10, 0, 0, 0, 0,
                100, 1, 100, 10,
                0, 0, 5, 100
        );
        ClassicalMap map = new ClassicalMap(0, 10, 10);
        Vector2d pos = new Vector2d(3, 3);

        Animal p1 = new Animal(pos, new Genotype(5), 50, 0);
        Animal p2 = new Animal(pos, new Genotype(5), 150, 0);

        map.place(p1);
        map.place(p2);

        List<Animal> children = map.animalReproduction(config, 10);

        assertTrue(children.isEmpty());
    }
}