package model;

import java.util.ArrayList;

public class Owner extends Person{
    private ArrayList<Pet> pets;

    public Owner(String name, String lastName, String id, String phone, String email, String address) {
        super(name, lastName, id, phone, email, address);
        this.pets = new ArrayList<>();
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }

    public void setPets(ArrayList<Pet> pets) {
        this.pets = pets;
    }
}
