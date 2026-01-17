package agh.model;

import agh.model.util.AnimalStats;
import agh.model.util.Genotype;

public interface AnimalListener {
    void animalChanged(AnimalStats stats, Genotype genotype, int descendantCount, Vector2d position);
    void animalChanged(AnimalStats stats, Genotype genotype, int descendantCount, Vector2d position,AnimalSex animalSex);
}
