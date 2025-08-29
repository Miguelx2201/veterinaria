package model;

import java.util.ArrayList;

public class Veterinarian extends Person{
    private String businessCard;
    private int yearsExperience;
    private Specialty speciality;
    private ArrayList<Appointment> listAppointments;

    public Veterinarian(String name, String lastName, String id, String phone, String email,
                        String address, String businessCard, int yearsExperience, Specialty speciality) {
        super(name, lastName, id, phone, email, address);
        this.businessCard = businessCard;
        this.yearsExperience = yearsExperience;
        this.speciality = speciality;
        this.listAppointments = new ArrayList<>();
    }

    public String getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(String businessCard) {
        this.businessCard = businessCard;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    public void setYearsExperience(int yearsExperience) {
        this.yearsExperience = yearsExperience;
    }

    public Specialty getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Specialty speciality) {
        this.speciality = speciality;
    }

    public ArrayList<Appointment> getListAppointments() {
        return listAppointments;
    }

    public void setListAppointments(ArrayList<Appointment> listAppointments) {
        this.listAppointments = listAppointments;
    }
}
