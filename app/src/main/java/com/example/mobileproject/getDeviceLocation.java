package com.example.mobileproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class getDeviceLocation implements LocationListener {
    private MyLocation deviceLocation;
    private ArrayList<MyLocation> locations;
    protected LocationManager locationManager;

    public getDeviceLocation(Context context, MyLocation deviceLocation, ArrayList<MyLocation> locations) {
        this.deviceLocation = deviceLocation;
        this.locations = locations;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location x) {
        deviceLocation = new MyLocation(x.getLatitude(), x.getLongitude());

        getDistance getDistance = new getDistance(deviceLocation, locations);
        getDistance.calculateDistance();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}