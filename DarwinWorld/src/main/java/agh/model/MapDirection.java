package agh.model;

import agh.model.Vector2d;

import java.util.Random;

public enum MapDirection {
    NORTH("N"),
    NORTH_EAST("NE"),
    EAST("E"),
    SOUTH_EAST("SE"),
    SOUTH("S"),
    SOUTH_WEST("SW"),
    WEST("W"),
    NORTH_WEST("NW");

    private static final MapDirection[] directions = values();
    private static final Random PRNG = new Random();

    private final String direction;

    MapDirection(String direction) {
        this.direction = direction;
    }

    public static MapDirection randomDirection() {
        return directions[PRNG.nextInt(directions.length)];
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
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }
}
