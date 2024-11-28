package com.project.RealEstate.models;

import java.time.LocalDate;

public class MaintenanceRequest {

    private int id; // Primary Key
    private int tenantId; // Foreign Key (Tenant)
    private int ownerId; // Foreign Key (Owner)
    private int propertyId; // Foreign Key (Property)
    private LocalDate requestDate;
    private Status status; // Enum for status
    private LocalDate scheduledDate;
    private String description;

    // Enum for request status
    public enum Status {
        PENDING,
        APPROVED,
        DECLINED,
        APPROVED_ARCHIVED,  // Archived after being approved
        DECLINED_ARCHIVED   // Archived after being declined
    }

    // Constructor without ID (useful for creating new requests)
    public MaintenanceRequest(int tenantId, int ownerId, int propertyId, LocalDate requestDate, Status status, String description) {
        this.tenantId = tenantId;
        this.ownerId = ownerId;
        this.propertyId = propertyId;
        this.requestDate = requestDate;
        this.status = status;
        this.description = description;
    }

    // Getter and setter methods
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Method to update the status and scheduled date
    public void approveRequest(LocalDate scheduledDate) {
        this.status = Status.APPROVED;
        this.scheduledDate = scheduledDate;
    }

    public void declineRequest() {
        this.status = Status.DECLINED;
    }
}
