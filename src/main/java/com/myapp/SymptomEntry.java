package com.myapp;

public class SymptomEntry {

    private String id;
    private String name;
    private String date;
    private String description;
    private String userID;

    public SymptomEntry(){}

    public SymptomEntry(String name, String date, String description, String userID) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.date = date;
        this.description = description;
        this.userID = userID;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getUserID() { return userID; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setUserID(String userID) { this.userID = userID; }
}
