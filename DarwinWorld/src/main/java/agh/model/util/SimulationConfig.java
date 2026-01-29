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

    public SimulationConfig(JsonObject obj) {
        this(obj.getInt("mapWidth", 0),
                obj.getInt("mapHeight", 0),
                obj.getInt("plants", 0),
                obj.getInt("energyFromGrass", 0),
                obj.getInt("dailyInc", 0),
                obj.getInt("animals", 0),
                obj.getInt("energy", 0),
                obj.getInt("energyLoss", 0),
                obj.getInt("fertility", 0),
                obj.getInt("reproduction", 0),
                obj.getInt("mutationMinValue", 0),
                obj.getInt("mutationMaxValue", 0),
                obj.getInt("genomLength", 0),
                obj.getInt("day", 0),
                obj.getBoolean("isHabsburg", false),
                obj.getInt("startingMales", 0),
                obj.getInt("startingFemales", 0),
                obj.getInt("inbreedingPenalty", 0)
        );
    }

    public void validate() throws InvalidConfigException {
        if (this.mapHeight() < 3 || this.mapWidth() < 3 || this.mapHeight() > 100 || this.mapWidth() > 200)
            throw new InvalidConfigException("Allowed map size is 3x3 to 200x100");

        if (this.startGrassesCount() < 0 || this.startGrassesCount() > this.mapWidth() * this.mapHeight())
            throw new InvalidConfigException("Grass count must be non-negative and cannot exceed the number of map fields");

        if (this.energyFromEatingGrass() < 0)
            throw new InvalidConfigException("Energy gain cannot be negative");

        if (this.newGrassesDaily() < 0 || this.newGrassesDaily > (int) (0.2 * this.mapWidth * this.mapHeight))
            throw new InvalidConfigException("Daily grass growth must be non-negative and cannot exceed 20% of the map");

        if (this.startAnimalCount() < 0)
            throw new InvalidConfigException("Animal count cannot be negative");

        if (this.startAnimalCount() > this.mapWidth() * this.mapHeight())
            throw new InvalidConfigException("Animal count cannot exceed the map area");

        if (this.startEnergy() < 0)
            throw new InvalidConfigException("Starting energy cannot be negative");

        if (this.energyLostDaily() < 0)
            throw new InvalidConfigException("Daily energy loss cannot be negative");

        if (this.energyNeededToReproduce() < 0)
            throw new InvalidConfigException("Energy required for reproduction cannot be negative");

        if (this.energyLostToReproduce() < 0)
            throw new InvalidConfigException("Energy lost during reproduction cannot be negative");

        if (this.energyNeededToReproduce() < this.energyLostToReproduce())
            throw new InvalidConfigException("Energy required for reproduction cannot be lower than energy lost");

        if (this.minimalMutationCount() < 0)
            throw new InvalidConfigException("Minimum mutation count cannot be negative");

        if (this.maximalMutationCount() < 0)
            throw new InvalidConfigException("Maximum mutation count cannot be negative");

        if (this.maximalMutationCount() < this.minimalMutationCount())
            throw new InvalidConfigException("Maximum mutation count cannot be lower than minimum mutation count");

        if (this.genotypeLength() <= 0)
            throw new InvalidConfigException("Minimum genotype length is 1");

        if (this.maximalMutationCount() > this.genotypeLength())
            throw new InvalidConfigException("Maximum mutation count cannot exceed genotype length");

        if (this.timeBetweenDays() < 100)
            throw new InvalidConfigException("Minimum day duration is 100ms");

        if (this.habsburgsOn() && this.startingMales() < 0)
            throw new InvalidConfigException("Male count cannot be negative");

        if (this.habsburgsOn() && this.startingFemales() < 0)
            throw new InvalidConfigException("Female count cannot be negative");

        if (this.habsburgsOn() && this.startingFemales() + this.startingMales() != this.startAnimalCount())
            throw new InvalidConfigException("The sum of males and females must equal the total animal count");

        if (this.habsburgsOn() && this.inbreedingPenalty() < 0)
            throw new InvalidConfigException("Inbreeding energy penalty cannot be negative");
    }

    public JsonObject toJson() {
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
                .add("startingMales", this.startingMales())
                .add("startingFemales", this.startingFemales())
                .add("inbreedingPenalty", this.inbreedingPenalty())
                .build();
    }


}
