package agh.model;

import agh.model.util.Boundary;

import java.util.*;

public class JungleMap extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary boundary;
    private final Boundary jungle;
    private int grassCount;


    public JungleMap(int grassCount, Boundary boundary) {
        this.boundary = boundary;
        int height = boundary.topRight().getY() - boundary.bottomLeft().getY();
        int equator = Math.floorDiv(height, 2);
        int eq_ten_percent = Math.floorDiv(equator, 10);
        this.jungle = new Boundary(new Vector2d(0, equator - eq_ten_percent),
                new Vector2d(boundary.topRight().getX(), equator + eq_ten_percent));
        placeGrasses(grassCount);
    }

    private void placeGrasses(int grassesToPlace) {
        int mapArea = boundary.topRight().getX() * boundary.topRight().getY();
        Random rand = new Random();
        int i = grassesToPlace;
        int mapWidth = boundary.topRight().getX() + 1;
        while (i > 0) {
            if (mapArea >= grassCount) {
                return;
            }
            int pareto = rand.nextInt(5);
            boolean inJungle = pareto > 0;
            while (true) {
                int x = rand.nextInt(mapWidth);
                int y;
                if (inJungle) {
                    y = rand.nextInt(jungle.bottomLeft().getY(), jungle.topRight().getY() + 1);
                } else {
                    y = rand.nextInt(2) < 1 ?
                            rand.nextInt(jungle.bottomLeft().getY()) : // dolna połowa mapy
                            rand.nextInt(jungle.topRight().getY() + 1, boundary.topRight().getY() + 1); // górna połowa
                }
                Vector2d grassPosition = new Vector2d(x, y);
                if (grasses.get(grassPosition) == null) {
                    grasses.put(grassPosition, new Grass(grassPosition));
                    i -= 1;
                    grassCount += 1;
                    break;
                }
            }
        }
    }
    @Override
    public void move(Animal animal, MoveDirection moveDirection) {
//        Vector2d old_position = animal.getPosition();
//        MapDirection old_direction = animal.getDirection();
//        animal.move(moveDirection, this);
//        if (!Objects.equals(old_position, animal.getPosition())) {
//            animals.remove(old_position);
//            animals.put(animal.getPosition(), animal);
//            notifyObservers("Animal has moved from: " + old_position + " to: " + animal.getPosition());
//        } else if (!Objects.equals(old_direction, animal.getDirection())) {
//            notifyObservers("Animal at: " + animal.getPosition() + " has changed direction from: "
//                    + old_direction + " to: " + animal.getDirection());
//        } else {
//            notifyObservers("Animal at: " + animal.getPosition() + " tried to move "
//                    + moveDirection.toString().toLowerCase() + " but can't.");
//        }
    }
    public void checkEatenGrasses() {
        grasses.values().forEach(grass -> {
            List<Animal> animalsEating = animalsAt(grass.getPosition());

        });
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

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }
}
