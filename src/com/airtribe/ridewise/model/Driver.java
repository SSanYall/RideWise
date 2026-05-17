package com.airtribe.ridewise.model;

public class Driver {
    private String id;
    private String name;
    private double currentLocationX;
    private double currentLocationY;
    private boolean available;
    private VehicleType vehicleType;
    private int completedRides;

    public Driver(String id, String name, double locationX, double locationY, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocationX = locationX;
        this.currentLocationY = locationY;
        this.vehicleType = vehicleType;
        this.available = true;
        this.completedRides = 0;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCurrentLocationX() {
        return currentLocationX;
    }

    public double getCurrentLocationY() {
        return currentLocationY;
    }

    public boolean isAvailable() {
        return available;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getCompletedRides() {
        return completedRides;
    }

    // Setters
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setCurrentLocation(double locationX, double locationY) {
        this.currentLocationX = locationX;
        this.currentLocationY = locationY;
    }

    public void incrementCompletedRides() {
        this.completedRides++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=(" + currentLocationX + ", " + currentLocationY + ')' +
                ", available=" + available +
                ", vehicleType=" + vehicleType +
                ", completedRides=" + completedRides +
                '}';
    }
}
