package agh.model;

import agh.model.util.Boundary;

import java.util.*;
import java.util.stream.Collectors;

public class JungleMap extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary boundary;
    private final Boundary jungle;
    private final int mapArea;
    private int grassCount;

    public JungleMap(int grassCount, Boundary boundary) {
        this.boundary = boundary;
        mapArea = (boundary.topRight().getX() + 1) * (boundary.topRight().getY() + 1);

        int height = boundary.topRight().getY() + 1;
        int equator = Math.floorDiv(height, 2);
        int map_ten_percent = Math.floorDiv(height, 10);
        if (map_ten_percent == 0) map_ten_percent = 1;
        this.jungle = new Boundary(
                new Vector2d(0, equator - map_ten_percent),
                new Vector2d(boundary.topRight().getX(), equator + map_ten_percent));

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
            if (mapArea <= grassCount) {
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
                    grassCount += 1;
                    break;
                }
            }
        }
    }

    public void nextDay(int i) {
        notifyObservers("Day: " + i);
        System.out.println("Day: " + i);
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
                grassCount--;
            }
        }
    }


    public List<Animal> animalReproduction(int energyNeededToReproduce, int energyLostToReproduction, int minMutations, int maxMutation) {
        List<Animal> children = new ArrayList<>();
        Random PRNG = new Random();
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalsAtP = animals.get(position);
            if (animalsAtP.size() < 2) {
                continue;
            }
            animalsAtP.sort(Comparator.comparingInt(Animal::getEnergy)
                    .thenComparing(Animal::getAge)
                    .thenComparing(Animal::getChildrenCount)
                    .reversed());

            Animal firstAnimal = animalsAtP.getFirst();
            Animal secondAnimal = animalsAtP.get(1);

            if (firstAnimal.getEnergy() > energyNeededToReproduce && secondAnimal.getEnergy() > energyNeededToReproduce) {
                Animal child = new Animal(position, firstAnimal, secondAnimal,
                        PRNG.nextInt(minMutations, maxMutation + 1), energyLostToReproduction * 2);

                firstAnimal.addChild(child, energyLostToReproduction);
                secondAnimal.addChild(child, energyLostToReproduction);

                children.add(child);
            }
        }
        for (Animal child: children) {
            place(child);
        }

        return children;
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> list = new ArrayList<>(super.getElements());
        list.addAll(grasses.values());
        return list;
    }

    @Override
    public Boundary getCurrentBounds() {
        return boundary;
    }

    public Boundary getJungle(){
        return jungle;
    }
}