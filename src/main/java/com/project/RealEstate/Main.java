package com.project.RealEstate;

import com.project.RealEstate.controllers.MaintenanceRequestController;

public class Main {
    public static void main(String[] args) {
        MaintenanceRequestController maintenanceRequestController = new MaintenanceRequestController();

        int requestId = 8; // Example request ID to archive

        // Archive the maintenance request
        boolean result = maintenanceRequestController.archiveMaintenanceRequest(requestId);

        if (result) {
            System.out.println("Request successfully archived.");
        } else {
            System.out.println("Failed to archive the request.");
        }
    }
}
