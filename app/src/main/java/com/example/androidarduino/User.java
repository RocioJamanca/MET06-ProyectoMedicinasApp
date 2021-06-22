package com.example.androidarduino;

public class User {
    public String email;
    public String password;
    public String name;
    public String surname;
    public String age;
    public String device;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public User(String email, String password, String name, String surname, String age, String device) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.device = device;
    }
}