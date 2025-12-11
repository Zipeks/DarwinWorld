package agh.model;

import agh.model.util.Boundary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GrassField extends AbstractWorldMap {
    private final int grassCount;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GrassField(int grassCount) {
        this.grassCount = grassCount;
        placeGrasses();
    }

    public GrassField() {
        this(0);
    }

    private void placeGrasses() {
        Random rand = new Random();
        int range = (int) Math.sqrt(grassCount * 10);
        int i = grassCount;
        while (i > 0) {
            Vector2d grassPosition = new Vector2d(rand.nextInt(range), rand.nextInt(range));
            if (grasses.get(grassPosition) == null) {
                grasses.put(grassPosition, new Grass(grassPosition));
                i -= 1;
            }
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return super.objectAt(position) != null ? super.objectAt(position) : grasses.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> list = super.getElements();
        list.addAll(grasses.values());
        return list;
    }

    @Override
    public Boundary getCurrentBounds() {
        List<WorldElement> worldElements = getElements();
        Vector2d v1 = worldElements.getFirst().getPosition();
        Vector2d v2 = v1;
        for (WorldElement worldElement : worldElements) {
            v1 = v1.upperRight(worldElement.getPosition());
            v2 = v2.lowerLeft(worldElement.getPosition());
        }
        return new Boundary(v2, v1);
    }
}
