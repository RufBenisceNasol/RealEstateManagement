package com.project.RealEstate.controllers;

import com.project.RealEstate.dao.OwnerDAO;
import com.project.RealEstate.dao.PropertyDAO;
import com.project.RealEstate.dao.TenantDAO;
import com.project.RealEstate.models.Owner;
import com.project.RealEstate.models.Property;
import com.project.RealEstate.models.Tenant;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class OwnerController {

    private final OwnerDAO ownerDAO;
    private final PropertyDAO propertyDAO;
    private final TenantDAO tenantDAO;

    public OwnerController(OwnerDAO ownerDAO, PropertyDAO propertyDAO, TenantDAO tenantDAO) {
        this.ownerDAO = ownerDAO;
        this.propertyDAO = propertyDAO;
        this.tenantDAO = tenantDAO;
    }

    // Helper: Check if owner exists
    private boolean isOwnerValid(int ownerId) {
        Owner owner = ownerDAO.getOwnerById(ownerId);
        return owner != null;
    }

    // Helper: Check ownership of property
    private boolean isOwnerOfProperty(int propertyId, int ownerId) {
        Property property = propertyDAO.getPropertyById(propertyId);
        return property != null && property.getOwnerId() == ownerId;
    }

    // Create Property
    public String createProperty(String name, String address, double price, Property.Availability availability, Property.Type type, int ownerId) {
        if (!isOwnerValid(ownerId)) {
            return "Error: Owner not found.";
        }

        // Validate inputs (you can expand this as needed)
        if (name == null || name.isEmpty() || address == null || address.isEmpty() || price <= 0) {
            return "Error: Invalid property details.";
        }

        // Create the Property object
        Property property = new Property(name, address, price, availability, type, ownerId);

        // Add property to database
        boolean success = propertyDAO.addProperty(property);
        return success ? "Property created successfully." : "Error: Unable to create property.";
    }

    // Get Property by ID
    public Property getPropertyById(int propertyId, int ownerId) {
        if (!isOwnerOfProperty(propertyId, ownerId)) {
            System.out.println("Error: Property not found or unauthorized access.");
            return null;
        }
        return propertyDAO.getPropertyById(propertyId);
    }

    // Get all Properties by Owner ID
    public List<Property> getAllProperties(int ownerId) {
        if (!isOwnerValid(ownerId)) {
            System.out.println("Error: Owner not found.");
            return null;
        }
        return propertyDAO.getAllPropertiesByOwnerId(ownerId);
    }

    // Update Property
    public String updateProperty(int propertyId, String name, String address, double price, Property.Availability availability, Property.Type type, int ownerId) {
        if (!isOwnerOfProperty(propertyId, ownerId)) {
            return "Error: Property not found or unauthorized access.";
        }

        // Update property details
        Property property = propertyDAO.getPropertyById(propertyId);
        property.setName(name);
        property.setAddress(address);
        property.setPrice(price);
        property.setAvailability(availability);
        property.setType(type);

        boolean success = propertyDAO.updateProperty(property);
        return success ? "Property updated successfully." : "Error: Unable to update property.";
    }

    // Delete Property
    public String deleteProperty(int propertyId, int ownerId) {
        if (!isOwnerOfProperty(propertyId, ownerId)) {
            return "Error: Property not found or unauthorized access.";
        }

        boolean success = propertyDAO.deleteProperty(propertyId, ownerId);
        return success ? "Property deleted successfully." : "Error: Unable to delete property.";
    }

    // Create Tenant for a specific Owner with password hashing
    public String createTenant(String name, String email, String contactNumber, String password, int ownerId) {
        // Validate the Owner ID
        if (!isOwnerValid(ownerId)) {
            return "Error: Owner not found.";
        }

        // Hash the password before creating the tenant
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create a new Tenant object with the hashed password
        Tenant tenant = new Tenant(name, email, contactNumber, hashedPassword);
        tenant.setOwnerId(ownerId);

        // Call DAO to insert the tenant into the database
        boolean success = tenantDAO.createTenant(tenant);
        return success ? "Tenant created successfully." : "Error: Unable to create tenant.";
    }

    // Get Tenant by ID
    public Tenant getTenantById(int tenantId) {
        return tenantDAO.getTenantById(tenantId);
    }

    // Delete Tenant
    public String deleteTenant(int tenantId, int ownerId) {
        Tenant tenant = tenantDAO.getTenantById(tenantId);
        if (tenant == null || tenant.getOwnerId() != ownerId) {
            return "Error: Tenant not found or unauthorized access.";
        }

        boolean success = tenantDAO.deleteTenant(tenantId);
        return success ? "Tenant deleted successfully." : "Error: Unable to delete tenant.";
    }
}
