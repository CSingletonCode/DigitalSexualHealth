package com.myapp;

public class Clinic {
    private int id;
    private String name;
    private String address;
    private double latitude, longitude, distance;

    public Clinic(int id, String name, String address, double latitude, double longitude, double distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
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

    public String getAddress() {
        return address;
    }
}
