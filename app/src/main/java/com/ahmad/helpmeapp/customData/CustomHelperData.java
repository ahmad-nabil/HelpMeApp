package com.ahmad.helpmeapp.customData;

public class CustomHelperData {
    private String FullName;
    private String Email;
    private String Password;
    private String Phone;
    private String LevelExp;
    private String TypeExp;
    private String LocationHelper;

    private double Longitude;
    private double Latitude;
    private String Id;
    public CustomHelperData() {}

    public CustomHelperData(String fullName, String email, String password, String phone, String levelExp, String typeExp, String locationHelper, double longitude, double latitude, String id) {
        FullName = fullName;
        Email = email;
        Password = password;
        Phone = phone;
        LevelExp = levelExp;
        TypeExp = typeExp;
        LocationHelper = locationHelper;
        Longitude = longitude;
        Latitude = latitude;
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public String getLocationHelper() {
        return LocationHelper;
    }

    public void setLocationHelper(String locationHelper) {
        LocationHelper = locationHelper;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
