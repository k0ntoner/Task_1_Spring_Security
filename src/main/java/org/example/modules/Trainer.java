package org.example.modules;

public class Trainer extends User{
    private String specialization;
    private int userId;

    public Trainer(String firstName, String lastName, String username, String password, String isActive, String specialization, int userId) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
