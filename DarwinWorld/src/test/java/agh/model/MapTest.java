package agh.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapTest {
    private ClassicalMap map;

    @BeforeEach
    void setUp() {
        map = new ClassicalMap(0, 10, 10);
    }

    @Test
    void testGlobeWrappingWestToEast() {
        Vector2d startPos = new Vector2d(0, 5);
        Vector2d moveVector = new Vector2d(-1, 0);

        Vector2d newPos = map.moveOnMap(startPos, moveVector);

        assertEquals(new Vector2d(9, 5), newPos);
    }

    @Test
    void testGlobeWrappingEastToWest() {
        Vector2d startPos = new Vector2d(9, 5);
        Vector2d moveVector = new Vector2d(1, 0);

        Vector2d newPos = map.moveOnMap(startPos, moveVector);

        assertEquals(new Vector2d(0, 5), newPos);
    }

    @Test
    void testPoleBouncingNorth() {
        Vector2d startPos = new Vector2d(5, 9);
        Vector2d moveVector = new Vector2d(0, 1);

        Vector2d newPos = map.moveOnMap(startPos, moveVector);

        assertEquals(8, newPos.getY());
        assertEquals(5, newPos.getX());
    }

    @Test
    void testPoleBouncingSouth() {
        Vector2d startPos = new Vector2d(5, 0);
        Vector2d moveVector = new Vector2d(0, -1);

        Vector2d newPos = map.moveOnMap(startPos, moveVector);

        assertEquals(1, newPos.getY());
        assertEquals(5, newPos.getX());
    }

}