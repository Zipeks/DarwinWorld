package agh.model;

public enum AnimalSex {
    MALE,
    FEMALE;

    @Override
    public String toString(){
        return switch (this) {
            case MALE -> "M";
            case FEMALE -> "F";
        };
    }

}
