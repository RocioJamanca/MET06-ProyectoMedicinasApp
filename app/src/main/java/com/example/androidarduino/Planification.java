package com.example.androidarduino;

public class Planification {
    String medicina;
    String day;

    public Planification() {

    }

    public Planification(String medicina, String day) {
        this.medicina = medicina;
        this.day = day;
    }

    public String getMedicina() {
        return medicina;
    }

    public void setMedicina(String medicina) {
        this.medicina = medicina;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
