# RideWise - Requirements Document

## Functional Requirements

### 1. User Management

#### Rider Requirements
- [x] Register riders with ID, name, and location
- [x] Update rider location
- [x] Retrieve rider information
- [x] View all registered riders

#### Driver Requirements
- [x] Register drivers with ID, name, coordinates, and vehicle type
- [x] Update driver location coordinates
- [x] Track vehicle type (BIKE, AUTO, CAR)
- [x] Track availability status
- [x] Monitor completed rides count
- [x] View available drivers

### 2. Ride Management

#### Ride Request
- [x] Create ride request with rider, distance, and unique ID
- [x] Initial status: REQUESTED
- [x] Track rider reference
- [x] Track distance in kilometers

#### Ride Assignment
- [x] Assign available driver to requested ride
- [x] Update ride status to ASSIGNED
- [x] Mark driver as unavailable
- [x] Throw NoDriverAvailableException when no drivers available

#### Ride Completion
- [x] Complete only ASSIGNED rides
- [x] Calculate fare based on strategy
- [x] Generate fare receipt with timestamp
- [x] Mark driver as available
- [x] Increment driver completed rides count
- [x] Update ride status to COMPLETED

#### Ride Cancellation
- [x] Cancel REQUESTED or ASSIGNED rides
- [x] Release driver (if assigned)
- [x] Prevent cancellation of COMPLETED/CANCELLED rides
- [x] Update ride status to CANCELLED

### 3. Ride Matching Strategy

#### Nearest Driver Strategy
- [x] Select driver with minimum distance to rider
- [x] Support both locality names and coordinate strings
- [x] Use real Bangalore coordinates via offline geocoder
- [x] Return null for empty driver list

#### Least Active Driver Strategy
- [x] Select driver with minimum completed rides
- [x] Balance workload across drivers
- [x] Return first driver on tie

### 4. Fare Calculation Strategy

#### Default Fare Strategy
- [x] Base fare: ₹10
- [x] Per-km rate: ₹5/km
- [x] Formula: 10 + (distance × 5)

#### Peak Hour Fare Strategy
- [x] Apply 1.5× multiplier during peak hours (18:00-23:00)
- [x] Normal fare outside peak hours
- [x] Formula: fare × 1.5 (if peak) or fare (if normal)

### 5. Location Management

#### Offline Geocoder
- [x] Support 20+ Bangalore localities
- [x] Map locality names to real coordinates
- [x] Validate locality existence
- [x] Return null for invalid localities
- [x] Handle null input gracefully

### 6. Utilities

#### ID Generation
- [x] Generate unique Rider IDs (RIDER_N)
- [x] Generate unique Driver IDs (DRIVER_N)
- [x] Generate unique Ride IDs (RIDE_N)
- [x] Generate generic IDs with/without prefix
- [x] Thread-safe counter management
- [x] Reset counters for testing

## Non-Functional Requirements

### Code Quality
- [x] SOLID principles adherence
- [x] Strategy pattern for flexible algorithms
- [x] Exception handling for error cases
- [x] Comprehensive test coverage (53 tests, 100% pass rate)

### Performance
- [x] O(n) driver search in available drivers
- [x] Thread-safe ID generation
- [x] In-memory data storage (suitable for prototype)

### Reliability
- [x] All business rules enforced
- [x] Proper state transitions
- [x] Exception handling for edge cases
- [x] Driver availability validation

## Ride Status Lifecycle

```
REQUESTED → ASSIGNED → COMPLETED
        ↓
     CANCELLED
```

- REQUESTED: Initial state when rider requests ride
- ASSIGNED: Driver assigned, driver unavailable
- COMPLETED: Ride completed, fare calculated
- CANCELLED: Ride cancelled before/after assignment

## Supported Localities

Bangalore major areas: Indiranagar, Koramangala, MG Road, Whitefield, Jayanagar, Bangalore Fort, Brigade Road, Vijayanagar, Marathahalli, Hebbal, Electronic City, Bellandur, Sarjapur, Varthur, Yelahanka, Yeshwantpur, Bannerghatta, Silk Board, Domlur, Indiranagar East


