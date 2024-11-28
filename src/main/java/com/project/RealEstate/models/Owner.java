package com.project.RealEstate.models;

import org.mindrot.jbcrypt.BCrypt;

public class Owner {

    private int id; // Primary Key
    private String name;
    private String email;
    private String contactNumber;
    private String password; // Hashed password

    // Constructor without ID (useful for creating new owners)
    public Owner(String name, String email, String contactNumber, String password) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        setPassword(password);  // Hash the password during object creation
    }

    // Getter and Setter methods for each field
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // Hash password before saving it
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Hash password using BCrypt
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Validate password
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
