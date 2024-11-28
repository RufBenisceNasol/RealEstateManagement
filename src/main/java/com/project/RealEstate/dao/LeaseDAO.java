package com.project.RealEstate.dao;

import com.project.RealEstate.models.Lease;
import com.project.RealEstate.utils.DatabaseConnection;
import com.project.RealEstate.models.Property;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeaseDAO {

    private final PropertyDAO propertyDAO = new PropertyDAO();

    // Save Lease (Insert into the leases table)
    public Lease save(Lease lease) {
        String query = "INSERT INTO leases (tenantId, propertyId, startDate, endDate, rentAmount) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, lease.getTenantId());
            statement.setInt(2, lease.getPropertyId());
            statement.setDate(3, java.sql.Date.valueOf(lease.getStartDate())); // Convert LocalDate to SQL Date
            statement.setDate(4, java.sql.Date.valueOf(lease.getEndDate())); // Convert LocalDate to SQL Date
            statement.setDouble(5, lease.getRentAmount());

            int result = statement.executeUpdate();

            if (result > 0) {
                // Get the generated lease ID
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    lease.setId(generatedKeys.getInt(1)); // Set the generated ID to the lease object

                    // Update property availability to OCCUPIED when lease is created
                    propertyDAO.updatePropertyAvailability(lease.getPropertyId(), "OCCUPIED");

                    return lease;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // LeaseDAO.java
    public Lease update(Lease lease) {
        String query = "UPDATE leases SET tenantId = ?, propertyId = ?, startDate = ?, endDate = ?, rentAmount = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, lease.getTenantId());
            statement.setInt(2, lease.getPropertyId());
            statement.setDate(3, Date.valueOf(lease.getStartDate()));  // Convert LocalDate to SQL Date
            statement.setDate(4, Date.valueOf(lease.getEndDate()));    // Convert LocalDate to SQL Date
            statement.setDouble(5, lease.getRentAmount());
            statement.setInt(6, lease.getId()); // Set the lease ID for the update

            int result = statement.executeUpdate();
            if (result > 0) {
                return lease; // Return the updated lease object
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // If update fails, return null
    }


    // Delete Lease
    public boolean delete(int leaseId) {
        String query = "DELETE FROM leases WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, leaseId);
            int result = statement.executeUpdate();

            if (result > 0) {
                // Retrieve the property ID from the deleted lease to revert its availability to AVAILABLE
                String selectQuery = "SELECT propertyId FROM leases WHERE id = ?";
                try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                    selectStatement.setInt(1, leaseId);
                    ResultSet resultSet = selectStatement.executeQuery();
                    if (resultSet.next()) {
                        int propertyId = resultSet.getInt("propertyId");
                        // Update the property availability to AVAILABLE
                        propertyDAO.updatePropertyAvailability(propertyId, "AVAILABLE");
                    }
                }

                return true; // Deletion was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Find Lease by ID
    public Optional<Lease> findById(int leaseId) {
        String query = "SELECT * FROM leases WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, leaseId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Lease lease = new Lease(
                        resultSet.getInt("tenantId"),
                        resultSet.getInt("propertyId"),
                        resultSet.getDate("startDate").toLocalDate(),
                        resultSet.getDate("endDate").toLocalDate(),
                        resultSet.getDouble("rentAmount")
                );
                lease.setId(resultSet.getInt("id"));
                return Optional.of(lease);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Find All Leases
    public List<Lease> findAll() {
        List<Lease> leases = new ArrayList<>();
        String query = "SELECT * FROM leases";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Lease lease = new Lease(
                        resultSet.getInt("tenantId"),
                        resultSet.getInt("propertyId"),
                        resultSet.getDate("startDate").toLocalDate(),
                        resultSet.getDate("endDate").toLocalDate(),
                        resultSet.getDouble("rentAmount")
                );
                lease.setId(resultSet.getInt("id"));
                leases.add(lease);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leases;
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
}
