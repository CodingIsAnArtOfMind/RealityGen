package com.homefinder.realitygen.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Multi-Tenant Liquibase Service
 * Handles database schema migrations for each tenant
 */
@Slf4j
@Service
public class MultiTenantLiquibaseService {

    @Value("${spring.liquibase.change-log}")
    private String changeLogPath;

    private final DataSource dataSource;

    public MultiTenantLiquibaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Run Liquibase migration for a specific tenant schema
     *
     * @param tenantId The tenant identifier
     * @param schemaName The schema name for this tenant
     */
    public void migrateSchema(String tenantId, String schemaName) {
        log.info("Starting Liquibase migration for tenant: {} in schema: {}", tenantId, schemaName);

        try (Connection connection = dataSource.getConnection()) {

            // Create schema if it doesn't exist
            createSchemaIfNotExists(connection, schemaName);

            // Set search path to tenant schema
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET search_path TO " + schemaName);
            }

            // Create Liquibase database object
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(schemaName);
            database.setLiquibaseSchemaName(schemaName);

            // Run Liquibase migration
            Liquibase liquibase = new Liquibase(
                    changeLogPath.replace("classpath:", ""),
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(new Contexts(), new LabelExpression());

            log.info("Successfully completed Liquibase migration for tenant: {} in schema: {}",
                    tenantId, schemaName);

        } catch (Exception e) {
            log.error("Failed to run Liquibase migration for tenant: {} in schema: {}",
                    tenantId, schemaName, e);
            throw new RuntimeException("Liquibase migration failed for tenant: " + tenantId, e);
        }
    }

    /**
     * Create schema if it doesn't exist
     */
    private void createSchemaIfNotExists(Connection connection, String schemaName) throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            log.info("Schema '{}' created or already exists", schemaName);
        }
    }

    /**
     * Rollback last changeset for a tenant
     */
    public void rollbackLastChange(String tenantId, String schemaName) {
        log.info("Rolling back last changeset for tenant: {} in schema: {}", tenantId, schemaName);

        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SET search_path TO " + schemaName);
            }

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(schemaName);
            database.setLiquibaseSchemaName(schemaName);

            Liquibase liquibase = new Liquibase(
                    changeLogPath.replace("classpath:", ""),
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.rollback(1, new Contexts(), new LabelExpression());

            log.info("Successfully rolled back last changeset for tenant: {}", tenantId);

        } catch (Exception e) {
            log.error("Failed to rollback for tenant: {} in schema: {}", tenantId, schemaName, e);
            throw new RuntimeException("Liquibase rollback failed for tenant: " + tenantId, e);
        }
    }
}

