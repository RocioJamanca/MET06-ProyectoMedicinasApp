package com.example.androidarduino;

public class Device {
    int sensor;
    int leds;
    int light;
    int dataInt;
    int testInt;

    public Device(int sensor, int leds, int light, int dataInt, int testInt) {
        this.sensor = sensor;
        this.leds = leds;
        this.light = light;
        this.dataInt = dataInt;
        this.testInt = testInt;
    }

    public Device() {
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public int getLeds() {
        return leds;
    }

    public void setLeds(int leds) {
        this.leds = leds;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getDataInt() {
        return dataInt;
    }

    public void setDataInt(int dataInt) {
        this.dataInt = dataInt;
    }

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }
}
