package agh;

import agh.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {
    private final List<MoveDirection> moves;
    private final List<Animal> animals;
    private final WorldMap worldMap;

    public Simulation(List<Vector2d> positions, List<MoveDirection> moves, WorldMap worldMap){
        this.moves = moves;
        this.animals = new ArrayList<>();
        this.worldMap = worldMap;
        for (Vector2d position : positions) {
            Animal animal = new Animal(position);
            this.worldMap.place(animal);
            animals.add(animal);
        }
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    @Override
    public void run() {
        int i = 0;
        for (MoveDirection nextMove : moves) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                IO.println(e.getMessage());
            }
            worldMap.move(animals.get(i), nextMove);
            i = (i + 1) % animals.size();
        }
    }

}
