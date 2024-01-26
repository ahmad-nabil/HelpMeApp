package com.ahmad.helpmeapp.customData;

public class CustomLocationHelper {
    String Name;
    String Phone;
    String LevelExp;
    String TypeExp;
Double Longitude;
Double Latitude;
String id;
    public CustomLocationHelper() {
    }

    public CustomLocationHelper(String name, String phone, String levelExp, String typeExp, Double longitude, Double latitude, String id) {
        Name = name;
        Phone = phone;
        LevelExp = levelExp;
        TypeExp = typeExp;
        Longitude = longitude;
        Latitude = latitude;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLevelExp() {
        return LevelExp;
    }

    public void setLevelExp(String levelExp) {
        LevelExp = levelExp;
    }

    public String getTypeExp() {
        return TypeExp;
    }

    public void setTypeExp(String typeExp) {
        TypeExp = typeExp;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
