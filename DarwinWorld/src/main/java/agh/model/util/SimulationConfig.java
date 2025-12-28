package agh.model.util;

public record SimulationConfig(int mapWidth,
                               int mapHeight,
                               boolean habsburgsOn,
                               int startGrassesCount,
                               int energyFromEatingGrass,
                               int newGrassesDaily,
                               int startAnimalCount,
                               int startEnergy,
                               int energyLostDaily,
                               int energyNeededToReproduce,
                               int energyLostToReproduce,
                               int minimalMutationCount,
                               int maximalMutationCount,
                               int genotypeLength,
                               int timeBetweenDays) {
}
