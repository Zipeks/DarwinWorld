package agh.model.util;

import agh.model.Animal;
import agh.model.Gene;

import java.util.*;

public class Genotype {
    private static final Random PRNG = new Random();
    private final List<Gene> genes;
    private int activeGeneIdx;
    private Integer cachedHashCode = null;

    public Genotype(int size) {
        genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(Gene.randomGene());
        }
        activeGeneIdx = PRNG.nextInt(size);
    }

    public Genotype(Genotype genotype) {
        genes = genotype.getGenes();
        activeGeneIdx = PRNG.nextInt(genes.size());
    }

    public Genotype(Animal parentOne, Animal parentTwo, int mutationsCnt) {
        List<Gene> youngGenes = combineTwoGenotypes(parentOne, parentTwo);
        applyMutations(youngGenes, mutationsCnt);
        genes = youngGenes;
        activeGeneIdx = PRNG.nextInt(genes.size());
    }

    private List<Gene> combineTwoGenotypes(Animal parentOne, Animal parentTwo) {
        Animal strong = parentOne.getEnergy() >= parentTwo.getEnergy() ? parentOne : parentTwo;
        Animal weak = parentOne.getEnergy() >= parentTwo.getEnergy() ? parentTwo : parentOne;

        int totalEnergy = strong.getEnergy() + weak.getEnergy();
        List<Gene> strongGenes = strong.getGenotype().getGenes();
        List<Gene> weakGenes = weak.getGenotype().getGenes();
        int genesLength = strongGenes.size();

        int splitPoint = (int) Math.floor(((double) strong.getEnergy() / totalEnergy) * genesLength);
        List<Gene> childGenes = new ArrayList<>();
        if (PRNG.nextInt(2) > 0) {
            childGenes.addAll(strongGenes.subList(0, splitPoint));
            childGenes.addAll(weakGenes.subList(splitPoint, genesLength));
        } else {
            int breakPoint = genesLength - splitPoint;
            childGenes.addAll(weakGenes.subList(0, breakPoint));
            childGenes.addAll(strongGenes.subList(breakPoint, genesLength));
        }
        return childGenes;
    }

    public void applyMutations(List<Gene> youngAnimalGenes, int mutationsCnt) {
        int size = youngAnimalGenes.size();
        if (mutationsCnt > size) mutationsCnt = size;

        HashSet<Integer> indexesToMutate = new HashSet<>();
        while (indexesToMutate.size() < mutationsCnt) {
            indexesToMutate.add(PRNG.nextInt(size));
        }

        for (int idx : indexesToMutate) {
            Gene oldGene = youngAnimalGenes.get(idx);
            Gene newGene;
            do {
                newGene = Gene.randomGene();
            } while (newGene == oldGene);

            youngAnimalGenes.set(idx, newGene);
        }
    }

    public static double kinshipLevel(Animal animalOne, Animal animalTwo) {
        List<Gene> genesOne = animalOne.getGenotype().getGenes();
        List<Gene> genesTwo = animalTwo.getGenotype().getGenes();
        int repeatingGenes = 0;
        for (int i = 0; i < genesOne.size(); i++) {
            if (genesOne.get(i) == genesTwo.get(i)) {
                repeatingGenes++;
            }
        }
        return (double) repeatingGenes / genesOne.size();
    }

    public int next() {
        int currentGene = genes.get(activeGeneIdx).numericValue();
        activeGeneIdx = (activeGeneIdx + 1) % genes.size();
        return currentGene;
    }

    public List<Gene> getGenes() {
        return new ArrayList<>(genes);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return Objects.equals(genes, genotype.genes);
    }

    @Override
    public int hashCode() {
        if (cachedHashCode == null) {
            cachedHashCode = Objects.hashCode(genes);
        }
        return cachedHashCode;
    }

    @Override
    public String toString() {
        return genes.toString();
    }

    public int getActiveGen() {
        return activeGeneIdx;
    }


}
