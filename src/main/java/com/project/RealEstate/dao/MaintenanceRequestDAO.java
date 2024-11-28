package com.project.RealEstate.dao;

import com.project.RealEstate.models.MaintenanceRequest;
import com.project.RealEstate.models.Lease;
import com.project.RealEstate.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class MaintenanceRequestDAO {

    private LeaseDAO leaseDAO = new LeaseDAO();  // To fetch tenant's lease details

    // Method to create a new maintenance request by a tenant
    public boolean createRequestByTenant(MaintenanceRequest request) {
        // Step 1: Get the propertyId the tenant is renting using the LeaseDAO
        Lease tenantLease = leaseDAO.findActiveLeaseByTenantId(request.getTenantId());

        if (tenantLease == null) {
            System.out.println("No active lease found for tenant.");
            return false;  // If no active lease found, return false
        }

        // Step 2: Get the ownerId based on the propertyId from the lease
        int propertyId = tenantLease.getPropertyId();
        int ownerId = getOwnerIdByPropertyId(propertyId);  // Method to fetch ownerId based on propertyId

        if (ownerId == -1) {
            System.out.println("Owner not found for this property.");
            return false;
        }

        // Step 3: Create the maintenance request and insert it into the database
        String query = "INSERT INTO maintenancerequest (tenantId, ownerId, propertyId, requestDate, status, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, request.getTenantId());
            statement.setInt(2, ownerId);  // Set ownerId for the property
            statement.setInt(3, propertyId);
            statement.setDate(4, Date.valueOf(request.getRequestDate()));
            statement.setString(5, request.getStatus().name());  // Default to PENDING
            statement.setString(6, request.getDescription());

            int result = statement.executeUpdate();
            return result > 0;  // Return true if the request was successfully inserted

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to fetch ownerId based on propertyId
    public int getOwnerIdByPropertyId(int propertyId) {
        String query = "SELECT ownerId FROM property WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, propertyId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("ownerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if no owner is found
    }

    // Method to find an active lease by tenantId
    public Lease findActiveLeaseByTenantId(int tenantId) {
        String query = "SELECT * FROM leases WHERE tenantId = ? AND startDate <= ? AND endDate >= ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Get the current date to compare with the lease's start and end dates
            LocalDate currentDate = LocalDate.now();

            // Set parameters: tenantId, current date for both startDate and endDate comparisons
            statement.setInt(1, tenantId);
            statement.setDate(2, Date.valueOf(currentDate));
            statement.setDate(3, Date.valueOf(currentDate));

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // If an active lease is found, create a Lease object and return it
                Lease lease = new Lease(
                        resultSet.getInt("tenantId"),
                        resultSet.getInt("propertyId"),
                        resultSet.getDate("startDate").toLocalDate(),
                        resultSet.getDate("endDate").toLocalDate(),
                        resultSet.getDouble("rentAmount")
                );
                lease.setId(resultSet.getInt("id"));
                return lease;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no active lease is found
    }

    // Method for the owner to approve or decline a maintenance request
    public boolean respondToRequest(int requestId, boolean isApproved, LocalDate startDate) {
        String query;

        // Determine if the request is approved or declined
        if (isApproved) {
            // If approved, update the status to APPROVED and set the start date
            query = "UPDATE maintenancerequest SET status = ?, scheduledDate = ? WHERE id = ?";
        } else {
            // If declined, only update the status to DECLINED
            query = "UPDATE maintenancerequest SET status = ? WHERE id = ?";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters based on approval or decline
            if (isApproved) {
                statement.setString(1, MaintenanceRequest.Status.APPROVED.name());
                statement.setDate(2, Date.valueOf(startDate)); // Set the start date
                statement.setInt(3, requestId); // Set the request ID
            } else {
                statement.setString(1, MaintenanceRequest.Status.DECLINED.name());
                statement.setInt(2, requestId); // Set the request ID
            }

            int result = statement.executeUpdate();
            return result > 0;  // Return true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false if an error occurs
        }
    }

    // Method to mark a maintenance request as archived
    public boolean archiveRequest(int requestId) {
        String query = "UPDATE maintenancerequest SET status = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Update the status to 'ARCHIVED'
            statement.setString(1, MaintenanceRequest.Status.APPROVED.name() + "_ARCHIVED");
            statement.setInt(2, requestId);

            int result = statement.executeUpdate();
            return result > 0; // Return true if the update was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
