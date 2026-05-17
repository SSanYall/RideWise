package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.OfflineGeocoder;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> availableDrivers) {
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            return null;
        }

        double[] riderCoords = getRiderCoordinates(rider);

        Driver nearestDriver = availableDrivers.get(0);
        double minDistance = calculateDistance(riderCoords[0], riderCoords[1], availableDrivers.get(0).getCurrentLocationX(), availableDrivers.get(0).getCurrentLocationY());

        for (int i = 1; i < availableDrivers.size(); i++) {
            Driver driver = availableDrivers.get(i);
            double distance = calculateDistance(riderCoords[0], riderCoords[1], driver.getCurrentLocationX(), driver.getCurrentLocationY());

            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }

        return nearestDriver;
    }

    private double[] getRiderCoordinates(Rider rider) {
        String location = rider.getLocation();

        // First try to geocode using OfflineGeocoder (for locality names)
        double[] coords = OfflineGeocoder.geocode(location);
        if (coords != null) {
            return coords;
        }

        // Fall back to parsing as "x,y" format (for coordinate strings)
        return parseLocation(location);
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private double[] parseLocation(String location) {
        try {
            String[] parts = location.split(",");
            return new double[]{Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())};
        } catch (Exception e) {
            // Default to origin if parsing fails
            return new double[]{0, 0};
        }
    }
}
