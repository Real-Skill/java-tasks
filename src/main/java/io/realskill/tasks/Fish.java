package io.realskill.tasks;

public class Fish {

    private final String name;
    private final Boolean predator;

    public Fish(String name) {
        this.name = name;
        this.predator = Boolean.FALSE;
    }

    public Fish(String name, Boolean predator) {
        this.name = name;
        this.predator = predator;
    }

    public String getName() {
        return name;
    }

    public Boolean getPredator() {
        return predator;
    }

}
