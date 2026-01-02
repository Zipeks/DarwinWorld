package agh.model.util;

public class SimulationStats {
    Genotype mostPopularGenotype;
    private int animalsCount=0;
    private int grassesCount = 0;
    private int emptyFields = 0;
    private int avgEnergyLevel = 0;
    private int avgLifeTime = 0;
    private int avgChildCount = 0;
    private int currentDate = 0;

    public Genotype getMostPopularGenotype() {
        return mostPopularGenotype;
    }

    public void setMostPopularGenotype(Genotype mostPopularGenotype) {
        this.mostPopularGenotype = mostPopularGenotype;
    }

    public int getGrassesCount() {
        return grassesCount;
    }

    public void setGrassesCount(int grassesCount) {
        this.grassesCount = grassesCount;
    }

    public int getAnimalsCount() { return animalsCount; }

    public void setAnimalsCount(int animalsCount) {
        this.animalsCount = animalsCount;
    }

    public int getEmptyFields() {
        return emptyFields;
    }

    public void setEmptyFields(int emptyFields) {
        this.emptyFields = emptyFields;
    }

    public int getAvgEnergyLevel() {
        return avgEnergyLevel;
    }

    public void setAvgEnergyLevel(int avgEnergyLevel) {
        this.avgEnergyLevel = avgEnergyLevel;
    }

    public int getAvgLifeTime() {
        return avgLifeTime;
    }

    public void setAvgLifeTime(int avgLifeTime) {
        this.avgLifeTime = avgLifeTime;
    }

    public int getAvgChildCount() {
        return avgChildCount;
    }

    public void setAvgChildCount(int avgChildCount) {
        this.avgChildCount = avgChildCount;
    }

    public int getCurrentDate() {
        return currentDate;
    }

    public void increaseCurrentDate() {
        currentDate++;
    }
}
