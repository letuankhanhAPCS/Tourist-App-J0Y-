package com.example.mobileproject;

import java.util.ArrayList;

public class getDistance {
    private MyLocation deviceLocation;
    private ArrayList<MyLocation> locations;

    public getDistance(MyLocation deviceLocation, ArrayList<MyLocation> locations) {
        this.deviceLocation = deviceLocation;
        this.locations = locations;
    }

    public void calculateDistance() {
        for(int i = 0; i < locations.size(); i++) {
            double distance = distanceInKmBetweenEarthCoordinates(locations.get(i).getLocationLatitude(), locations.get(i).getLocationLongitude(),
                                                                    deviceLocation.getLocationLatitude(), deviceLocation.getLocationLongitude());
            locations.get(i).setLocationDistance(distance);
        }
    }

    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double distanceInKmBetweenEarthCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
}
