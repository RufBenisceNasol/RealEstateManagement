package com.project.RealEstate.models;

public class Property {

    private int id; // Primary Key
    private String name;
    private String address;
    private double price;
    private Availability availability; // Enum for availability
    private Type type; // Enum for property type
    private int ownerId; // Foreign Key (Owner)

    // Enum for property availability
    public enum Availability {
        AVAILABLE,
        OCCUPIED
    }

    // Enum for property type
    public enum Type {
        HOUSE,
        CONDOMINIUM,
        APARTMENT
    }

    // Constructor without ID (useful for creating new properties)
    public Property(String name, String address, double price, Availability availability, Type type, int ownerId) {
        this.name = name;
        this.address = address;
        this.price = price;
        this.availability = availability;
        this.type = type;
        this.ownerId = ownerId;
    }

    // Getter and setter methods for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
