package com.ahmad.helpmeapp.customData;

public class CustomSpinnerList {
    String Address;
    int id_Img;

    public CustomSpinnerList(String address, int id_Img) {
        Address = address;
        this.id_Img = id_Img;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getId_Img() {
        return id_Img;
    }

    public void setId_Img(int id_Img) {
        this.id_Img = id_Img;
    }
}
