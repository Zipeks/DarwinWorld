package agh.model;

import agh.model.util.SimulationConfig;
import agh.model.util.Genotype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabsburgReproductionTest {

    private SimulationConfig config;
    private HabsburgMap map;

    @BeforeEach
    void setUp() {
        config = new SimulationConfig(
                10, 10, 0, 0, 0, 0,
                100, 1, 10, 5,
                0, 0, 5, 100,
                true, 5, 5, 10
        );
        map = new HabsburgMap(0, 10, 10);
    }

    @Test
    void twoMalesShouldNotReproduce() {
        Vector2d pos = new Vector2d(2, 2);
        HabsburgAnimal m1 = new HabsburgAnimal(pos, new Genotype(5), 50, 0, AnimalSex.MALE);
        HabsburgAnimal m2 = new HabsburgAnimal(pos, new Genotype(5), 50, 0, AnimalSex.MALE);

        map.place(m1);
        map.place(m2);

        List<Animal> children = map.animalReproduction(config, 10);

        assertTrue(children.isEmpty());
    }

    @Test
    void twoFemalesShouldNotReproduce() {
        Vector2d pos = new Vector2d(2, 2);
        HabsburgAnimal f1 = new HabsburgAnimal(pos, new Genotype(5), 50, 0, AnimalSex.FEMALE);
        HabsburgAnimal f2 = new HabsburgAnimal(pos, new Genotype(5), 50, 0, AnimalSex.FEMALE);

        map.place(f1);
        map.place(f2);

        List<Animal> children = map.animalReproduction(config, 10);

        assertTrue(children.isEmpty());
    }

    @Test
    void maleAndFemaleShouldReproduce() {
        Vector2d pos = new Vector2d(2, 2);
        HabsburgAnimal m = new HabsburgAnimal(pos, new Genotype(100), 50, 0, AnimalSex.MALE);
        HabsburgAnimal f = new HabsburgAnimal(pos, new Genotype(100), 50, 0, AnimalSex.FEMALE);

        map.place(m);
        map.place(f);

        List<Animal> children = map.animalReproduction(config, 10);

        assertEquals(1, children.size());
        assertInstanceOf(HabsburgAnimal.class, children.getFirst());
        if (Genotype.kinshipLevel(m, f) < 0.25) {
            assertEquals(config.energyLostToReproduce() * 2, children.getFirst().getEnergy());
        }
    }

    @Test
    void penaltyForInbreading() {
        Vector2d pos = new Vector2d(2, 2);
        Genotype genotype = new Genotype(5);
        HabsburgAnimal m = new HabsburgAnimal(pos, new Genotype(genotype), 50, 0, AnimalSex.MALE);
        HabsburgAnimal f = new HabsburgAnimal(pos, new Genotype(genotype), 50, 0, AnimalSex.FEMALE);

        map.place(m);
        map.place(f);

        List<Animal> children = map.animalReproduction(config, 10);

        assertEquals(config.energyLostToReproduce() * 2 * (1 - config.inbreedingPenalty() / 100), children.getFirst().getEnergy());
    }
}