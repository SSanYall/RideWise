package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.util.IdGenerator;
import com.airtribe.ridewise.util.OfflineGeocoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private RiderService riderService;
    private DriverService driverService;
    private RideService rideService;
    private Scanner scanner;

    public Main() {
        this.riderService = new RiderService();
        this.driverService = new DriverService();
        // Initialize with default strategies
        this.rideService = new RideService(
                new NearestDriverStrategy(),
                new DefaultFareStrategy()
        );
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        System.out.println("========================================");
        System.out.println("Welcome to RideWise - Ride Sharing App");
        System.out.println("========================================\n");

        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addRider();
                    break;
                case "2":
                    addDriver();
                    break;
                case "3":
                    viewAvailableDrivers();
                    break;
                case "4":
                    requestRide();
                    break;
                case "5":
                    completeRide();
                    break;
                case "6":
                    viewAllRides();
                    break;
                case "7":
                    running = false;
                    System.out.println("\nThank you for using RideWise! Goodbye!");
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please enter a number between 1 and 7.\n");
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View All Rides");
        System.out.println("7. Exit");
        System.out.print("\nEnter your choice: ");
    }

    private void addRider() {
        System.out.println("\n--- Add Rider ---");
        System.out.print("Enter rider name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Rider name cannot be empty.");
            return;
        }

        String location = selectLocation("Rider");
        if (location == null) {
            return;
        }

        String riderId = IdGenerator.generateRiderId();
        Rider rider = riderService.registerRider(riderId, name, location);
        System.out.println("✅ Rider registered successfully!");
        System.out.println("   ID: " + rider.getId());
        System.out.println("   Name: " + rider.getName());
        System.out.println("   Location: " + rider.getLocation());
    }

    private void addDriver() {
        System.out.println("\n--- Add Driver ---");
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Driver name cannot be empty.");
            return;
        }

        String location = selectLocation("Driver");
        if (location == null) {
            return;
        }

        double[] coords = OfflineGeocoder.geocode(location);
        if (coords == null) {
            System.out.println("❌ Failed to get coordinates for location: " + location);
            return;
        }

        System.out.println("Enter vehicle type:");
        System.out.println("1. BIKE");
        System.out.println("2. AUTO");
        System.out.println("3. CAR");
        System.out.print("Choice: ");
        String vehicleChoice = scanner.nextLine().trim();

        VehicleType vehicleType;
        switch (vehicleChoice) {
            case "1":
                vehicleType = VehicleType.BIKE;
                break;
            case "2":
                vehicleType = VehicleType.AUTO;
                break;
            case "3":
                vehicleType = VehicleType.CAR;
                break;
            default:
                System.out.println("❌ Invalid vehicle type.");
                return;
        }

        String driverId = IdGenerator.generateDriverId();
        Driver driver = driverService.registerDriver(driverId, name, coords[0], coords[1], vehicleType);
        System.out.println("✅ Driver registered successfully!");
        System.out.println("   ID: " + driver.getId());
        System.out.println("   Name: " + driver.getName());
        System.out.println("   Location: " + location + " (" + driver.getCurrentLocationX() + ", " + driver.getCurrentLocationY() + ")");
        System.out.println("   Vehicle Type: " + driver.getVehicleType());
    }

    private String selectLocation(String entityType) {
        System.out.println("\nSelect location:");
        List<String> localities = new ArrayList<>(OfflineGeocoder.getAllLocalities());
        localities.sort(String::compareTo);

        for (int i = 0; i < localities.size(); i++) {
            System.out.println((i + 1) + ". " + localities.get(i));
        }

        System.out.print(entityType + " location choice: ");
        String choice = scanner.nextLine().trim();

        int locationIndex;
        try {
            locationIndex = Integer.parseInt(choice) - 1;
            if (locationIndex < 0 || locationIndex >= localities.size()) {
                System.out.println("❌ Invalid location selection.");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return null;
        }

        return localities.get(locationIndex);
    }

    private void viewAvailableDrivers() {
        System.out.println("\n--- Available Drivers ---");
        List<Driver> availableDrivers = driverService.getAvailableDrivers();

        if (availableDrivers.isEmpty()) {
            System.out.println("No drivers currently available.");
            return;
        }

        for (Driver driver : availableDrivers) {
            System.out.println("\n" + driver);
        }
    }

    private void requestRide() {
        System.out.println("\n--- Request Ride ---");

        if (riderService.getTotalRiders() == 0) {
            System.out.println("❌ No riders registered. Please add a rider first.");
            return;
        }

        if (driverService.getTotalDrivers() == 0) {
            System.out.println("❌ No drivers registered. Please add a driver first.");
            return;
        }

        // Display available riders
        System.out.println("Available Riders:");
        List<Rider> riders = riderService.getAllRiders();
        for (int i = 0; i < riders.size(); i++) {
            Rider rider = riders.get(i);
            System.out.println((i + 1) + ". " + rider.getName() + " (" + rider.getId() + ")");
        }

        System.out.print("Select rider number: ");
        String riderChoice = scanner.nextLine().trim();

        int riderIndex;
        try {
            riderIndex = Integer.parseInt(riderChoice) - 1;
            if (riderIndex < 0 || riderIndex >= riders.size()) {
                System.out.println("❌ Invalid rider selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return;
        }

        Rider selectedRider = riders.get(riderIndex);

        System.out.print("Enter ride distance (in km): ");
        String distanceStr = scanner.nextLine().trim();

        double distance;
        try {
            distance = Double.parseDouble(distanceStr);
            if (distance <= 0) {
                System.out.println("❌ Distance must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid distance. Please enter a valid number.");
            return;
        }

        String rideId = IdGenerator.generateRideId();
        Ride ride = rideService.requestRide(rideId, selectedRider, distance);

        // Try to assign driver
        try {
            List<Driver> availableDrivers = driverService.getAvailableDrivers();
            if (availableDrivers.isEmpty()) {
                System.out.println("❌ No drivers available at this moment.");
                return;
            }

            Ride assignedRide = rideService.assignDriver(rideId, availableDrivers);
            System.out.println("✅ Ride requested and driver assigned successfully!");
            System.out.println("   Ride ID: " + assignedRide.getId());
            System.out.println("   Rider: " + assignedRide.getRider().getName());
            System.out.println("   Driver: " + assignedRide.getDriver().getName());
            System.out.println("   Distance: " + assignedRide.getDistance() + " km");
            System.out.println("   Status: " + assignedRide.getStatus());
        } catch (NoDriverAvailableException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void completeRide() {
        System.out.println("\n--- Complete Ride ---");

        // Get all assigned rides
        List<Ride> assignedRides = rideService.getRidesByStatus(com.airtribe.ridewise.model.RideStatus.ASSIGNED);

        if (assignedRides.isEmpty()) {
            System.out.println("No assigned rides available to complete.");
            return;
        }

        System.out.println("Assigned Rides:");
        for (int i = 0; i < assignedRides.size(); i++) {
            Ride ride = assignedRides.get(i);
            System.out.println((i + 1) + ". Ride " + ride.getId() + " - Rider: " + ride.getRider().getName() +
                    ", Driver: " + ride.getDriver().getName() + ", Distance: " + ride.getDistance() + " km");
        }

        System.out.print("Select ride number to complete: ");
        String rideChoice = scanner.nextLine().trim();

        int rideIndex;
        try {
            rideIndex = Integer.parseInt(rideChoice) - 1;
            if (rideIndex < 0 || rideIndex >= assignedRides.size()) {
                System.out.println("❌ Invalid ride selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a number.");
            return;
        }

        Ride selectedRide = assignedRides.get(rideIndex);

        try {
            Ride completedRide = rideService.completeRide(selectedRide.getId());
            System.out.println("✅ Ride completed successfully!");
            System.out.println("   Ride ID: " + completedRide.getId());
            System.out.println("   Rider: " + completedRide.getRider().getName());
            System.out.println("   Driver: " + completedRide.getDriver().getName());
            System.out.println("   Distance: " + completedRide.getDistance() + " km");
            System.out.println("   Fare: $" + String.format("%.2f", completedRide.getFareReceipt().getAmount()));
            System.out.println("   Status: " + completedRide.getStatus());
        } catch (Exception e) {
            System.out.println("❌ Error completing ride: " + e.getMessage());
        }
    }

    private void viewAllRides() {
        System.out.println("\n--- All Rides ---");
        List<Ride> rides = rideService.getAllRides();

        if (rides.isEmpty()) {
            System.out.println("No rides found.");
            return;
        }

        for (Ride ride : rides) {
            System.out.println("\n" + ride);
            if (ride.getFareReceipt() != null) {
                System.out.println("   Fare: $" + String.format("%.2f", ride.getFareReceipt().getAmount()));
            }
        }
    }
}
