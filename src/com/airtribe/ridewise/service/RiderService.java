package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Rider;
import java.util.ArrayList;
import java.util.List;

public class RiderService {
    private List<Rider> riders;

    public RiderService() {
        this.riders = new ArrayList<>();
    }

    public Rider registerRider(String id, String name, String location) {
        Rider rider = new Rider(id, name, location);
        riders.add(rider);
        return rider;
    }

    public Rider getRiderById(String id) {
        for (Rider rider : riders) {
            if (rider.getId().equals(id)) {
                return rider;
            }
        }
        return null;
    }

    public List<Rider> getAllRiders() {
        return new ArrayList<>(riders);
    }

    public int getTotalRiders() {
        return riders.size();
    }
}
