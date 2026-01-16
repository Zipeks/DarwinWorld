package agh.model.util;

import agh.model.InvalidConfigException;
import agh.model.filesManager.JsonLoader;

import javax.json.Json;
import javax.json.JsonObject;

public record SimulationConfig(int mapWidth,
                               int mapHeight,
                               int startGrassesCount,
                               int energyFromEatingGrass,
                               int newGrassesDaily,
                               int startAnimalCount,
                               int startEnergy,
                               int energyLostDaily,
                               int energyNeededToReproduce,
                               int energyLostToReproduce,
                               int minimalMutationCount,
                               int maximalMutationCount,
                               int genotypeLength,
                               int timeBetweenDays,
                               boolean habsburgsOn,
                               int startingMales,
                               int startingFemales,
                               int inbreedingPenalty) {

    public SimulationConfig(int mapWidth,
                            int mapHeight,
                            int startGrassesCount,
                            int energyFromEatingGrass,
                            int newGrassesDaily,
                            int startAnimalCount,
                            int startEnergy,
                            int energyLostDaily,
                            int energyNeededToReproduce,
                            int energyLostToReproduce,
                            int minimalMutationCount,
                            int maximalMutationCount,
                            int genotypeLength,
                            int timeBetweenDays) {
        this(mapWidth, mapHeight, startGrassesCount,
                energyFromEatingGrass,
                newGrassesDaily,
                startAnimalCount,
                startEnergy,
                energyLostDaily,
                energyNeededToReproduce,
                energyLostToReproduce,
                minimalMutationCount,
                maximalMutationCount,
                genotypeLength,
                timeBetweenDays,
                false, 0, 0, 0);
    }

    public SimulationConfig(JsonObject obj){
        this(obj.getInt("mapWidth",0),
            obj.getInt("mapHeight",0),
            obj.getInt("plants",0),
            obj.getInt("energyFromGrass",0),
            obj.getInt("dailyInc",0),
            obj.getInt("animals",0),
            obj.getInt("energy",0),
            obj.getInt("energyLoss",0),
            obj.getInt("fertility",0),
            obj.getInt("reproduction",0),
            obj.getInt("mutationMinValue",0),
            obj.getInt("mutationMaxValue",0),
            obj.getInt("genomLength",0),
            obj.getInt("day",0),
            obj.getBoolean("isHabsburg",false),
            obj.getInt("startingMales",0),
            obj.getInt("startingFemales",0),
            obj.getInt("inbreedingPenalty",0)
        );
    }

    public void validate() throws InvalidConfigException {
        if(this.mapHeight()<3 || this.mapWidth()<3 || this.mapHeight()>80 || this.mapWidth()>160)
            throw new InvalidConfigException("Dopusczalny rozmiar 3x3 - 160x80");
        if(this.startGrassesCount()<0 || this.startGrassesCount()>this.mapWidth()*this.mapHeight())
            throw new InvalidConfigException("Dopuszczalna ilość trawy jest dodatnia i nie większa od ilości pól na mapie");
        if(this.energyFromEatingGrass()<0)
            throw new InvalidConfigException("Przyrost energii nie może być ujemny");
        if(this.newGrassesDaily()<0)
            throw new InvalidConfigException("Dzienny przyrost roślin nie może być ujemny");
        if(this.startAnimalCount()<0)
            throw new InvalidConfigException("Liczba zwierząt nie może być ujemna");
        if(this.startEnergy()<0)
            throw new InvalidConfigException("Energia początkowa nie może być ujemna");
        if(this.energyLostDaily()<0)
            throw new InvalidConfigException("Strata energii nie może być ujemna");
        if(this.energyNeededToReproduce()<0)
            throw new InvalidConfigException("Energia potrzebna do rozmnażania nie może być ujemna");
        if(this.energyLostToReproduce()<0)
            throw new InvalidConfigException("Energia tracona przy rozmnażaniu nie może być ujemna");
        if(this.energyNeededToReproduce()<this.energyLostToReproduce())
            throw new InvalidConfigException("Energia potrzebna do rozmnażania nie może być mniejsza od energii traconej");
        if(this.minimalMutationCount()<0)
            throw new InvalidConfigException("Minimalna liczba mutacji nie może być ujemna");
        if(this.maximalMutationCount()<0)
            throw new InvalidConfigException("Maksymalna liczba mutacji nie może być ujemna");
        if(this.maximalMutationCount()<this.minimalMutationCount())
            throw new InvalidConfigException("Maksymalna liczba mutacji nie może być mniejsza od minimalnej");
        if(this.genotypeLength()<=0)
            throw new InvalidConfigException("Minimalna długość genotypu wynosi 1");
        if(this.maximalMutationCount()>this.genotypeLength())
            throw new InvalidConfigException("Maksymalna liczba mutacji nie może być większa od długości genotypu");
        if(this.timeBetweenDays()<100)
            throw new InvalidConfigException("Minimalny czas trwania dnia to 100ms");
        if(this.habsburgsOn() && this.startingMales()<0)
            throw new InvalidConfigException("Minimalna ilość samców nie może być ujemna");
        if(this.habsburgsOn() && this.startingFemales()<0)
            throw new InvalidConfigException("Minimalna ilość samic nie może być ujemna");
        if(this.habsburgsOn() && this.startingFemales()+this.startingMales()!=this.startAnimalCount())
            throw new InvalidConfigException("Suma samic i samców musi być równa liczbie zwierząt");
        if(this.habsburgsOn() && this.inbreedingPenalty()<0)
            throw new InvalidConfigException("Strata energii przy pokrewieństwie nie może być ujemna");
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("mapWidth", this.mapWidth())
                .add("mapHeight", this.mapHeight())
                .add("energy", this.startEnergy())
                .add("reproduction", this.energyLostToReproduce())
                .add("genomLength", this.genotypeLength())
                .add("energyLoss", this.energyLostDaily())
                .add("energyFromGrass", this.energyFromEatingGrass())
                .add("mutationMinValue", this.minimalMutationCount())
                .add("mutationMaxValue", this.maximalMutationCount())
                .add("animals", this.startAnimalCount())
                .add("fertility", this.energyNeededToReproduce())
                .add("plants", this.startGrassesCount())
                .add("dailyInc", this.newGrassesDaily())
                .add("day", this.timeBetweenDays())
                .add("isHabsburg", this.habsburgsOn())
                .add("startingMales",this.startingMales())
                .add("startingFemales",this.startingFemales())
                .add("inbreedingPenalty",this.inbreedingPenalty())
                .build();
    }



}
