package com.airtribe.ridewise.util;

public class IdGenerator {
    private static int riderCounter = 0;
    private static int driverCounter = 0;
    private static int rideCounter = 0;
    private static int generalCounter = 0;

    public static synchronized String generateRiderId() {
        return "RIDER_" + (++riderCounter);
    }

    public static synchronized String generateDriverId() {
        return "DRIVER_" + (++driverCounter);
    }

    public static synchronized String generateRideId() {
        return "RIDE_" + (++rideCounter);
    }

    // Generic ID generation
    public static synchronized String generateId() {
        return "ID_" + (++generalCounter);
    }

    // Generic ID generation with prefix
    public static synchronized String generateId(String prefix) {
        return prefix + "_" + (++generalCounter);
    }

    // Reset counters for testing purposes
    public static void resetCounters() {
        riderCounter = 0;
        driverCounter = 0;
        rideCounter = 0;
        generalCounter = 0;
    }
}
