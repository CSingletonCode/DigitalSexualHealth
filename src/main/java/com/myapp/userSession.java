package com.myapp;

public class userSession {
    private static userSession instance;

    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private String Dob;
    private String gender;

    private userSession(int userId,String email, String firstName, String lastName, String Dob, String gender) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Dob = Dob;
        this.gender = gender;
    }

    public static void login(int UserId, String email, String firstName, String lastName, String Dob, String gender) {
        instance = new userSession(UserId,email,firstName,lastName,Dob,gender);
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
}
