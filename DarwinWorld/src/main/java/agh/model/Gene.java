package agh.model;

import java.util.Random;

public enum Gene {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7);

    private static final Gene[] genes = values();
    private static final Random PRNG = new Random();

    private final int gene;

    Gene(int gene) {
        this.gene = gene;
    }

    public static Gene randomGene() {
        return genes[PRNG.nextInt(genes.length)];
    }

    public int numericValue() {
        return gene;
    }

    @Override
    public String toString() {
        return String.valueOf(gene);
    }
}
