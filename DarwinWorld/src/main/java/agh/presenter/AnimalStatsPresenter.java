package agh.presenter;

import agh.model.AnimalListener;
import agh.model.Vector2d;
import agh.model.util.AnimalStats;
import agh.model.util.Genotype;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class AnimalStatsPresenter implements AnimalListener {

    @FXML
    private Label positionLabel;
    @FXML
    private Label genotypeLabel;
    @FXML
    private Label energy;
    @FXML
    private Label offspring;
    @FXML
    private Label eatenGrass;
    @FXML
    private Label age;
    @FXML
    private Label dayOfDead;

    @Override
    public void animalChanged(AnimalStats stats, Genotype genotype, int descendantCount, Vector2d position) {
        Platform.runLater(() -> updateStats(stats,genotype,descendantCount,position));
    }

    public void updateStats(AnimalStats stats,Genotype genotype,int descendantCount,Vector2d position){
        positionLabel.setText(String.valueOf(position));
        genotypeLabel.setText(genotype+" ("+genotype.getActiveGen()+")");
        energy.setText(String.valueOf(stats.getEnergy()));
        offspring.setText(String.valueOf(stats.getChildrenCount())+" / "+String.valueOf(descendantCount) );
        eatenGrass.setText(String.valueOf(stats.getGrassesEaten()));
        age.setText(String.valueOf(stats.getAge()));
        dayOfDead.setText(stats.getDeathDate().isPresent() ? String.valueOf(stats.getDeathDate().get()) : "-");
    }

}
