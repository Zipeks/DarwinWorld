package agh.model;

import agh.model.util.SimulationStats;

public interface StatsListener {
        void statsChanged(SimulationStats stats);
}
