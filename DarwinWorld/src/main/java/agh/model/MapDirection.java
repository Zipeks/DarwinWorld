package agh.model;

import agh.model.Vector2d;

public enum MapDirection {
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private static final MapDirection[] directions = values();
    private final String direction;

    MapDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return direction;
    }

    public MapDirection next() {
        return directions[(this.ordinal() + 1) % directions.length];
    }

    public MapDirection previous() {
        return directions[(this.ordinal() + directions.length - 1) % directions.length];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
        };
    }
}
