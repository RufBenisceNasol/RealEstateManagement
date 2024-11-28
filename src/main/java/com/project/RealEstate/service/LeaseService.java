package com.project.RealEstate.service;

import com.project.RealEstate.dao.LeaseDAO;
import com.project.RealEstate.models.Lease;

import java.time.LocalDate;
import java.util.List;

public class LeaseService {
    private final LeaseDAO leaseDAO = new LeaseDAO();

    // Create a new Lease
    public Lease createLease(int tenantId, int propertyId, LocalDate startDate, LocalDate endDate, double rentAmount) {
        validateLeaseDetails(startDate, endDate, rentAmount);
        Lease lease = new Lease(tenantId, propertyId, startDate, endDate, rentAmount);
        return leaseDAO.save(lease);
    }

    // Read a Lease by ID
    public Lease getLeaseById(int id) {
        return leaseDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Lease not found with ID: " + id));
    }

    // Update a Lease
    public Lease updateLease(int id, int tenantId, int propertyId, LocalDate startDate, LocalDate endDate, double rentAmount) {
        validateLeaseDetails(startDate, endDate, rentAmount);
        Lease lease = getLeaseById(id);
        lease.setTenantId(tenantId);
        lease.setPropertyId(propertyId);
        lease.setStartDate(startDate);
        lease.setEndDate(endDate);
        lease.setRentAmount(rentAmount);
        return leaseDAO.update(lease);
    }

    // Delete a Lease
    public boolean deleteLease(int id) {
        return leaseDAO.delete(id);
    }

    // List All Leases
    public List<Lease> getAllLeases() {
        return leaseDAO.findAll();
    }

    // Private validation method
    private void validateLeaseDetails(LocalDate startDate, LocalDate endDate, double rentAmount) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        if (rentAmount <= 0) {
            throw new IllegalArgumentException("Rent amount must be greater than zero.");
        }
    }
}
