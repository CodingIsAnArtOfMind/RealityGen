package com.homefinder.realitygen.service;

import com.homefinder.realitygen.config.MultiTenantLiquibaseService;
import com.homefinder.realitygen.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service to handle tenant provisioning
 */
@Slf4j
@Service
public class TenantProvisioningService {

    private final MultiTenantLiquibaseService liquibaseService;

    public TenantProvisioningService(MultiTenantLiquibaseService liquibaseService) {
        this.liquibaseService = liquibaseService;
    }

    /**
     * Provision a new tenant with its own schema
     *
     * @param tenantId Unique tenant identifier
     * @param tenantName Display name for the tenant
     * @return Created Tenant object
     */
    public Tenant provisionNewTenant(String tenantId, String tenantName) {
        log.info("Provisioning new tenant: {} ({})", tenantName, tenantId);

        // Generate schema name (e.g., tenant_abc123)
        String schemaName = "tenant_" + tenantId.toLowerCase().replaceAll("[^a-z0-9_]", "_");

        // Create tenant object
        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        tenant.setTenantName(tenantName);
        tenant.setSchemaName(schemaName);
        tenant.setActive(true);

        try {
            // Run Liquibase migration to create schema and tables
            liquibaseService.migrateSchema(tenantId, schemaName);

            // TODO: Save tenant to database (public.tenants table)
            // tenantRepository.save(tenant);

            log.info("Successfully provisioned tenant: {} with schema: {}", tenantId, schemaName);

            return tenant;

        } catch (Exception e) {
            log.error("Failed to provision tenant: {}", tenantId, e);
            throw new RuntimeException("Failed to provision tenant: " + tenantId, e);
        }
    }

    /**
     * Update existing tenant schema (run migrations)
     */
    public void updateTenantSchema(String tenantId, String schemaName) {
        log.info("Updating schema for tenant: {}", tenantId);
        liquibaseService.migrateSchema(tenantId, schemaName);
    }

    /**
     * Rollback last change for a tenant
     */
    public void rollbackTenantSchema(String tenantId, String schemaName) {
        log.info("Rolling back schema for tenant: {}", tenantId);
        liquibaseService.rollbackLastChange(tenantId, schemaName);
    }
}

