package com.project.RealEstate.dao;

import com.project.RealEstate.models.Property;
import com.project.RealEstate.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    // Create Property (Owner can create property)
    public boolean addProperty(Property property) {
        String query = "INSERT INTO property (name, address, price, availability, type, ownerId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, property.getName());
            statement.setString(2, property.getAddress());
            statement.setDouble(3, property.getPrice());
            statement.setString(4, property.getAvailability().toString());
            statement.setString(5, property.getType().toString());
            statement.setInt(6, property.getOwnerId());

            int result = statement.executeUpdate();
            return result > 0;  // If result is greater than 0, insertion was successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // If any error occurs, return false
        }
    }

    // Get Property by ID
    public Property getPropertyById(int propertyId) {
        Property property = null;
        String query = "SELECT * FROM property WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, propertyId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                property = new Property(
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getDouble("price"),
                        Property.Availability.valueOf(resultSet.getString("availability")),
                        Property.Type.valueOf(resultSet.getString("type")),
                        resultSet.getInt("ownerId")
                );
                property.setId(resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return property;
    }

    // Get all Properties by Owner ID
    public List<Property> getAllPropertiesByOwnerId(int ownerId) {
        List<Property> properties = new ArrayList<>();
        String query = "SELECT * FROM property WHERE ownerId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Property property = new Property(
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getDouble("price"),
                        Property.Availability.valueOf(resultSet.getString("availability")),
                        Property.Type.valueOf(resultSet.getString("type")),
                        resultSet.getInt("ownerId")
                );
                property.setId(resultSet.getInt("id"));
                properties.add(property);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }

    // Update Property
    public boolean updateProperty(Property property) {
        String query = "UPDATE property SET name = ?, address = ?, price = ?, availability = ?, type = ? WHERE id = ? AND ownerId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, property.getName());
            statement.setString(2, property.getAddress());
            statement.setDouble(3, property.getPrice());
            statement.setString(4, property.getAvailability().toString());
            statement.setString(5, property.getType().toString());
            statement.setInt(6, property.getId());
            statement.setInt(7, property.getOwnerId());

            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete Property
    public boolean deleteProperty(int propertyId, int ownerId) {
        String query = "DELETE FROM property WHERE id = ? AND ownerId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, propertyId);
            statement.setInt(2, ownerId);

            int result = statement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update property availability
    public boolean updatePropertyAvailability(int propertyId, String availability) {
        String query = "UPDATE property SET availability = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, availability);
            statement.setInt(2, propertyId);

            int result = statement.executeUpdate();
            return result > 0; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if there was an error
    }
}
