package agh.model;

import java.util.Objects;

public class Animal implements WorldElement {
    private MapDirection direction;
    private Vector2d position;

    public Animal(Vector2d positon) {
        this.direction = MapDirection.NORTH;
        this.position = positon;
    }

    public Animal() {
        this(new Vector2d(2, 2));
    }

    public MapDirection getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return direction.toString();
    }

    public boolean isAt(Vector2d otherPosition) {
        return Objects.equals(position, otherPosition);
    }

    public void move(MoveDirection dir, MoveValidator moveValidator) {
        switch (dir) {
            case RIGHT -> direction = direction.next();
            case LEFT -> direction = direction.previous();
            case FORWARD -> {
                Vector2d v = position.add(direction.toUnitVector());
                if (moveValidator.canMoveTo(v))
                    position = v;
            }
            case BACKWARD -> {
                Vector2d v = position.add(direction.toUnitVector().opposite());
                if (moveValidator.canMoveTo(v))
                    position = v;
            }
        }
    }
}
