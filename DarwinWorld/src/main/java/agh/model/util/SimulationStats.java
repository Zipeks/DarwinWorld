package agh.model.util;

import agh.model.Animal;

import java.util.List;

public record SimulationStats(
        int animalsCount,
        int grassesCount,
        int emptyFields,
        int avgEnergyLevel,
        int avgLifeTime,
        int avgChildCount,
        int currentDate,
        Genotype mostPopularGenotype)
{
    public static SimulationStats updateStats(List<Animal> animals, SimulationStats previousStats, MapStats mapStats) {
        int aliveAnimalsCount = 0;
        int deadAnimalsCount = 0;
        int totalDeadAnimalsLifeLength = 0;
        int totalAliveAnimalsEnergy = 0;
        Genotype mostPopularGenotype = null;
        int maxDescendants = 0;
        int totalAliveAnimalsChildrenCount = 0;
        int avgChildCount;
        int avgLifeTime = 0;
        for (Animal animal : animals) {
            if (animal.isAlive()) {
                aliveAnimalsCount++;
                totalAliveAnimalsEnergy += animal.getEnergy();
                int descendants = animal.getDescendantCount();
                if (maxDescendants < descendants) {
                    maxDescendants = descendants;
                    mostPopularGenotype = animal.getGenotype();
                }
                totalAliveAnimalsChildrenCount += animal.getChildrenCount();
                animal.increaseAge();
            } else {
                deadAnimalsCount++;
                totalDeadAnimalsLifeLength += animal.getAge();
            }
        }
        avgChildCount = (aliveAnimalsCount == 0 ? 0 : totalAliveAnimalsChildrenCount / aliveAnimalsCount);
        if (deadAnimalsCount > 0) {
            avgLifeTime = (totalDeadAnimalsLifeLength / deadAnimalsCount);
        }
        return  new SimulationStats(
                aliveAnimalsCount,
                mapStats.grassCount(),
                mapStats.emptyCells(),
                aliveAnimalsCount == 0 ? 0 : totalAliveAnimalsEnergy / aliveAnimalsCount,
                avgLifeTime,
                avgChildCount,
                previousStats.currentDate() + 1,
                mostPopularGenotype
        );
    }
}
