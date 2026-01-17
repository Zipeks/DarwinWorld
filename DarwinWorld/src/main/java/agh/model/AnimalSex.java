package agh.model;

public enum AnimalSex {
    MALE,
    FEMALE;

    @Override
    public String toString(){
        return switch (this) {
            case MALE -> "Samiec";
            case FEMALE -> "Samica";
        };
    }

}
