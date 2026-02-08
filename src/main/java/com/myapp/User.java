package com.myapp;

public class User {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;


    public void setEmail(String text) {
        this.email = text;
    }
    public void setPassword(String text) {
        this.password = text;
    }
    public void setFirstName(String text) {
        this.firstName = text;
    }
    public void setLastName(String text) {
        this.lastName = text;
    }
    public void setDob(String text) {
        this.dob = text;
    }
    public void setGender(String text) {
        this.gender = text;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getDob() {
        return dob;
    }
    public String getGender() {
        return gender;
    }
}
