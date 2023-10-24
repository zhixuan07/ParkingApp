package com.example.parkingappinti;

public class User {

    String email;
    String name;
    String phone;

    public User(){

    }
    public User(String email,String name, String phone){
        this.email = email;
        this.phone = phone;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
