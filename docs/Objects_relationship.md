# RideWise - Object Relationships & Interactions
## Entity Relationships
\\\mermaid
erDiagram
    RIDER ||--o{ RIDE : requests
    DRIVER ||--o{ RIDE : completes
    RIDE ||--|| FARERECEIPT : generates
    DRIVER ||--|| VEHICLETYPE : has
    RIDE ||--|| RIDESTATUS : has
    RIDER {
        string id PK
        string name
        string location
    }
    DRIVER {
        string id PK
        string name
        double locationX
        double locationY
        string vehicleType FK
        boolean available
        int completedRides
    }
    RIDE {
        string id PK
        string riderId FK
        string driverId FK
        double distance
        string rideStatus FK
        string fareReceiptId FK
    }
    FARERECEIPT {
        string rideId PK
        double amount
        timestamp generatedAt
    }
    VEHICLETYPE {
        string type PK
    }
    RIDESTATUS {
        string status PK
    }
\\\
## Object Composition
**Ride** has-a **Rider** (composition)
- Rider is required for a ride to exist
- If ride is deleted, reference to rider remains valid
- Multiple rides can reference the same rider
**Ride** has-a **Driver** (composition)
- Driver is assigned during ride lifecycle
- Driver is optional initially (null before assignment)
- One driver can have multiple rides (but sequential, not concurrent)
**Ride** has-a **FareReceipt** (composition)
- FareReceipt is generated only after ride completion
- Receipt is tied to specific ride
- Optional until ride is completed
**Driver** has-a **VehicleType** (composition)
- Each driver has exactly one vehicle type
- Cannot be changed after registration
## Interaction Flow Diagrams
### Ride Request Flow
\\\mermaid
sequenceDiagram
    participant User
    participant Main
    participant RiderService
    participant RideService
    User->>Main: Request Ride (select rider)
    Main->>RiderService: getRiderById(id)
    RiderService-->>Main: Rider object
    User->>Main: Enter distance
    Main->>RideService: requestRide(id, rider, distance)
    RideService->>RideService: Create Ride(REQUESTED)
    RideService-->>Main: Ride object
    Main-->>User: Ride created
\\\
### Ride Assignment Flow
\\\mermaid
sequenceDiagram
    participant Main
    participant DriverService
    participant RideService
    participant Strategy as MatchingStrategy
    participant Driver
    Main->>DriverService: getAvailableDrivers()
    DriverService-->>Main: List\<Driver\>
    Main->>RideService: assignDriver(rideId, drivers)
    RideService->>Strategy: findDriver(rider, drivers)
    Strategy-->>RideService: Selected Driver
    RideService->>Driver: setAvailable(false)
    RideService-->>Main: Updated Ride(ASSIGNED)
\\\
### Ride Completion Flow
\\\mermaid
sequenceDiagram
    participant Main
    participant RideService
    participant FareStrategy
    participant Driver
    Main->>RideService: completeRide(rideId)
    RideService->>FareStrategy: calculateFare(ride)
    FareStrategy-->>RideService: amount
    RideService->>RideService: Create FareReceipt
    RideService->>Driver: incrementCompletedRides()
    RideService->>Driver: setAvailable(true)
    RideService-->>Main: Ride(COMPLETED)
\\\
## Service-to-Service Relationships
\\\mermaid
graph TB
    Main[Main.java]
    Main -->|creates| RS[RiderService]
    Main -->|creates| DS[DriverService]
    Main -->|creates| RideS[RideService]
    Main -->|uses| IdGen[IdGenerator]
    Main -->|uses| Geocoder[OfflineGeocoder]
    RideS -->|queries| RS
    RideS -->|queries| DS
    RideS -->|uses| MatchStrategy[RideMatchingStrategy]
    RideS -->|uses| FareStrategy[FareStrategy]
    MatchStrategy -->|reads| DS
    FareStrategy -->|reads| RideService
\\\
## Data Flow State Machine
\\\mermaid
stateDiagram-v2
    [*] --> RiderRegistered: registerRider()
    [*] --> DriverRegistered: registerDriver()
    RiderRegistered --> RideRequested: requestRide()
    RideRequested --> RideAssigned: assignDriver()
    RideAssigned --> RideCompleted: completeRide()
    RideCompleted --> [*]
    RideRequested --> RideCancelled: cancelRide()
    RideAssigned --> RideCancelled: cancelRide()
    RideCancelled --> [*]
    DriverRegistered --> DriverAvailable: available = true
    DriverAvailable --> DriverBusy: assignDriver()
    DriverBusy --> DriverAvailable: completeRide()
    DriverBusy --> DriverAvailable: cancelRide()
\\\
## Object Lifecycle
### Rider Lifecycle
1. **Creation**: registerRider() → Rider object created
2. **Active**: Rider can request multiple rides
3. **Persistent**: Rider object remains until application shutdown
### Driver Lifecycle
1. **Creation**: registerDriver() → Driver object created with availability=true
2. **Available**: Can be assigned new rides
3. **Busy**: Assigned to ride, unavailable for new assignments
4. **Available Again**: After ride completion
5. **Persistent**: Driver object remains until application shutdown
### Ride Lifecycle
1. **Requested** (initial): Created with status=REQUESTED
2. **Assigned**: Matched with driver, status=ASSIGNED
3. **Completed**: Fare calculated, receipt generated, status=COMPLETED
   OR
3. **Cancelled**: Cancelled before/at assignment, status=CANCELLED
### FareReceipt Lifecycle
1. **Created**: Generated when ride completes
2. **Attached**: Added to ride object
3. **Persistent**: Remains for audit/history
## Dependency Graph
\\\mermaid
graph LR
    Main[Main]
    MainServices[Services]
    ServiceAbstractions[Strategy Abstractions]
    ServiceImpl[Strategy Implementations]
    Entities[Domain Entities]
    Utils[Utilities]
    Main -->|depends on| MainServices
    Main -->|depends on| Utils
    MainServices -->|depends on| Entities
    MainServices -->|depends on| ServiceAbstractions
    ServiceAbstractions -->|implemented by| ServiceImpl
    ServiceImpl -->|processes| Entities
    Utils -->|generates/maps| Entities
\\\
