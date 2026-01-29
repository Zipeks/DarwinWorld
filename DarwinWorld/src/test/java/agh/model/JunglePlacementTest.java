package agh.model;

import agh.model.util.Boundary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunglePlacementTest {

    @Test
    void jungleShouldBeInCenterAndOccupy20Percent() {
        ClassicalMap map = new ClassicalMap(0, 10, 20);
        Boundary jungle = map.getJungle();

        assertEquals(8, jungle.bottomLeft().getY());
        assertEquals(11, jungle.topRight().getY());

        assertEquals(0, jungle.bottomLeft().getX());
        assertEquals(9, jungle.topRight().getX());
    }

    @Test
    void jungleShouldHandleOddMapHeight() {
        ClassicalMap map = new ClassicalMap(0, 10, 15);
        Boundary jungle = map.getJungle();

        assertEquals(6, jungle.bottomLeft().getY());
        assertEquals(8, jungle.topRight().getY());
    }

}
