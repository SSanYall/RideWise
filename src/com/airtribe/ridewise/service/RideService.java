package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RideService {
    private List<Ride> rides;
    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;

    public RideService(RideMatchingStrategy rideMatchingStrategy, FareStrategy fareStrategy) {
        this.rides = new ArrayList<>();
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
    }

    public Ride requestRide(String rideId, Rider rider, double distance) {
        Ride ride = new Ride(rideId, rider, distance);
        rides.add(ride);
        return ride;
    }

    public Ride assignDriver(String rideId, List<Driver> availableDrivers) throws NoDriverAvailableException {
        Ride ride = getRideById(rideId);
        if (ride == null) {
            throw new NoDriverAvailableException("Ride with ID " + rideId + " not found");
        }

        Driver driver = rideMatchingStrategy.findDriver(ride.getRider(), availableDrivers);
        if (driver == null) {
            throw new NoDriverAvailableException("No driver available for ride " + rideId);
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);
        driver.setAvailable(false);

        return ride;
    }

    public Ride completeRide(String rideId) throws Exception {
        Ride ride = getRideById(rideId);
        if (ride == null) {
            throw new Exception("Ride with ID " + rideId + " not found");
        }

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new Exception("Ride must be in ASSIGNED status to complete. Current status: " + ride.getStatus());
        }

        double fare = fareStrategy.calculateFare(ride);
        FareReceipt receipt = new FareReceipt(rideId, fare, LocalDateTime.now());
        ride.setFareReceipt(receipt);
        ride.setStatus(RideStatus.COMPLETED);

        // Update driver availability and completed rides
        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.setAvailable(true);
            driver.incrementCompletedRides();
        }

        return ride;
    }

    public Ride cancelRide(String rideId) throws Exception {
        Ride ride = getRideById(rideId);
        if (ride == null) {
            throw new Exception("Ride with ID " + rideId + " not found");
        }

        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new Exception("Cannot cancel a ride with status: " + ride.getStatus());
        }

        // If driver was assigned, make them available again
        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.setAvailable(true);
        }

        ride.setStatus(RideStatus.CANCELLED);
        return ride;
    }

    public Ride getRideById(String rideId) {
        for (Ride ride : rides) {
            if (ride.getId().equals(rideId)) {
                return ride;
            }
        }
        return null;
    }

    public List<Ride> getAllRides() {
        return new ArrayList<>(rides);
    }

    public List<Ride> getRidesByStatus(RideStatus status) {
        List<Ride> result = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getStatus() == status) {
                result.add(ride);
            }
        }
        return result;
    }

    public int getTotalRides() {
        return rides.size();
    }
}
