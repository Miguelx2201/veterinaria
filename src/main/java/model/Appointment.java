package model;

import java.time.LocalDate;

public class Appointment {
    private String id;
    private LocalDate date;
    private double cost;
    private String time;
    private String reason;
    private String observation;
    private AppointmentStatus status;
    private double duration;
    private Pet pet;
    private Veterinarian veterinarian;

    public Appointment(String id, LocalDate date, double cost, String time, String reason, String observation, AppointmentStatus status, double duration, Pet pet, Veterinarian veterinarian) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.time = time;
        this.reason = reason;
        this.observation = observation;
        this.status = status;
        this.duration = duration;
        this.pet = pet;
        this.veterinarian = veterinarian;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }
}
