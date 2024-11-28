package com.project.RealEstate.models;

import java.time.LocalDate;

public class Invoice {

    private int id; // Primary Key
    private int leaseId; // Foreign Key (Lease)
    private LocalDate issueDate;
    private LocalDate dueDate;
    private double amount;
    private PaymentStatus paymentStatus; // Enum for payment status

    // Enum for payment status
    public enum PaymentStatus {
        PENDING,
        PAID
    }

    // Constructor without ID (useful for creating new invoices)
    public Invoice(int leaseId, LocalDate issueDate, LocalDate dueDate, double amount, PaymentStatus paymentStatus) {
        this.leaseId = leaseId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
