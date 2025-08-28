package model;

import java.util.ArrayList;

public class Veterinary {
    private String location;
    private String name;
    private String phone;
    private String email;
    private String nit;
    private ArrayList<Pet> listPets;

    public Veterinary(String location, String name, String phone, String email, String nit) {
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nit = nit;
        this.listPets = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public ArrayList<Pet> getListPets() {
        return listPets;
    }

    public void setListPets(ArrayList<Pet> listPets) {
        this.listPets = listPets;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "\nVeterinaria:\nNombre:"+name+"\nNit:"+nit+"\nLocation:"+location+"\nPhone:"+phone+"\nEmail:"+email+
                "\nMascotas:"+listPets.toString()+"\n";
    }

    /**
     * Metodo que añade una mascota a la lista de mascotas.
     * @param pet
     * @return
     */
    public String addPet(Pet pet) {
        String result = "";
        Pet foundPet = searchPet(pet.getId());
        if(foundPet == null) {
            listPets.add(pet);
            result = "La mascota ha sido añadida exitosamente.";
        } else {
            result = "La mascota ya existe en el sistema.";
        }
        return result;
    }

    /**
     * Metodo que elimina una mascota de la lista de mascotas.
     * @param id
     * @return
     */
    public String removePet(String id) {
        String result = "";
        Pet foundPet = searchPet(id);
        if(foundPet != null) {
            listPets.remove(foundPet);
            result = "La mascota ha sido eliminada exitosamente.";
        } else {
            result = "La mascota no ha sido encontrada en el sistema.";
        }
        return result;
    }

    /**
     * Metodo que busca una mascota usando su id
     * @param id
     * @return
     */
    public Pet searchPet(String id) {
        Pet foundPet = null;
        for(Pet pet: listPets) {
            if(pet.getId().equals(id)) {
                foundPet = pet;
            }
        }
        return foundPet;
    }

    public String updatePet(String id, Pet pet) {
        String result = "";
        Pet foundedPet = searchPet(id);
        if(foundedPet != null) {
            listPets.remove(foundedPet);
            listPets.add(pet);
            result = "La mascota ha sido actualizada exitosamente.";
        } else {
            result = "La mascota no ha sido encontrada.";
        }
        return result;
    }
}
