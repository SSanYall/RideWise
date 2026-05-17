package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import java.time.LocalDateTime;

public class PeakHourFareStrategy implements FareStrategy {
    private static final double BASE_FARE = 10.0;
    private static final double FARE_PER_KM = 5.0;
    private static final double PEAK_HOUR_MULTIPLIER = 1.5;
    private static final int PEAK_HOUR_START = 18; // 6 PM
    private static final int PEAK_HOUR_END = 23;   // 11 PM

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = BASE_FARE + (ride.getDistance() * FARE_PER_KM);
        
        if (isPeakHour()) {
            return baseFare * PEAK_HOUR_MULTIPLIER;
        }
        
        return baseFare;
    }

    private boolean isPeakHour() {
        int currentHour = LocalDateTime.now().getHour();
        return currentHour >= PEAK_HOUR_START && currentHour < PEAK_HOUR_END;
    }
}
