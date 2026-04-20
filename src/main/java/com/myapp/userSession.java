package com.myapp;

import java.io.IOException;

public class userSession {
    private static userSession instance;

    private final int userId;
    private final String email;
    private String firstName;
    private final String lastName;
    private final String Dob;
    private final String gender;
    private boolean highContrast = false;
    private boolean largeText = false;
    private double myLat;
    private double myLon;

    private userSession(int userId,String email, String firstName, String lastName, String Dob, String gender, double myLat, double myLon) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Dob = Dob;
        this.gender = gender;
        this.myLat = myLat;
        this.myLon = myLon;
    }

    public static void login(int UserId, String email, String firstName, String lastName, String Dob, String gender, double lat , double lng) throws IOException {
        instance = new userSession(UserId,email,firstName,lastName,Dob,gender,lat,lng);
        String[] current_session = sessionManager.getSession();
        if (current_session != null) {
            instance.setHighContrast(sessionManager.isHighContrast());
            instance.setLargeText(sessionManager.isLargeText());
        } else {
            instance.setHighContrast(false);
            instance.setLargeText(false);
        }
    }

    public static userSession getInstance() {
        return instance;
    }

    public static void cleanUserSession() {
        instance = null;
    }

    public int getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getDob() {
        return Dob;
    }
    public String getGender() {
        return gender;
    }
    public boolean isHighContrast() { return highContrast; }
    public void setHighContrast(boolean highContrast) { this.highContrast = highContrast; }
    public boolean isLargeText() { return largeText; }
    public void setLargeText(boolean largeText) { this.largeText = largeText; }
    public double getMyLat() { return myLat; }
    public double getMyLon() { return myLon; }
    public void setMyLat(double myLat) { this.myLat = myLat; }
    public void setMyLon(double myLon) { this.myLon = myLon; }
}
