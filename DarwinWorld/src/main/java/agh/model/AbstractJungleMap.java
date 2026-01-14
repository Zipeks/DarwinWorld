package agh.model;

import agh.model.util.Boundary;
import agh.model.util.SimulationConfig;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractJungleMap extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary boundary;
    private final Boundary jungle;
    private final int mapArea;

    public AbstractJungleMap(int grassCount, int width, int height) {
        this.boundary = new Boundary(new Vector2d(0, 0), new Vector2d(width - 1, height-1));
        mapArea = (boundary.topRight().getX() + 1) * (boundary.topRight().getY() + 1);

        int jungleHeight = Math.round((float) height / 5);
        int midRow = height / 2;
        int lowerRow = (height - jungleHeight) / 2;
        int upperRow = lowerRow + jungleHeight - 1;
        this.jungle = new Boundary(new Vector2d(0, lowerRow), new Vector2d(boundary.topRight().getX(), upperRow));

        placeGrasses(grassCount);
    }
    public void removeDeadAnimals() {
        Iterator<Map.Entry<Vector2d, List<Animal>>> iterator = animals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2d, List<Animal>> entry = iterator.next();
            List<Animal> animalList = entry.getValue();

            animalList.removeIf(animal -> animal.getEnergy() <= 0);

            if (animalList.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public void placeGrasses(int grassesToPlace) {
        Random rand = new Random();
        int i = 0;
        int mapWidth = boundary.topRight().getX() + 1;
        while (i < grassesToPlace) {
            if (mapArea <= getGrassCount()) {
                return;
            }
            while (true) {
                int pareto = rand.nextInt(5);
                boolean inJungle = pareto > 0;
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
                    i++;
                    break;
                }
            }
        }
    }
    public void moveAnimals(int moveCost) {
        List<Animal> allAnimals = animals.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        animals.clear();

        for (Animal animal : allAnimals) {
            animal.move(this, moveCost);
            place(animal);
        }
    }

    public Vector2d moveOnMap(Vector2d position, Vector2d moveVector) {
        int width = boundary.topRight().getX() + 1;
        int height = boundary.topRight().getY() + 1;

        int y = position.getY() + moveVector.getY();

        if (y < 0 || y >= height) {
            y = position.getY();
        }
        int x = (position.getX() + moveVector.getX() + width) % width;

        return new Vector2d(x, y);
    }

    public void grassConsumption(int energyGained) {
        for (List<Animal> animalsList : animals.values()) {
            if (animalsList.isEmpty()) return;
            Vector2d position = animalsList.getFirst().getPosition();
            if (grasses.containsKey(position)) {
                animalsList.sort(Comparator.comparingInt(Animal::getEnergy)
                        .thenComparing(Animal::getAge)
                        .thenComparing(Animal::getChildrenCount)
                        .reversed());

                Animal strongest = animalsList.getFirst();
                strongest.eatenGrass(energyGained);

                grasses.remove(position);
            }
        }
    }

    public abstract List<Animal> animalReproduction(SimulationConfig config, int currentDay);

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> list = new ArrayList<>(grasses.values());
        list.addAll(super.getElements());
        return list;
    }
    public int getEmptyCellsCount() {
        int emptyCells = mapArea - animals.size();
        for (Vector2d cell: grasses.keySet()) {
            if (!animals.containsKey(cell)) {
                emptyCells--;
            }
        }
        return emptyCells;
    }

    @Override
    public Boundary getCurrentBounds() {
        return boundary;
    }

    public Boundary getJungle() {
        return jungle;
    }

    public int getGrassCount() {
        return grasses.size();
    }


    public void nextDay(int i) {
        notifyObservers("Day: " + i);
        System.out.println("Day: " + i);
    }
}
