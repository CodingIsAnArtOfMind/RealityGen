package com.homefinder.realitygen.entity.relationships;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * CATEGORY ENTITY - Another example of ONE-TO-MANY
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * This is the "ONE" side: One Category has Many Books
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    /**
     * One Category has Many Books
     *
     * mappedBy = "category" → points to the 'category' field in Book class
     *
     * NO CASCADE here because:
     * - If we delete a Category, we don't want to delete all Books
     * - Books should remain with category = NULL
     */
    @OneToMany(
        mappedBy = "category",
        fetch = FetchType.LAZY
        // No cascade - deleting category won't delete books
        // No orphanRemoval - removing book from list won't delete it
    )
    private List<Book> books = new ArrayList<>();

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ═══════════════════════════════════════════════════════════════════════════

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

