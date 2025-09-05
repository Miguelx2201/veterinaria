package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Veterinary {
    private String location;
    private String name;
    private String phone;
    private String email;
    private String nit;
    private ArrayList<Pet> listPets;
    private ArrayList<Owner> listOwners;
    private ArrayList<Appointment> listAppointments;
    private ArrayList<Veterinarian> listVeterinarians;

    public Veterinary(String location, String name, String phone, String email, String nit) {
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nit = nit;
        this.listPets = new ArrayList<>();
        this.listOwners = new ArrayList<>();
        this.listAppointments = new ArrayList<>();
        this.listVeterinarians = new ArrayList<>();
    }

    public Veterinary() {
        this.listPets = new ArrayList<>();
        this.listOwners = new ArrayList<>();
        this.listAppointments = new ArrayList<>();
        this.listVeterinarians = new ArrayList<>();
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
        if(listPets == null) listPets = new ArrayList<>();
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

    public ArrayList<Owner> getListOwners() {
        if(listOwners == null) listOwners = new ArrayList<>();
        return listOwners;
    }

    public void setListOwners(ArrayList<Owner> listOwners) {
        this.listOwners = listOwners;
    }

    public ArrayList<Appointment> getListAppointments() {
        if(listAppointments == null) listAppointments = new ArrayList<>();
        return listAppointments;
    }

    public void setListAppointments(ArrayList<Appointment> listAppointments) {
        this.listAppointments = listAppointments;
    }

    public ArrayList<Veterinarian> getListVeterinarians() {
        if(listVeterinarians == null) listVeterinarians = new ArrayList<>();
        return listVeterinarians;
    }

    public void setListVeterinarians(ArrayList<Veterinarian> listVeterinarians) {
        this.listVeterinarians = listVeterinarians;
    }

    @Override
    public String toString() {
        return "\nVeterinaria:\nNombre:"+name+"\nNit:"+nit+"\nLocation:"+location+"\nPhone:"+phone+"\nEmail:"+email+
                "\nMascotas:"+listPets.toString()+"\n";
    }
    public String addPet(Pet pet) {
        if(searchPet(pet.getId()).isEmpty()) {
            listPets.add(pet);
            return "Pet has been added successfully.";
        } else return "Pet already exists.";
    }
    public Optional<Pet> searchPet(String id) {
        return listPets.stream().filter(pet -> pet.getId().equals(id)).findFirst();
    }
    public String removePet(String id) {
        boolean remove = listPets.removeIf(pet -> pet.getId().equals(id));
        if(remove) return "Pet has been removed successfully.";
        else return "Pet not found.";
    }
    public String removePet(Pet petRemove) {
        boolean remove = listPets.removeIf(pet -> pet.equals(petRemove));
        if(remove) return "Pet has been removed successfully.";
        else return "Pet not found.";
    }
    public String updatePet(String id, Pet pet) {
        Optional<Pet> foundPet = searchPet(id);
        if(foundPet.isPresent()) {
            listPets.remove(foundPet.get());
            listPets.add(pet);
            return "Pet has been updated successfully.";
        } else return "Pet not found.";
    }

    public String addOwner(Owner owner) {
        String result = "";
        Owner foundOwner = searchOwner(owner.getId());
        if(foundOwner == null) {
            listOwners.add(owner);
            result = "Owner has been added successfully.";
        } else {
            result = "Owner already exists.";
        }
        return result;
    }
    public String removeOwner(String id) {
        String result = "";
        Owner foundOwner = searchOwner(id);
        if(foundOwner != null) {
            listOwners.remove(foundOwner);
            result = "Owner has been removed successfully.";
        } else {
            result = "Owner not found.";
        }
        return result;
    }
    public Owner searchOwner(String id) {
        Owner foundOwner = null;
        for(Owner owner: listOwners) {
            if(owner.getId().equals(id)) {
                foundOwner = owner;
            }
        }
        return foundOwner;
    }
    public String updateOwner(String id, Owner owner) {
        String result = "";
        Owner foundOwner = searchOwner(id);
        if(foundOwner != null) {
            listOwners.remove(foundOwner);
            listOwners.add(owner);
            result = "Owner has been updated successfully.";
        } else {
            result = "Owner not found.";
        }
        return result;
    }
    public String addAppointmente(Appointment appointment) {
        String result = "";
        Appointment foundAppointment = searchAppointment(appointment.getId());
        if(foundAppointment == null) {
            listAppointments.add(appointment);
            result = "Appointment has been added successfully.";
        } else {
            result = "Appointment already exists.";
        }
        return result;
    }
    public String removeAppointmente(String id) {
        String result = "";
        Appointment foundAppointment = searchAppointment(id);
        if(foundAppointment != null) {
            listAppointments.remove(foundAppointment);
            result = "Appointment has been removed successfully.";
        } else {
            result = "Appointment not found.";
        }
        return result;
    }
    public Appointment searchAppointment(String id) {
        Appointment foundAppointment = null;
        for(Appointment appointment: listAppointments) {
            if(appointment.getId().equals(id)) {
                foundAppointment = appointment;
            }
        }
        return foundAppointment;
    }
    public String updateAppointment(String id, Appointment appointment) {
        String result = "";
        Appointment foundAppointment = searchAppointment(id);
        if(foundAppointment != null) {
            listAppointments.remove(foundAppointment);
            listAppointments.add(appointment);
            result = "Appointment has been updated successfully.";
        } else {
            result = "Appointment not found.";
        }
        return result;
    }
    public String addVeterinarian(Veterinarian veterinarian) {
        String result = "";
        Veterinarian foundVeterinarian = searchVeterinarian(veterinarian.getId());
        if(foundVeterinarian == null) {
            listVeterinarians.add(veterinarian);
            result = "Veterinarian has been added successfully.";
        } else {
            result = "Veterinarian already exists.";
        }
        return result;
    }
    public String removeVeterinarian(String id) {
        String result = "";
        Veterinarian foundVeterinarian = searchVeterinarian(id);
        if(foundVeterinarian != null) {
            listVeterinarians.remove(foundVeterinarian);
            result = "Veterinarian has been removed successfully.";
        } else {
            result = "Veterinarian not found.";
        }
        return result;
    }
    public Veterinarian searchVeterinarian(String id) {
        Veterinarian foundVeterinarian = null;
        for(Veterinarian veterinarian: listVeterinarians) {
            if(veterinarian.getId().equals(id)) {
                foundVeterinarian = veterinarian;
            }
        }
        return foundVeterinarian;
    }
    public String updateVeterinarian(String id, Veterinarian veterinarian) {
        String result = "";
        Veterinarian foundVeterinarian = searchVeterinarian(id);
        if(foundVeterinarian != null) {
            listVeterinarians.remove(foundVeterinarian);
            listVeterinarians.add(veterinarian);
            result = "Veterinarian has been updated successfully.";
        } else {
            result = "Veterinarian not found.";
        }
        return result;
    }
}
