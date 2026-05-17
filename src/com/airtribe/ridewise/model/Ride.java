package com.airtribe.ridewise.model;

public class Ride {
    private String id;
    private Rider rider;
    private Driver driver;
    private double distance;
    private RideStatus status;
    private FareReceipt fareReceipt;

    public Ride(String id, Rider rider, double distance) {
        this.id = id;
        this.rider = rider;
        this.distance = distance;
        this.status = RideStatus.REQUESTED;
        this.driver = null;
        this.fareReceipt = null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    // Setters
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public void setFareReceipt(FareReceipt fareReceipt) {
        this.fareReceipt = fareReceipt;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id='" + id + '\'' +
                ", rider=" + (rider != null ? rider.getName() : "null") +
                ", driver=" + (driver != null ? driver.getName() : "null") +
                ", distance=" + distance +
                ", status=" + status +
                ", fare=" + (fareReceipt != null ? fareReceipt.getAmount() : "N/A") +
                '}';
    }
}
