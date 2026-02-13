package com.myapp;

import java.util.Arrays;
import java.util.List;

public class Clinic {
    private int id;
    private String name;
    private String address;
    private double latitude, longitude, distance;
    private String email;
    private String phone;
    private String urgentPhone;
    private List<String> hours;


    public Clinic(int id, String name, String address, double latitude, double longitude, double distance, String email, String phone, String urgentPhone, String hours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.email = email;
        this.phone = phone;
        this.urgentPhone = urgentPhone;
        this.hours = Arrays.asList(hours.split(","));
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getDistance() {
        return distance;
    }
    public String getStringDistance() {
        return String.format("%.2f", distance);
    }
    public String getAddress() {
        return address;
    }
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getUrgentPhone() {return urgentPhone;}
    public List<String> getHours() {return hours;}
}
