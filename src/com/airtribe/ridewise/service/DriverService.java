package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.VehicleType;
import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private List<Driver> drivers;

    public DriverService() {
        this.drivers = new ArrayList<>();
    }

    public Driver registerDriver(String id, String name, double locationX, double locationY, VehicleType vehicleType) {
        Driver driver = new Driver(id, name, locationX, locationY, vehicleType);
        drivers.add(driver);
        return driver;
    }

    public Driver getDriverById(String id) {
        for (Driver driver : drivers) {
            if (driver.getId().equals(id)) {
                return driver;
            }
        }
        return null;
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            if (driver.isAvailable()) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers);
    }

    public void updateDriverAvailability(String driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }

    public int getTotalDrivers() {
        return drivers.size();
    }
}
