package com.example.parkingappinti;

import android.widget.EditText;

public class Vehicle {

    String brand;
    String userID;
    String plate;
    public Vehicle(){}

    public Vehicle(String plate, String userID, String brand){
        this.userID = userID;
        this.brand =brand;
        this.plate = plate;
    }

    public String getBrand() {

        return brand;
    }

    public String getUserID() {
        return userID;
    }

    public String getPlate() {
        return plate;
    }
}
