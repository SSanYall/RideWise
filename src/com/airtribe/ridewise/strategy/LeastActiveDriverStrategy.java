package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import java.util.List;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }

        Driver leastActiveDriver = availableDrivers.get(0);
        int minRides = availableDrivers.get(0).getCompletedRides();

        for (int i = 1; i < availableDrivers.size(); i++) {
            Driver driver = availableDrivers.get(i);
            if (driver.getCompletedRides() < minRides) {
                minRides = driver.getCompletedRides();
                leastActiveDriver = driver;
            }
        }

        return leastActiveDriver;
    }
}
