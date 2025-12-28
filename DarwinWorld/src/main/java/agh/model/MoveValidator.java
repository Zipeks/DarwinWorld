package agh.model;

import agh.model.Vector2d;

public interface MoveValidator {
    Vector2d moveOnMap(Vector2d position, Vector2d moveVector);
}