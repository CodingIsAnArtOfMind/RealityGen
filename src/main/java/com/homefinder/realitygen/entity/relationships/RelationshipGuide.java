package com.homefinder.realitygen.entity.relationships;

/**
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *                                    JPA ENTITY RELATIONSHIPS - COMPLETE GUIDE
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * This guide explains how to define entity relationships in Spring Boot/JPA/Hibernate.
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 1: THE GOLDEN RULES - HOW TO DECIDE WHERE TO PUT ANNOTATIONS
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * RULE 1: Read the annotation from the entity you're in
 * ──────────────────────────────────────────────────────
 *   "ONE Author has MANY Books"     → In Author class, use @OneToMany
 *   "MANY Books belong to ONE Author" → In Book class, use @ManyToOne
 *
 * RULE 2: The @ManyToOne side ALWAYS has the foreign key column
 * ──────────────────────────────────────────────────────────────
 *   The entity with @ManyToOne will have a column like "author_id" in its table.
 *   The entity with @OneToMany does NOT have any extra columns.
 *
 * RULE 3: @JoinColumn goes on the OWNING side
 * ────────────────────────────────────────────
 *   - In OneToMany/ManyToOne: The @ManyToOne side is ALWAYS the owner
 *   - In ManyToMany: YOU choose which side is the owner (the one with @JoinTable)
 *
 * RULE 4: mappedBy goes on the NON-OWNING (inverse) side
 * ────────────────────────────────────────────────────────
 *   - mappedBy = "fieldName" points to the field on the other entity
 *   - It means: "I don't own this relationship, go look at that field"
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 2: WHERE DOES "author_id" COME FROM?
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * When you write:
 *
 *   @ManyToOne
 *   @JoinColumn(name = "author_id")
 *   private Author author;
 *
 * The "author_id" is NOT a field you create - JPA creates this column automatically!
 *
 * Here's what happens:
 * ────────────────────
 * 1. JPA sees @JoinColumn(name = "author_id")
 * 2. JPA creates a column called "author_id" in the Book table
 * 3. When you do: book.setAuthor(someAuthor)
 * 4. JPA takes someAuthor.getId() and stores it in the author_id column
 *
 * You NEVER need to create an "authorId" field in your Java class!
 * JPA handles the foreign key column behind the scenes.
 *
 * Example SQL that JPA generates:
 * ────────────────────────────────
 *   CREATE TABLE books (
 *       id BIGINT AUTO_INCREMENT PRIMARY KEY,
 *       title VARCHAR(255),
 *       author_id BIGINT,                         -- JPA created this!
 *       FOREIGN KEY (author_id) REFERENCES authors(id)
 *   );
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 3: @JoinColumn EXPLAINED
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * @JoinColumn is used to customize the foreign key column.
 *
 * Common attributes:
 * ──────────────────
 *
 * name = "author_id"
 *   → The name of the FK column in the database
 *   → Without this, JPA generates a name like "author_id" anyway
 *
 * nullable = false
 *   → The FK column cannot be NULL
 *   → Every Book MUST have an Author
 *
 * referencedColumnName = "id"
 *   → Which column in the referenced table to use
 *   → Default is the primary key, so usually not needed
 *
 * foreignKey = @ForeignKey(name = "fk_book_author")
 *   → Names the FK constraint
 *   → Useful for better database documentation
 *   → Without this, JPA generates a random name
 *
 * insertable = true (default)
 *   → Whether this column is included in INSERT statements
 *
 * updatable = true (default)
 *   → Whether this column is included in UPDATE statements
 *
 * Example:
 * ────────
 *   @ManyToOne
 *   @JoinColumn(
 *       name = "author_id",
 *       nullable = false,
 *       foreignKey = @ForeignKey(name = "fk_book_author")
 *   )
 *   private Author author;
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 4: CASCADE TYPES EXPLAINED
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * Cascade means: "When I do something to this entity, do the same to related entities"
 *
 * CascadeType.PERSIST
 * ────────────────────
 *   When you SAVE the parent, also SAVE the children
 *
 *   Author author = new Author("John");
 *   Book book = new Book("Spring Guide");
 *   author.getBooks().add(book);
 *   authorRepository.save(author);  // Also saves the book!
 *
 * CascadeType.MERGE
 * ─────────────────
 *   When you UPDATE the parent, also UPDATE the children
 *
 *   author.setName("John Updated");
 *   author.getBooks().get(0).setTitle("Updated Title");
 *   authorRepository.save(author);  // Also updates the book!
 *
 * CascadeType.REMOVE
 * ──────────────────
 *   When you DELETE the parent, also DELETE the children
 *
 *   authorRepository.delete(author);  // Also deletes all books!
 *
 * CascadeType.REFRESH
 * ───────────────────
 *   When you REFRESH the parent from DB, also REFRESH the children
 *
 *   entityManager.refresh(author);  // Also refreshes books!
 *
 * CascadeType.DETACH
 * ──────────────────
 *   When you DETACH the parent from session, also DETACH the children
 *
 * CascadeType.ALL
 * ───────────────
 *   All of the above! Use with caution.
 *
 * WHEN TO USE WHAT:
 * ─────────────────
 * - OneToMany (parent-child): Usually CascadeType.ALL + orphanRemoval
 * - ManyToOne: Usually NO cascade (don't delete author when deleting book!)
 * - ManyToMany: Usually PERSIST + MERGE only (NOT REMOVE!)
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 5: ORPHAN REMOVAL EXPLAINED
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * orphanRemoval = true means:
 * "If a child is removed from the parent's collection, DELETE it from the database"
 *
 * Example:
 * ────────
 *   @OneToMany(mappedBy = "author", orphanRemoval = true)
 *   private List<Book> books;
 *
 *   // This will DELETE the book from the database!
 *   author.getBooks().remove(someBook);
 *   authorRepository.save(author);
 *
 * WITHOUT orphanRemoval:
 * ──────────────────────
 *   author.getBooks().remove(someBook);
 *   // The book is NOT deleted
 *   // The book's author_id is just set to NULL (if nullable)
 *
 * DIFFERENCE FROM CascadeType.REMOVE:
 * ────────────────────────────────────
 * - CascadeType.REMOVE: Deletes children when PARENT is deleted
 * - orphanRemoval: Deletes child when it's REMOVED FROM THE COLLECTION
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 6: RELATIONSHIP COMPARISON TABLE
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * ┌───────────────────┬────────────────────────┬────────────────────────┬────────────────────────────────────────┐
 * │ Aspect            │ OneToMany/ManyToOne    │ ManyToMany             │ Notes                                  │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ FK Location       │ In the @ManyToOne side │ In a separate JOIN     │ FK = Foreign Key                       │
 * │                   │ (child table)          │ table                  │                                        │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ Owner Side        │ @ManyToOne side        │ Side with @JoinTable   │ Only owner side changes are persisted  │
 * │                   │ (always)               │ (you choose)           │                                        │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ @JoinColumn       │ On @ManyToOne side     │ Inside @JoinTable      │ Defines the FK column                  │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ mappedBy          │ On @OneToMany side     │ On non-owner side      │ Points to field name in other entity   │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ Default Fetch     │ @ManyToOne: EAGER      │ LAZY                   │ Always prefer LAZY for performance     │
 * │                   │ @OneToMany: LAZY       │                        │                                        │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ Cascade           │ On @OneToMany (parent) │ On owner side          │ Be careful with REMOVE in ManyToMany   │
 * ├───────────────────┼────────────────────────┼────────────────────────┼────────────────────────────────────────┤
 * │ orphanRemoval     │ On @OneToMany only     │ Not typically used     │ Deletes child when removed from list   │
 * └───────────────────┴────────────────────────┴────────────────────────┴────────────────────────────────────────┘
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 7: DATABASE TABLES GENERATED
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * From our example entities, JPA creates these tables:
 *
 * 1. OneToMany/ManyToOne (Author-Book):
 * ────────────────────────────────────
 *
 *   authors                          books
 *   ┌──────────────────┐             ┌──────────────────────────┐
 *   │ id (PK)          │◄────────────│ author_id (FK)           │
 *   │ name             │             │ id (PK)                  │
 *   │ email            │             │ title                    │
 *   └──────────────────┘             │ isbn                     │
 *                                    │ category_id (FK)         │
 *                                    └──────────────────────────┘
 *
 * 2. ManyToMany (Student-Course):
 * ───────────────────────────────
 *
 *   students                    student_course_join              courses
 *   ┌──────────────────┐        ┌─────────────────────┐         ┌──────────────────┐
 *   │ id (PK)          │◄───────│ student_id (FK, PK) │         │ id (PK)          │
 *   │ name             │        │ course_id (FK, PK)  │────────►│ name             │
 *   │ email            │        └─────────────────────┘         │ credits          │
 *   └──────────────────┘                                        │ instructor       │
 *                                                               └──────────────────┘
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 8: QUICK DECISION GUIDE
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * Ask yourself: "How many of A can relate to how many of B?"
 *
 * ┌─────────────────────────────────────────────────────────────────────────────────────────────┐
 * │ Scenario                              │ Use This                │ Example                  │
 * ├─────────────────────────────────────────────────────────────────────────────────────────────┤
 * │ One A has many Bs                     │ @OneToMany on A         │ One Author has many      │
 * │ Each B belongs to one A               │ @ManyToOne on B         │ Books                    │
 * ├─────────────────────────────────────────────────────────────────────────────────────────────┤
 * │ Many As can have many Bs              │ @ManyToMany on both     │ Many Students enroll in  │
 * │ Many Bs can have many As              │ @JoinTable on ONE side  │ many Courses             │
 * ├─────────────────────────────────────────────────────────────────────────────────────────────┤
 * │ One A has exactly one B               │ @OneToOne               │ One User has one         │
 * │ One B belongs to exactly one A        │                         │ Profile                  │
 * └─────────────────────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 * SECTION 9: COMMON MISTAKES TO AVOID
 * ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
 *
 * MISTAKE 1: Putting @JoinColumn on @OneToMany side
 * ──────────────────────────────────────────────────
 * ❌ WRONG:
 *   @OneToMany
 *   @JoinColumn(name = "author_id")  // This creates a different structure!
 *   private List<Book> books;
 *
 * ✅ CORRECT:
 *   @OneToMany(mappedBy = "author")  // Use mappedBy on OneToMany
 *   private List<Book> books;
 *
 * MISTAKE 2: Forgetting to update both sides of bidirectional relationship
 * ─────────────────────────────────────────────────────────────────────────
 * ❌ WRONG:
 *   author.getBooks().add(book);  // Only updated author side
 *
 * ✅ CORRECT:
 *   author.getBooks().add(book);
 *   book.setAuthor(author);  // Update both sides!
 *
 * MISTAKE 3: Using CascadeType.REMOVE in ManyToMany
 * ──────────────────────────────────────────────────
 * ❌ WRONG:
 *   @ManyToMany(cascade = CascadeType.ALL)  // REMOVE will delete related entities!
 *
 * ✅ CORRECT:
 *   @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
 *
 * MISTAKE 4: Only updating the non-owner side in ManyToMany
 * ─────────────────────────────────────────────────────────
 * ❌ WRONG:
 *   course.getStudents().add(student);  // Course is not owner, won't save!
 *
 * ✅ CORRECT:
 *   student.getCourses().add(course);  // Student is owner, will save!
 *   course.getStudents().add(student);  // Keep both sides in sync
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * See the entity files in this package for complete working examples:
 * - Author.java      → OneToMany example (the ONE side)
 * - Book.java        → ManyToOne example (the MANY side)
 * - Category.java    → Another OneToMany example
 * - Student.java     → ManyToMany owner side (has @JoinTable)
 * - Course.java      → ManyToMany inverse side (has mappedBy)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 */
public class RelationshipGuide {
    // This is a documentation-only class
    // See the entity files for actual implementations
}

