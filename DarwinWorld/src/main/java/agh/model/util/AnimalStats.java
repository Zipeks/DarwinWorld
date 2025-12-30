package agh.model.util;

public class AnimalStats {
    private int age = 0;
    private int grassesEaten = 0;
    private int deathDate;
    private int energy = 0;
    private int childrenCount = 0;

    public int getAge() {
        return age;
    }

    public int getGrassesEaten() {
        return grassesEaten;
    }

    public int getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(int date) {
        deathDate = date;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void increaseAge() {
        age++;
    }

    public void increaseGrassesEaten() {
        grassesEaten++;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void increaseChildrenCount() {
        childrenCount++;
    }
}
