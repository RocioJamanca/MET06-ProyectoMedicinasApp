package com.example.androidarduino;

public class Medicine {

    public String medicine_photo;
    public String day;
    public String medicine_name;

    public String getTook() {
        return took;
    }

    public void setTook(String took) {
        this.took = took;
    }

    public String took;

    public Medicine(String medicine_photo, String medicine_name, String took) {
        this.medicine_photo = medicine_photo;
        this.medicine_name = medicine_name;
        this.took =took;
    }

    public Medicine( ) {

    }

    public String medicine_photo() {
        return medicine_photo;
    }

    public void setmedicine_photo(String profile_photo) {
        this.medicine_photo = profile_photo;
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
