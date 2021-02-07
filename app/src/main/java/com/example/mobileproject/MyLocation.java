package com.example.mobileproject;

public class MyLocation implements java.io.Serializable {
    private String locationName, locationAddress, locationDescription;
    private Double locationRating, locationLatitude, locationLongitude, locationDistance;
    private int numOfRating;
    private String[] locationPictures;
    private MyTimeZone locationTimeZone;

    public MyLocation(String locationName, String locationAddress, String locationDescription,
                      int numOfRating, Double locationRating, Double locationLatitude, Double locationLongitude,
                      String[] locationPictures, MyTimeZone locationTimeZone) {
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationDescription = locationDescription;
        this.numOfRating = numOfRating;
        this.locationRating = locationRating;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationPictures = locationPictures;
        this.locationTimeZone = locationTimeZone;
    }

    public MyLocation(String locationName, String locationAddress, Double locationLatitude, Double locationLongitude) {
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public MyLocation(Double locationLatitude, Double locationLongitude) {
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public int getNumOfRating() {
        return numOfRating;
    }

    public void setNumOfRating(int numOfRating) {
        this.numOfRating = numOfRating;
    }

    public Double getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(Double locationRating) {
        this.locationRating = locationRating;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public Double getLocationDistance() {
        return locationDistance;
    }

    public void setLocationDistance(Double locationDistance) {
        this.locationDistance = locationDistance;
    }

    public String[] getLocationPictures() {
        return locationPictures;
    }

    public void setLocationPictures(String[] locationPictures) {
        this.locationPictures = locationPictures;
    }

    public MyTimeZone getLocationTimeZone() {
        return locationTimeZone;
    }

    public void setLocationTimeZone(MyTimeZone locationTimeZone) {
        this.locationTimeZone = locationTimeZone;
    }
}