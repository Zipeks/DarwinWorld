package agh.model;

import agh.model.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}
