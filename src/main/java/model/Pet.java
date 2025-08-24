package model;

import java.util.Arrays;

public class Pet {
    private String id;
    private String name;
    private String species;
    private String breed;
    private byte age;
    private String color;
    private double weight;
    private String[] listDiseases;

    public Pet(String id, String name, String species, String breed, byte age, String color, double weight) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.color = color;
        this.weight = weight;
        this.listDiseases = new String[5];
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

    public String[] getListDiseases() {
        return listDiseases;
    }

    public void setListDiseases(String[] listDiseases) {
        this.listDiseases = listDiseases;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                ", weight=" + weight +
                ", diseases=" + Arrays.toString(listDiseases) + '}';
    }

    /**
     * Metodo que añade una enfermedad a una mascota
     * @param diseases
     * @param index
     * @return
     */
    public String addDiseases(String diseases, int index) {
        String result = "";
        listDiseases[index-1] = diseases;
        result = "Enfermedad numero "+index+" ha sido añadida exitosamente";
        return result;
    }
    public String addDiseases(String diseases) {
        String result = "";
        for(int i = 0; i<listDiseases.length; i++){
            if (this.listDiseases[i] == null) {
                this.listDiseases[i] = diseases;
                result = "Enfermedad añadida exitosamente";
                break;
            }
        }
        return result;
    }

}
