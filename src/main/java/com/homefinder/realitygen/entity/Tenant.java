package com.homefinder.realitygen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tenant entity to store tenant information
 * This will be stored in a master/public schema
 */
@Entity
@Table(name = "tenants", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String tenantId;

    @Column(nullable = false, length = 100)
    private String tenantName;

    @Column(nullable = false, unique = true, length = 50)
    private String schemaName;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(length = 500)
    private String description;
}

