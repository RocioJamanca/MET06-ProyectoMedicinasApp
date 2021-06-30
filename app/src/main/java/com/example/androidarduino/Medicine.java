package com.example.androidarduino;

public class Medicine {

    public String profile_photo;
    public String day;
    public String medicine_name;

    public Medicine(String profile_photo, String day, String medicine_name) {
        this.profile_photo = profile_photo;
        this.day = day;
        this.medicine_name = medicine_name;
    }

    public Medicine( ) {

    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }
}
