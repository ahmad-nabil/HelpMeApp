package com.ahmad.helpmeapp.customData;

public class CustomOrdersData {
    String name;
    String rate;
    String experience;

    public CustomOrdersData(String name, String rate, String experience) {
        this.name = name;
        this.rate = rate;
        this.experience = experience;
    }
    public CustomOrdersData() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
