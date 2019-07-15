package com.example.uk.kisanmarket;

public class User {
    private String name, username, email, phone, address, type;

    public User(String name, String username, String email, String phone, String address, String type) {
        this.name=name;
        this.username = username;
        this.email = email;
        this.phone=phone;
        this.address=address;
        this.type= type;
    }

    public String getName() { return name; }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() { return phone; }

    public String getAddress() { return address; }

    public String getType() {
        return type;
    }
}
