package com.project.RealEstate.controllers;

import com.project.RealEstate.dao.MaintenanceRequestDAO;
import com.project.RealEstate.models.MaintenanceRequest;

import java.time.LocalDate;

public class MaintenanceRequestController {

    private MaintenanceRequestDAO maintenanceRequestDAO = new MaintenanceRequestDAO();

    // Method to create a maintenance request
    public void createMaintenanceRequestByTenant(int tenantId, String description) {
        MaintenanceRequest.Status status = MaintenanceRequest.Status.PENDING;  // Default status

        // Step 1: Create the maintenance request object
        MaintenanceRequest request = new MaintenanceRequest(
                tenantId,
                0,  // ownerId will be set based on the propertyId
                0,  // propertyId will be set based on the tenant's lease
                LocalDate.now(),
                status,
                description
        );

        // Step 2: Send the maintenance request to the DAO for insertion
        boolean result = maintenanceRequestDAO.createRequestByTenant(request);

        if (result) {
            System.out.println("Maintenance request has been successfully submitted.");
        } else {
            System.out.println("Failed to submit the maintenance request.");
        }
    }

    // Method for the owner to respond to a maintenance request
    public boolean respondToMaintenanceRequest(int requestId, boolean isApproved, LocalDate startDate) {
        return maintenanceRequestDAO.respondToRequest(requestId, isApproved, startDate);
    }

    // Method to archive a maintenance request
    public boolean archiveMaintenanceRequest(int requestId) {
        return maintenanceRequestDAO.archiveRequest(requestId);
    }
}
