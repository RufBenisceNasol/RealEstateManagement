package com.project.RealEstate.controllers;

import com.project.RealEstate.dao.PropertyDAO;
import com.project.RealEstate.models.Property;
import com.project.RealEstate.models.Property.Availability;
import com.project.RealEstate.models.Property.Type;

import java.util.List;

public class PropertyController {

    private PropertyDAO propertyDAO;

    public PropertyController() {
        this.propertyDAO = new PropertyDAO();
    }

    // Create Property (Owner can create property)
    public void createProperty(String name, String address, double price, Availability availability, Type type, int ownerId) {
        Property property = new Property(name, address, price, availability, type, ownerId);
        if (propertyDAO.addProperty(property)) {
            System.out.println("Property created successfully!");
        } else {
            System.out.println("Failed to create property.");
        }
    }

    // Get Property by ID
    public Property getPropertyById(int propertyId) {
        Property property = propertyDAO.getPropertyById(propertyId);
        if (property != null) {
            System.out.println("Property fetched: " + property.getName());
        } else {
            System.out.println("Property not found.");
        }
        return property;
    }

    // Get all Properties by Owner ID
    public void getAllPropertiesByOwnerId(int ownerId) {
        List<Property> properties = propertyDAO.getAllPropertiesByOwnerId(ownerId);
        if (!properties.isEmpty()) {
            for (Property property : properties) {
                System.out.println(property.getName() + " - " + property.getAddress());
            }
        } else {
            System.out.println("No properties found for this owner.");
        }
    }

    // Update Property
    public void updateProperty(int propertyId, String name, String address, double price, Availability availability, Type type, int ownerId) {
        Property property = new Property(name, address, price, availability, type, ownerId);
        property.setId(propertyId);
        if (propertyDAO.updateProperty(property)) {
            System.out.println("Property updated successfully!");
        } else {
            System.out.println("Failed to update property.");
        }
    }

    // Delete Property
    public void deleteProperty(int propertyId, int ownerId) {
        if (propertyDAO.deleteProperty(propertyId, ownerId)) {
            System.out.println("Property deleted successfully!");
        } else {
            System.out.println("Failed to delete property.");
        }
    }

    // Get all Properties (No filter by owner)
    public void getAllProperties() {
        List<Property> properties = propertyDAO.getAllPropertiesByOwnerId(0); // 0 to fetch all properties
        if (!properties.isEmpty()) {
            for (Property property : properties) {
                System.out.println(property.getName() + " - " + property.getAddress());
            }
        } else {
            System.out.println("No properties available.");
        }
    }
}
