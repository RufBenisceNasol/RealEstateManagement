package com.project.RealEstate.models;

import java.time.LocalDate;
import java.sql.Date;

public class Lease {
    private int id; // Primary Key
    private int tenantId; // Foreign Key from Tenant
    private int propertyId; // Foreign Key from Property
    private LocalDate startDate;
    private LocalDate endDate;
    private double rentAmount;

    // Constructor
    public Lease(int tenantId, int propertyId, LocalDate startDate, LocalDate endDate, double rentAmount) {
        this.tenantId = tenantId;
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentAmount = rentAmount;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(double rentAmount) {
        this.rentAmount = rentAmount;
    }

    // Convert LocalDate to java.sql.Date for database storage
    public Date getSqlStartDate() {
        return Date.valueOf(startDate);
    }

    public Date getSqlEndDate() {
        return Date.valueOf(endDate);
    }
}
