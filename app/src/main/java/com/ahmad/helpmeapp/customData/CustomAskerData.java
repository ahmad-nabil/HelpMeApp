package com.ahmad.helpmeapp.customData;

public class CustomAskerData {
    String name;
    String distance;
    String car;
    String colorCar;
    Double longitude;
    Double latitude;

    public CustomAskerData() {
    }

    public CustomAskerData(String name, String distance, String car, String colorCar, Double longitude, Double latitude) {
        this.name = name;
        this.distance = distance;
        this.car = car;
        this.colorCar = colorCar;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getColorCar() {
        return colorCar;
    }

    public void setColorCar(String colorCar) {
        this.colorCar = colorCar;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
