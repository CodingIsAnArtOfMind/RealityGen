package com.homefinder.realitygen.entity.relationships;

import jakarta.persistence.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * BOOK ENTITY - Demonstrates MANY-TO-ONE relationship
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * RELATIONSHIP: Many Books belong to ONE Author
 *
 * KEY CONCEPT: The "MANY" side of ManyToOne - THIS IS THE OWNER SIDE!
 * - This is the CHILD in terms of business logic
 * - But in JPA, this is the OWNER of the relationship
 * - The foreign key (author_id) is stored HERE in the BOOK table
 *
 * WHY @ManyToOne goes here:
 * - Because MANY Books → ONE Author
 * - Read it as: "MANY of this entity belong to ONE of that entity"
 *
 * DATABASE STRUCTURE:
 * ┌─────────────────────────┐         ┌─────────────────────────┐
 * │      authors table      │         │       books table       │
 * ├─────────────────────────┤         ├─────────────────────────┤
 * │ id (PK)                 │◄────────│ author_id (FK)          │
 * │ name                    │         │ id (PK)                 │
 * │ email                   │         │ title                   │
 * └─────────────────────────┘         │ isbn                    │
 *                                     │ category_id (FK)        │
 *                                     └─────────────────────────┘
 *
 * The FOREIGN KEY is always on the MANY side (Book has author_id)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true)
    private String isbn;

    /**
     * MANY-TO-ONE RELATIONSHIP - The OWNING side
     *
     * @ManyToOne = Many Books belong to One Author
     *
     * @JoinColumn EXPLAINED:
     * ════════════════════════════════════════════════════════════════════
     *
     * name = "author_id"
     *   → This creates a column called "author_id" in the BOOKS table
     *   → This column stores the ID of the Author
     *   → WHERE DOES author_id COME FROM?
     *     - JPA CREATES this column automatically!
     *     - It will store the PRIMARY KEY (id) of the Author entity
     *     - When you do book.setAuthor(author), JPA puts author.getId() here
     *
     * nullable = false
     *   → Every book MUST have an author (author_id cannot be NULL)
     *   → If you try to save a Book without Author, you get an error
     *
     * foreignKey = @ForeignKey(name = "fk_book_author")
     *   → Names the foreign key constraint in the database
     *   → This is for better database documentation
     *   → Without this, JPA generates a random constraint name
     *
     * referencedColumnName = "id" (optional - defaults to primary key)
     *   → Specifies which column in Author table this FK points to
     *   → Usually not needed because it defaults to the primary key
     *
     * FETCH TYPES:
     * ════════════════════════════════════════════════════════════════════
     *
     * FetchType.EAGER (default for ManyToOne)
     *   → Author is loaded immediately when you load Book
     *   → One query loads both: SELECT * FROM books JOIN authors
     *
     * FetchType.LAZY
     *   → Author is NOT loaded when you load Book
     *   → Author is loaded only when you call book.getAuthor()
     *   → Better for performance if you don't always need the Author
     *
     * OPTIONAL:
     * ════════════════════════════════════════════════════════════════════
     *
     * optional = false
     *   → Same as nullable = false but for JPA layer
     *   → Tells JPA this relationship is required
     *   → Allows JPA to optimize queries (INNER JOIN instead of LEFT JOIN)
     */
    @ManyToOne(
        fetch = FetchType.LAZY,    // Don't load author until needed
        optional = false           // Author is required
    )
    @JoinColumn(
        name = "author_id",                              // Column name in books table
        nullable = false,                                // Can't be null in DB
        foreignKey = @ForeignKey(name = "fk_book_author") // FK constraint name
    )
    private Author author;

    /**
     * Another ManyToOne example - Book belongs to a Category
     * A Book can only be in ONE Category, but a Category has MANY Books
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "category_id",                               // Column name in books table
        foreignKey = @ForeignKey(name = "fk_book_category") // FK constraint name
    )
    private Category category;  // This can be null (optional relationship)

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════

    public Book() {}

    public Book(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    public Book(String title, String isbn, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

