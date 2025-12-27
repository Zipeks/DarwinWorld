package agh.model.util;

import agh.model.Animal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Genotype {
    private static final Random PRNG = new Random();
    private final List<Integer> genes;
    private int activeGeneIdx;

    public Genotype(int size) {
        genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(PRNG.nextInt(8));
        }
        activeGeneIdx = PRNG.nextInt(size);
    }

    public Genotype(Animal parentOne, Animal parentTwo, int mutationsCnt) {
        List<Integer> youngGenes = combineTwoGenotypes(parentOne, parentTwo);
        applyMutations(youngGenes, mutationsCnt);
        genes = youngGenes;
        activeGeneIdx = PRNG.nextInt(genes.size());
    }

    private List<Integer> combineTwoGenotypes(Animal parentOne, Animal parentTwo) {
        Animal strong = parentOne.getEnergy() >= parentTwo.getEnergy() ? parentOne : parentTwo;
        Animal weak = parentOne.getEnergy() >= parentTwo.getEnergy() ? parentTwo : parentOne;

        int totalEnergy = strong.getEnergy() + weak.getEnergy();
        List<Integer> strongGenes = strong.getGenotype().getGenes();
        List<Integer> weakGenes = weak.getGenotype().getGenes();
        int genesLength = strongGenes.size();

        int splitPoint = (int) Math.floor(((double) strong.getEnergy() / totalEnergy) * genesLength);
        List<Integer> childGenes = new ArrayList<>();
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
    public void applyMutations(List<Integer> youngAnimalGenes, int mutationsCnt) {
        int size = youngAnimalGenes.size();
        if (mutationsCnt > size) mutationsCnt = size;

        HashSet<Integer> indexesToMutate = new HashSet<>();
        while (indexesToMutate.size() < mutationsCnt) {
            indexesToMutate.add(PRNG.nextInt(size));
        }

        for (int idx : indexesToMutate) {
            int oldGene = youngAnimalGenes.get(idx);
            int newGene;
            do {
                newGene = PRNG.nextInt(8);
            } while (newGene == oldGene);

            youngAnimalGenes.set(idx, newGene);
        }
    }

    public int next() {
        int currentGene = genes.get(activeGeneIdx);
        activeGeneIdx = (activeGeneIdx + 1) % genes.size();
        return currentGene;
    }
    public List<Integer> getGenes() {
        return new ArrayList<>(genes);
    }
}
