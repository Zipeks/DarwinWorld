package agh.model;

import agh.model.util.Boundary;

import java.util.List;

public class HabsburgMap extends AbstractJungleMap {
    public HabsburgMap(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }

    @Override
    public List<Animal> animalReproduction(int energyNeeded, int energyLost, int minMut, int maxMut, int currentDay) {
        return List.of();
    }
}
