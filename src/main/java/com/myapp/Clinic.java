package com.myapp;

import java.util.Arrays;
import java.util.List;

public class Clinic {
    private final int id;
    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final double distance;
    private final String email;
    private final String phone;
    private final String urgentPhone;
    private final List<String> hours;
    private final String cost;


    public Clinic(int id, String name, String address, double latitude, double longitude, double distance, String email, String phone, String urgentPhone, String hours, int cost) {
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
        if (cost == -1){
            this.cost = "Free";
        } else {
            this.cost = "£"+String.format("%.2f", (float) cost/100);
        }
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getLatitude() { return latitude; }
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
    public String getCost() {return cost;}
}
