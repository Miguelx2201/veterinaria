package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Pet {
    private String name;
    private String id;
    private String species;
    private String breed;
    private byte age;
    private String color;
    private double weight;
    private Owner owner;
    private ArrayList<Appointment> listAppointments;


    public Pet(String id, String name, String species, String breed, byte age, String color, double weight) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.color = color;
        this.weight = weight;
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Pet" +
                "\nID: " + id +
                "\nName: " + name +
                "\nSpecies: " + species +
                "\nBreed: " + breed +
                "\nAge: " + age +
                "\nColor: " + color +
                "\nWeight: " + weight;
    }


}
