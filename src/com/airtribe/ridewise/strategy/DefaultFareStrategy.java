package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

public class DefaultFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 10.0;
    private static final double FARE_PER_KM = 5.0;

    @Override
    public double calculateFare(Ride ride) {
        return BASE_FARE + (ride.getDistance() * FARE_PER_KM);
    }
}
