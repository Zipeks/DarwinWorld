package agh.model;

import agh.model.util.Boundary;

import java.util.*;

public class JungleMap extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Boundary boundary;
    private final Boundary jungle;
    private final int mapArea;
    private int grassCount;

    public JungleMap(int grassCount, Boundary boundary) {
        this.boundary = boundary;
        mapArea = boundary.topRight().getX() * boundary.topRight().getY();
        int height = boundary.topRight().getY() - boundary.bottomLeft().getY();
        int equator = Math.floorDiv(height, 2);
        int eq_ten_percent = Math.floorDiv(equator, 10);
        this.jungle = new Boundary(new Vector2d(0, equator - eq_ten_percent),
                new Vector2d(boundary.topRight().getX(), equator + eq_ten_percent));
        IO.println(grassCount);
        placeGrasses(grassCount);
    }

    public void removeDeadAnimals() {
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalsAtP = animals.get(position);
            animalsAtP.removeIf(animal -> animal.getEnergy() <= 0);
        }
    }

    public void placeGrasses(int grassesToPlace) {
        Random rand = new Random();
        int i = grassesToPlace;
        int mapWidth = boundary.topRight().getX() + 1;
        IO.println(mapWidth);
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
                IO.println(grassPosition);
                if (grasses.get(grassPosition) == null) {
                    grasses.put(grassPosition, new Grass(grassPosition));
                    i -= 1;
                    grassCount += 1;
                    break;
                }
            }
        }
        IO.println(grasses);
    }

    public void moveAnimals(int moveCost) {
        animals.values().forEach(animals -> {
            for (Animal animal: animals) {
                animals.remove(animal);
                animal.move(this, moveCost);
                this.place(animal);

//                notifyObservers("Animal has moved from: " + old_position + " to: " + animal.getPosition());
//                } else if (!Objects.equals(old_direction, animal.getDirection())) {
//                    notifyObservers("Animal at: " + animal.getPosition() + " has changed direction from: "
//                            + old_direction + " to: " + animal.getDirection());
//                } else {
//                    notifyObservers("Animal at: " + animal.getPosition() + " tried to move "
//                            + moveDirection.toString().toLowerCase() + " but can't.");
//                }
            }

        });
    }
    public Vector2d moveOnMap(Vector2d position, Vector2d moveVector) {
        int width = boundary.topRight().getX();
        int height = boundary.topRight().getY();
        int y = position.getY() + moveVector.getY();
        if (y < 0 || y >= height) {
            moveVector = moveVector.opposite();
        }
        int x = (position.getX() + moveVector.getX() + width ) % width;
        return  new Vector2d(x,y);
    }

    public void grassConsumption(int energyGained) {
        animals.values().forEach(animalsAtP -> {
            Vector2d position = animalsAtP.getFirst().getPosition();
            if (!grasses.containsKey(position)) {
                return;
            }
            animalsAtP.sort(Comparator.comparingInt(Animal::getEnergy)
                    .thenComparing(Animal::getAge)
                    .thenComparing(Animal::getChildrenCount));
            Animal firstAnimal = animalsAtP.getFirst();
            firstAnimal.eatenGrass(energyGained);
            grasses.remove(position);
        });
    }


    public List<Animal> animalReproduction(int energyNeededToReproduce, int energyLostToReproduction, int minMutations, int maxMutation) {
        List<Animal> childAnimals = new ArrayList<>();
        Random PRNG = new Random();
        for (Vector2d position : animals.keySet()) {
            List<Animal> animalsAtP = animals.get(position);
            if (animalsAtP.size() < 2) {
                return Collections.emptyList();
            }
            animalsAtP.sort(Comparator.comparingInt(Animal::getEnergy)
                    .thenComparing(Animal::getAge)
                    .thenComparing(Animal::getChildrenCount));
            Animal firstAnimal = animalsAtP.getFirst();
            Animal secondAnimal = animalsAtP.get(1);
            if (firstAnimal.getEnergy() > energyNeededToReproduce && secondAnimal.getEnergy() > energyNeededToReproduce) {
                Animal childAnimal = new Animal(position, firstAnimal, secondAnimal,
                        PRNG.nextInt(minMutations, maxMutation + 1), energyLostToReproduction * 2);
                place(childAnimal);

                firstAnimal.addChild(childAnimal, energyLostToReproduction);
                secondAnimal.addChild(childAnimal, energyLostToReproduction);
            }
        }
        return childAnimals;
    }

    @Override
    public List<WorldElement> getElements() {
        List<WorldElement> list = super.getElements();
        list.addAll(grasses.values());
        IO.println(grasses.values());
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

    public boolean canMoveTo(Vector2d position) {
        return true;
    }
}
