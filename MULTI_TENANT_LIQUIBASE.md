# Multi-Tenant Liquibase Setup

This project is configured for **multi-tenant architecture** with separate database schemas per tenant.

## Architecture

- **Master Schema (public)**: Stores tenant metadata
- **Tenant Schemas**: Each tenant has their own schema (e.g., `tenant_abc123`)
- **Liquibase**: Runs programmatically to create/update tenant schemas

## How It Works

### 1. Dependencies

```xml
<!-- Spring Boot Starter (auto-configuration disabled) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-liquibase</artifactId>
</dependency>

<!-- Liquibase Core (for programmatic control) -->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

### 2. Configuration

Liquibase auto-configuration is **disabled** in `application.properties`:

```properties
spring.liquibase.enabled=false
```

This prevents Liquibase from running automatically on startup. Instead, we control when and where it runs.

### 3. Tenant Provisioning

When a new tenant is created:

1. **Generate schema name**: `tenant_<tenantId>`
2. **Create schema**: `CREATE SCHEMA IF NOT EXISTS tenant_abc123`
3. **Run Liquibase**: Execute changesets in the tenant schema
4. **Save tenant metadata**: Store in `public.tenants` table

## Usage Examples

### Provision a New Tenant

```bash
curl -X POST "http://localhost:8080/api/tenants/provision?tenantId=abc123&tenantName=ABC%20Corp"
```

This will:
- Create schema `tenant_abc123`
- Run all Liquibase changesets
- Create `users` table in the tenant schema

### Update Tenant Schema (Run Migrations)

```bash
curl -X POST "http://localhost:8080/api/tenants/update?tenantId=abc123&schemaName=tenant_abc123"
```

### Programmatic Usage

```java
@Autowired
private TenantProvisioningService provisioningService;

// Provision new tenant
Tenant tenant = provisioningService.provisionNewTenant("xyz789", "XYZ Company");

// Update existing tenant schema
provisioningService.updateTenantSchema("xyz789", "tenant_xyz789");

// Rollback last change
provisioningService.rollbackTenantSchema("xyz789", "tenant_xyz789");
```

## Key Classes

### MultiTenantLiquibaseService
Core service that runs Liquibase migrations for specific schemas.

```java
@Service
public class MultiTenantLiquibaseService {
    public void migrateSchema(String tenantId, String schemaName);
    public void rollbackLastChange(String tenantId, String schemaName);
}
```

### TenantProvisioningService
Business logic for tenant provisioning.

```java
@Service
public class TenantProvisioningService {
    public Tenant provisionNewTenant(String tenantId, String tenantName);
    public void updateTenantSchema(String tenantId, String schemaName);
}
```

### TenantController
REST API for tenant management (demo purposes).

## Database Structure

### Master Schema (public)
```sql
public.tenants
├── id
├── tenant_id (unique)
├── tenant_name
├── schema_name (unique)
├── active
└── description
```

### Tenant Schema (tenant_*)
```sql
tenant_abc123.users
├── id
├── name
├── email
├── password

tenant_abc123.databasechangelog (Liquibase tracking)
tenant_abc123.databasechangeloglock (Liquibase locking)
```

## Changelog Files

All tenant schemas share the same changelog:
- `db/changelog/app-changelog.xml` (master file)
- `db/changelog/scripts/create_user_table.sql` (user table)

Add new changesets here, and they'll be applied to all tenants when you run migrations.

## Why Both Dependencies?

**spring-boot-starter-liquibase**: 
- Provides Spring integration classes
- Includes SpringLiquibase bean configuration
- Dependency management

**liquibase-core**: 
- Programmatic API access
- Manual Liquibase runner
- Multi-schema support

## Production Considerations

1. **Security**: Add authentication/authorization to tenant endpoints
2. **Validation**: Validate tenant IDs and schema names
3. **Error Handling**: Implement rollback mechanisms
4. **Monitoring**: Log all schema operations
5. **Performance**: Consider connection pooling per tenant
6. **Isolation**: Ensure proper schema isolation
7. **Backup**: Implement per-tenant backup strategies

## Testing

Test the setup:
```bash
# Start application
./mvnw spring-boot:run

# Provision tenant 1
curl -X POST "http://localhost:8080/api/tenants/provision?tenantId=tenant1&tenantName=Tenant%20One"

# Provision tenant 2
curl -X POST "http://localhost:8080/api/tenants/provision?tenantId=tenant2&tenantName=Tenant%20Two"

# Check schemas in PostgreSQL
psql -U postgres -d wm_multitenent_housing_db -c "\dn"
psql -U postgres -d wm_multitenent_housing_db -c "\dt tenant_tenant1.*"
psql -U postgres -d wm_multitenent_housing_db -c "\dt tenant_tenant2.*"
```

## Next Steps

1. Implement Tenant Repository for persistence
2. Add Tenant Context/Resolver for routing requests
3. Implement DataSource routing per tenant
4. Add Hibernate multi-tenancy configuration
5. Implement tenant-aware authentication

---

**Note**: This is a foundation for multi-tenant architecture. You'll need to add tenant resolution, context management, and data source routing for a complete solution.

