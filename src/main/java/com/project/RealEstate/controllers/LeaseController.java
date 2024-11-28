package com.project.RealEstate.controllers;

import com.project.RealEstate.dao.LeaseDAO;
import com.project.RealEstate.models.Lease;
import com.project.RealEstate.models.Property;
import com.project.RealEstate.dao.PropertyDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LeaseController {

    private final LeaseDAO leaseDAO = new LeaseDAO();
    private final PropertyDAO propertyDAO = new PropertyDAO();

    // Owner can see all leases
    public List<Lease> getAllLeases() {
        return leaseDAO.findAll();
    }

    // Owner can create a lease
    public Lease createLease(int tenantId, int propertyId, LocalDate startDate, LocalDate endDate) {
        Property property = propertyDAO.getPropertyById(propertyId);
        if (property != null && property.getAvailability().equals(Property.Availability.AVAILABLE)) {
            // Create a lease and set the rent amount to the property price
            Lease lease = new Lease(tenantId, propertyId, startDate, endDate, property.getPrice());
            return leaseDAO.save(lease);
        }
        return null; // Return null if the property doesn't exist or is not available
    }

    //edit lease by ID
    public Lease editLeaseById(int leaseId, LocalDate newStartDate, LocalDate newEndDate) {
        // Fetch the lease by its ID
        Optional<Lease> leaseOptional = leaseDAO.findById(leaseId);

        if (leaseOptional.isPresent()) {
            Lease lease = leaseOptional.get();

            // Edit the lease details
            lease.setStartDate(newStartDate);  // Set the new start date
            lease.setEndDate(newEndDate);      // Set the new end date

            // Perform the update using the LeaseDAO
            return leaseDAO.update(lease);
        }

        return null; // If lease not found, return null
    }


    // Owner can delete a lease
    public boolean deleteLease(int leaseId) {
        return leaseDAO.delete(leaseId);
    }

    // Tenant can view their own leases
    public List<Lease> getLeasesByTenant(int tenantId) {
        List<Lease> allLeases = leaseDAO.findAll();
        return allLeases.stream()
                .filter(lease -> lease.getTenantId() == tenantId)
                .toList();
    }

    // Get lease details by lease ID
    public Optional<Lease> getLeaseById(int leaseId) {
        return leaseDAO.findById(leaseId);
    }
}
