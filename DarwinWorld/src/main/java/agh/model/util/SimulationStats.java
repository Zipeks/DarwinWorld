package agh.model.util;

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
}
