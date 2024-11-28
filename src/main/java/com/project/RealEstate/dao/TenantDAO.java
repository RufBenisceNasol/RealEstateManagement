package com.project.RealEstate.dao;

import com.project.RealEstate.models.Tenant;
import com.project.RealEstate.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TenantDAO {

    // Create Tenant
    public boolean createTenant(Tenant tenant) {
        String query = "INSERT INTO tenant (name, email, contactNumber, password, ownerId) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Hash the password before storing it in the database
            String hashedPassword = BCrypt.hashpw(tenant.getPassword(), BCrypt.gensalt());

            statement.setString(1, tenant.getName());
            statement.setString(2, tenant.getEmail());
            statement.setString(3, tenant.getContactNumber());
            statement.setString(4, hashedPassword);  // Store the hashed password
            statement.setInt(5, tenant.getOwnerId());  // Add Owner ID to Tenant

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get Tenant by ID
    public Tenant getTenantById(int tenantId) {
        String query = "SELECT * FROM tenant WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tenantId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToTenant(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all Tenants
    public List<Tenant> getAllTenants() {
        List<Tenant> tenants = new ArrayList<>();
        String query = "SELECT * FROM tenant";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tenants.add(mapResultSetToTenant(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tenants;
    }

    // Update Tenant
    public boolean updateTenant(Tenant tenant) {
        String query = "UPDATE tenant SET name = ?, email = ?, contactNumber = ?, password = ?, ownerId = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Hash the password before storing it
            String hashedPassword = BCrypt.hashpw(tenant.getPassword(), BCrypt.gensalt());

            statement.setString(1, tenant.getName());
            statement.setString(2, tenant.getEmail());
            statement.setString(3, tenant.getContactNumber());
            statement.setString(4, hashedPassword);  // Store the hashed password
            statement.setInt(5, tenant.getOwnerId());
            statement.setInt(6, tenant.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Tenant
    public boolean deleteTenant(int tenantId) {
        String query = "DELETE FROM tenant WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, tenantId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper: Map ResultSet to Tenant object
    private Tenant mapResultSetToTenant(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String contactNumber = resultSet.getString("contactNumber");
        String password = resultSet.getString("password");
        int ownerId = resultSet.getInt("ownerId");

        return new Tenant(id, name, email, contactNumber, password, ownerId);
    }

    // Helper: Validate password against stored hash
    public boolean validatePassword(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }
}
