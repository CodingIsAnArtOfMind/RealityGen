package com.homefinder.realitygen.entity.relationships;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * AUTHOR ENTITY - Demonstrates ONE-TO-MANY relationship
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * RELATIONSHIP: One Author can have MANY Books
 *
 * KEY CONCEPT: The "ONE" side of OneToMany
 * - This is the PARENT/OWNER in terms of business logic
 * - But in JPA, the CHILD (Book) actually owns the relationship in the database
 * - The foreign key (author_id) is stored in the BOOK table, not here
 *
 * WHY @OneToMany goes here:
 * - Because ONE Author → MANY Books
 * - Read it as: "This ONE entity has a relationship to MANY of that entity"
 *
 * MAPPEDBY EXPLAINED:
 * - "mappedBy = author" means: "Go look at the 'author' field in Book class"
 * - This tells JPA: "I don't own this relationship, Book does"
 * - The Book class has a field called 'author' with @ManyToOne
 *
 * CASCADE TYPES EXPLAINED:
 * - CascadeType.ALL: All operations (persist, merge, remove, refresh, detach) cascade
 * - CascadeType.PERSIST: When you save Author, save all Books too
 * - CascadeType.MERGE: When you update Author, update all Books too
 * - CascadeType.REMOVE: When you delete Author, delete all Books too
 * - CascadeType.REFRESH: When you refresh Author, refresh all Books too
 * - CascadeType.DETACH: When you detach Author, detach all Books too
 *
 * ORPHAN REMOVAL EXPLAINED:
 * - orphanRemoval = true means: If a Book is removed from this list, DELETE it from DB
 * - Example: author.getBooks().remove(book) → That book gets DELETED
 * - Without orphanRemoval: The book would just have author_id set to NULL
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generates ID (1, 2, 3...)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    /**
     * ONE-TO-MANY RELATIONSHIP
     *
     * @OneToMany = One Author has Many Books
     *
     * mappedBy = "author"
     *   → Points to the field name in Book class that has @ManyToOne
     *   → This means Book table will have the foreign key column (author_id)
     *   → Author table does NOT have any book-related columns
     *
     * cascade = CascadeType.ALL
     *   → All operations on Author will cascade to Books
     *   → Save author → also saves all books in the list
     *   → Delete author → also deletes all books in the list
     *
     * orphanRemoval = true
     *   → If you do: author.getBooks().remove(someBook)
     *   → That book will be DELETED from the database
     *   → Without this, the book would just have author_id = NULL
     *
     * fetch = FetchType.LAZY (default for OneToMany)
     *   → Books are NOT loaded when you load Author
     *   → Books are loaded only when you call author.getBooks()
     *   → This is good for performance
     */
    @OneToMany(
        mappedBy = "author",           // Field name in Book class
        cascade = CascadeType.ALL,     // All operations cascade
        orphanRemoval = true,          // Remove book from list = delete from DB
        fetch = FetchType.LAZY         // Load books only when needed
    )
    private List<Book> books = new ArrayList<>();

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHODS - Important for bidirectional relationships!
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Always use helper methods to maintain both sides of the relationship!
     * This ensures the in-memory model is consistent.
     */
    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);  // Set the other side too!
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);  // Clear the other side too!
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════

    public Author() {}

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

