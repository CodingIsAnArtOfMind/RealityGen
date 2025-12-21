package com.homefinder.realitygen.controller;

import com.homefinder.realitygen.entity.Tenant;
import com.homefinder.realitygen.service.TenantProvisioningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for tenant management
 * DEMO PURPOSES - Add proper security and validation in production
 */
@Slf4j
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantProvisioningService provisioningService;

    public TenantController(TenantProvisioningService provisioningService) {
        this.provisioningService = provisioningService;
    }

    /**
     * Provision a new tenant
     * POST /api/tenants/provision?tenantId=abc123&tenantName=ABC Corp
     */
    @PostMapping("/provision")
    public ResponseEntity<String> provisionTenant(
            @RequestParam String tenantId,
            @RequestParam String tenantName) {

        try {
            Tenant tenant = provisioningService.provisionNewTenant(tenantId, tenantName);

            return ResponseEntity.ok(
                String.format("Tenant '%s' provisioned successfully with schema: %s",
                    tenant.getTenantName(),
                    tenant.getSchemaName())
            );

        } catch (Exception e) {
            log.error("Failed to provision tenant", e);
            return ResponseEntity.badRequest()
                .body("Failed to provision tenant: " + e.getMessage());
        }
    }

    /**
     * Update tenant schema (run migrations)
     * POST /api/tenants/update?tenantId=abc123&schemaName=tenant_abc123
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateTenantSchema(
            @RequestParam String tenantId,
            @RequestParam String schemaName) {

        try {
            provisioningService.updateTenantSchema(tenantId, schemaName);
            return ResponseEntity.ok("Schema updated successfully for tenant: " + tenantId);

        } catch (Exception e) {
            log.error("Failed to update tenant schema", e);
            return ResponseEntity.badRequest()
                .body("Failed to update schema: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Multi-Tenant Liquibase Service is running!");
    }
}

