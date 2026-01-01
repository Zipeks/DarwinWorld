package agh.model.util;

import java.util.Optional;

public class AnimalStats {
    private final int birthDate;
    private int grassesEaten = 0;
    private Integer deathDate = null;
    private int age;
    private int energy = 0;
    private int childrenCount = 0;

    public AnimalStats(int birthDate) {
        this.birthDate = birthDate;
    }

    public int getGrassesEaten() {
        return grassesEaten;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return age;
    }
    public void increaseAge() {
        age++;
    }

    public Optional<Integer> getDeathDate() {
        return Optional.ofNullable(deathDate);
    }

    public void setDeathDate(Integer date) {
        deathDate = date;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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
