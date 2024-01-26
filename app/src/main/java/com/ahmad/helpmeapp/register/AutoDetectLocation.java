package com.ahmad.helpmeapp.register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class AutoDetectLocation implements PassLocationData {
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;
    PassLocationData passLocationData;

    public AutoDetectLocation(Context context, PassLocationData passLocationData) {
        this.passLocationData = passLocationData;
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }


    @SuppressLint("MissingPermission")
    //get location update using FusedLocationProviderClient and send data using pass Location Data
    public void startLocationUpdates() {
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, null).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Geocoder geocoder = new Geocoder(context);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        Address AddressLine = addresses.get(0);
                        passLocationData.onDataPass(longitude, latitude, AddressLine.getAddressLine(0));
                    } catch (IOException e) {

                    }

                } else {
                    Toast.makeText(context, "please try manual way", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "couldn't get your location please try manual way", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDataPass(double longitude, double latitude, String data) {

    }
}
