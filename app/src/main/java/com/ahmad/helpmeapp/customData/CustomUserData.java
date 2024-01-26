package com.ahmad.helpmeapp.customData;

public class CustomUserData {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String typeCar;
    private String colorCar;
    private String carModel;
    private String userId;
    public CustomUserData() {
    }

    public CustomUserData(String name, String email, String password, String phone, String typeCar, String colorCar, String carModel, String userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.typeCar = typeCar;
        this.colorCar = colorCar;
        this.carModel = carModel;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypeCar() {
        return typeCar;
    }

    public void setTypeCar(String typeCar) {
        this.typeCar = typeCar;
    }

    public String getColorCar() {
        return colorCar;
    }

    public void setColorCar(String colorCar) {
        this.colorCar = colorCar;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
