package com.project.RealEstate.dao;

import com.project.RealEstate.models.Owner;
import com.project.RealEstate.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAO {

    // Create a new owner
    public boolean createOwner(Owner owner) {
        String query = "INSERT INTO owner (name, email, contactNumber, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, owner.getName());
            statement.setString(2, owner.getEmail());
            statement.setString(3, owner.getContactNumber());
            statement.setString(4, owner.getPassword());

            int result = statement.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    owner.setId(generatedKeys.getInt(1)); // Set the generated ID to the owner object
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve an owner by ID
    public Owner getOwnerById(int ownerId) {
        String query = "SELECT * FROM owner WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Owner owner = new Owner(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("contactNumber"),
                        resultSet.getString("password") // Hashed password from DB
                );
                owner.setId(resultSet.getInt("id"));
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieve all owners
    public List<Owner> getAllOwners() {
        List<Owner> owners = new ArrayList<>();
        String query = "SELECT * FROM owner";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Owner owner = new Owner(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("contactNumber"),
                        resultSet.getString("password") // Hashed password from DB
                );
                owner.setId(resultSet.getInt("id"));
                owners.add(owner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
    }

    // Update an existing owner
    public boolean updateOwner(Owner owner) {
        String query = "UPDATE owner SET name = ?, email = ?, contactNumber = ?, password = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, owner.getName());
            statement.setString(2, owner.getEmail());
            statement.setString(3, owner.getContactNumber());
            statement.setString(4, owner.getPassword());
            statement.setInt(5, owner.getId());

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete an owner
    public boolean deleteOwner(int ownerId) {
        String query = "DELETE FROM owner WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ownerId);

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
