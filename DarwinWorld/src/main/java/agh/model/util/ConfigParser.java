package agh.model.util;

import javax.json.JsonObject;

public class ConfigParser {

    public static SimulationConfig jsonToConfig(JsonObject obj){
        int mapWidth = obj.getInt("mapWidth",0);
        int mapHeight = obj.getInt("mapHeight",0);
        int energy = obj.getInt("energy",0);
        int reproduction = obj.getInt("reproduction",0);
        int genomLength = obj.getInt("genomLength",0);
        int energyLossValue = obj.getInt("energyLoss",0);
        int energyFromGrass = obj.getInt("energyFromGrass",0);
        int mutationMinValue = obj.getInt("mutationMinValue",0);
        int mutationMaxValue = obj.getInt("mutationMaxValue",0);
        int animals = obj.getInt("animals",0);
        int fertility = obj.getInt("fertility",0);
        int plants = obj.getInt("plants",0);
        int dailyInc = obj.getInt("dailyInc",0);
        int day = obj.getInt("day",0);
        boolean isHabsburg = obj.getBoolean("isHabsburg",false);
        int startingMales = obj.getInt("startingMales",0);
        int startingFemales = obj.getInt("startingFemales",0);
        int inbreedingPenalty = obj.getInt("inbreedingPenalty",0);
        return new SimulationConfig(mapWidth, mapHeight,
                plants, energyFromGrass, dailyInc, animals, energy, energyLossValue,
                fertility, reproduction, mutationMinValue, mutationMaxValue,
                genomLength, day, isHabsburg, startingMales, startingFemales, inbreedingPenalty);
    }

}
