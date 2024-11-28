package com.project.RealEstate.controllers;

import com.project.RealEstate.dao.TenantDAO;
import com.project.RealEstate.models.Tenant;

public class TenantController {

    private final TenantDAO tenantDAO = new TenantDAO();

    // Create Tenant
    public boolean createTenant(Tenant tenant) {
        return tenantDAO.createTenant(tenant);
    }

    // Get Tenant by ID
    public Tenant getTenantById(int tenantId) {
        return tenantDAO.getTenantById(tenantId);
    }

    // Delete Tenant (Not required by Owner)
    public boolean deleteTenant(int tenantId) {
        return tenantDAO.deleteTenant(tenantId);
    }
}
