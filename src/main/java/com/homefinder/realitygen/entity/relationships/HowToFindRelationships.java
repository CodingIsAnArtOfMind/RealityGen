package com.homefinder.realitygen.entity.relationships;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
 * ║                     HOW TO FIND RELATIONSHIPS - THE DEVELOPER'S THINKING PROCESS                            ║
 * ╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════╝
 *
 * This guide teaches you HOW developers figure out relationships BEFORE writing code.
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #1: THE "CAN HAVE" SENTENCE TEST
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Write two sentences using "can have" and see which makes sense:
 *
 * Example: Author and Book
 * ────────────────────────
 *   Sentence 1: "One Author CAN HAVE how many Books?"     → MANY books ✓
 *   Sentence 2: "One Book CAN HAVE how many Authors?"     → Usually ONE author ✓
 *
 *   Result: Author (ONE) ←→ Book (MANY) = OneToMany/ManyToOne
 *
 *
 * Example: Student and Course
 * ───────────────────────────
 *   Sentence 1: "One Student CAN HAVE how many Courses?"  → MANY courses ✓
 *   Sentence 2: "One Course CAN HAVE how many Students?"  → MANY students ✓
 *
 *   Result: Both are MANY = ManyToMany
 *
 *
 * Example: User and Profile
 * ─────────────────────────
 *   Sentence 1: "One User CAN HAVE how many Profiles?"    → ONE profile ✓
 *   Sentence 2: "One Profile CAN HAVE how many Users?"    → ONE user ✓
 *
 *   Result: Both are ONE = OneToOne
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #2: THE BOX DIAGRAM (Most developers use this!)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Draw boxes and put numbers:
 *
 *   ┌──────────┐         ┌──────────┐
 *   │  Author  │────────▶│   Book   │
 *   │    1     │         │    *     │       (* means many)
 *   └──────────┘         └──────────┘
 *
 *   Read it: "1 Author has * (many) Books"
 *   The side with "1" gets @OneToMany
 *   The side with "*" gets @ManyToOne
 *
 *
 *   ┌──────────┐         ┌──────────┐
 *   │ Student  │────────▶│  Course  │
 *   │    *     │         │    *     │
 *   └──────────┘         └──────────┘
 *
 *   Read it: "* Students have * Courses"
 *   Both sides have "*" = ManyToMany
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #3: THE "BELONGS TO" vs "HAS" RULE
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * - The entity that "BELONGS TO" another = @ManyToOne (the child)
 * - The entity that "HAS" many others = @OneToMany (the parent)
 *
 * Example:
 * ────────
 *   "A Book BELONGS TO an Author"     → Book has @ManyToOne
 *   "An Author HAS many Books"        → Author has @OneToMany
 *
 *   "An Employee BELONGS TO a Department" → Employee has @ManyToOne
 *   "A Department HAS many Employees"     → Department has @OneToMany
 *
 *   "A Comment BELONGS TO a Post"     → Comment has @ManyToOne
 *   "A Post HAS many Comments"        → Post has @OneToMany
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #4: THE FOREIGN KEY QUESTION
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Ask: "Which table should store the reference (foreign key) to the other?"
 *
 * Rule: The MANY side stores the FK to the ONE side
 *
 * Why? Because it's more efficient:
 *
 *   ❌ BAD: Storing multiple book_ids in Author
 *      author_id | name      | book_ids
 *      1         | Rowling   | [1, 2, 3, 4, 5, 6, 7]  ← Arrays are messy!
 *
 *   ✅ GOOD: Storing one author_id in each Book
 *      book_id | title           | author_id
 *      1       | Harry Potter 1  | 1
 *      2       | Harry Potter 2  | 1
 *      3       | Harry Potter 3  | 1
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #5: REAL-WORLD EXAMPLES CHEAT SHEET
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Common OneToMany / ManyToOne patterns:
 * ──────────────────────────────────────
 *   Parent          Child              Relationship
 *   ──────          ─────              ────────────
 *   Author          Book               1 Author → Many Books
 *   User            Order              1 User → Many Orders
 *   Order           OrderItem          1 Order → Many OrderItems
 *   Post            Comment            1 Post → Many Comments
 *   Category        Product            1 Category → Many Products
 *   Department      Employee           1 Department → Many Employees
 *   Customer        Invoice            1 Customer → Many Invoices
 *   Album           Song               1 Album → Many Songs
 *   Folder          File               1 Folder → Many Files
 *   Team            Player             1 Team → Many Players
 *   Country         City               1 Country → Many Cities
 *   Playlist        Video              1 Playlist → Many Videos
 *   Tenant          User               1 Tenant → Many Users (multi-tenant!)
 *
 *
 * Common ManyToMany patterns:
 * ───────────────────────────
 *   Entity A        Entity B           Why ManyToMany?
 *   ────────        ────────           ───────────────
 *   Student         Course             1 student takes many courses, 1 course has many students
 *   User            Role               1 user has many roles, 1 role has many users
 *   Product         Tag                1 product has many tags, 1 tag on many products
 *   Movie           Actor              1 movie has many actors, 1 actor in many movies
 *   Book            Author             (if co-authors allowed!)
 *   Doctor          Patient            1 doctor has many patients, 1 patient sees many doctors
 *   Article         Category           1 article in many categories, 1 category has many articles
 *   Employee        Project            1 employee on many projects, 1 project has many employees
 *   Recipe          Ingredient         1 recipe has many ingredients, 1 ingredient in many recipes
 *
 *
 * Common OneToOne patterns:
 * ─────────────────────────
 *   Entity A        Entity B           Why OneToOne?
 *   ────────        ────────           ──────────────
 *   User            Profile            1 user has exactly 1 profile
 *   User            Settings           1 user has exactly 1 settings record
 *   Person          Passport           1 person has exactly 1 passport
 *   Employee        ParkingSpot        1 employee assigned 1 parking spot
 *   Order           Payment            1 order has 1 payment (usually)
 *   Country         Capital            1 country has 1 capital city
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #6: THE DECISION FLOWCHART
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 *                          ┌─────────────────────────────────────┐
 *                          │  How many B can one A have?         │
 *                          └─────────────────────────────────────┘
 *                                         │
 *                    ┌────────────────────┼────────────────────┐
 *                    ▼                    ▼                    ▼
 *               ┌─────────┐         ┌──────────┐         ┌─────────┐
 *               │  ONE    │         │   MANY   │         │  ZERO   │
 *               └─────────┘         └──────────┘         └─────────┘
 *                    │                    │                    │
 *                    ▼                    ▼                    ▼
 *          ┌─────────────────┐  ┌─────────────────┐    (No relationship
 *          │ How many A can  │  │ How many A can  │     needed)
 *          │ one B have?     │  │ one B have?     │
 *          └─────────────────┘  └─────────────────┘
 *                 │                      │
 *          ┌──────┴──────┐        ┌──────┴──────┐
 *          ▼             ▼        ▼             ▼
 *       ┌─────┐      ┌──────┐  ┌─────┐      ┌──────┐
 *       │ ONE │      │ MANY │  │ ONE │      │ MANY │
 *       └─────┘      └──────┘  └─────┘      └──────┘
 *          │             │        │             │
 *          ▼             ▼        ▼             ▼
 *     ┌─────────┐   ┌─────────┐ ┌───────────┐ ┌────────────┐
 *     │OneToOne │   │OneToMany│ │ManyToOne  │ │ManyToMany  │
 *     │ A ←→ B  │   │(on A)   │ │(on A)     │ │ A ←→ B     │
 *     └─────────┘   │ManyToOne│ │OneToMany  │ └────────────┘
 *                   │(on B)   │ │(on B)     │
 *                   └─────────┘ └───────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #7: DRAW THE TABLES FIRST
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Before writing Java, sketch the database tables:
 *
 * Step 1: List what you need to store for each entity
 *
 *   Author:              Book:
 *   - id                 - id
 *   - name               - title
 *   - email              - isbn
 *                        - ??? (need to link to author somehow)
 *
 * Step 2: Add the foreign key on the "MANY" side
 *
 *   Author:              Book:
 *   - id (PK)            - id (PK)
 *   - name               - title
 *   - email              - isbn
 *                        - author_id (FK) ← This links to Author!
 *
 * Step 3: Now you know the relationship!
 *   - Book has author_id → Book is the MANY side → @ManyToOne
 *   - Author has no book columns → Author is the ONE side → @OneToMany
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #8: THE QUESTION CHECKLIST
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * When analyzing any relationship, answer these questions:
 *
 * □ Can entity A exist without entity B?
 *   → If no, B might be the parent (OneToMany from B)
 *
 * □ Can entity B exist without entity A?
 *   → If no, A might be the parent (OneToMany from A)
 *
 * □ If I delete A, should B be deleted too?
 *   → If yes, use cascade and orphanRemoval
 *
 * □ Can multiple As reference the same B?
 *   → If yes, it's either ManyToOne or ManyToMany
 *
 * □ Can multiple Bs reference the same A?
 *   → If yes, it's either OneToMany or ManyToMany
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #9: VERBAL PRACTICE EXAMPLES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Practice saying these out loud:
 *
 * Example 1: Blog System
 * ──────────────────────
 *   "One User writes many Posts"           → User @OneToMany Post
 *   "One Post belongs to one User"         → Post @ManyToOne User
 *   "One Post has many Comments"           → Post @OneToMany Comment
 *   "One Comment belongs to one Post"      → Comment @ManyToOne Post
 *   "One Post has many Tags"               → Post @ManyToMany Tag
 *   "One Tag appears on many Posts"        → Tag @ManyToMany Post
 *
 * Example 2: E-commerce
 * ─────────────────────
 *   "One Customer places many Orders"      → Customer @OneToMany Order
 *   "One Order belongs to one Customer"    → Order @ManyToOne Customer
 *   "One Order has many OrderItems"        → Order @OneToMany OrderItem
 *   "One Product appears in many OrderItems" → Product @OneToMany OrderItem
 *   "One OrderItem references one Product" → OrderItem @ManyToOne Product
 *
 * Example 3: School System
 * ────────────────────────
 *   "One Teacher teaches many Classes"     → Teacher @OneToMany Class
 *   "One Class belongs to one Teacher"     → Class @ManyToOne Teacher
 *   "One Student enrolls in many Classes"  → Student @ManyToMany Class
 *   "One Class has many Students"          → Class @ManyToMany Student
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * TRICK #10: THE MULTIPLICITY NOTATION (UML Style)
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Developers often use these symbols:
 *
 *   1      = exactly one
 *   0..1   = zero or one (optional)
 *   *      = zero or more (many)
 *   1..*   = one or more (at least one)
 *   n..m   = between n and m
 *
 * Examples:
 *
 *   Author 1 ────────── * Book
 *   (One author has many books, each book has one author)
 *
 *   User 1 ────────── 0..1 Profile
 *   (One user has zero or one profile)
 *
 *   Student * ────────── * Course
 *   (Many students take many courses)
 *
 *   Order 1 ────────── 1..* OrderItem
 *   (One order has at least one order item)
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * QUICK SUMMARY - THE ULTIMATE CHEAT SHEET
 * ═════════════════════��═════════════════════════════════════════════════════════════════════════════════════════
 *
 * ┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
 * │                                                                                                              │
 * │   1. Ask: "One A has how many Bs?" and "One B has how many As?"                                              │
 * │                                                                                                              │
 * │   2. If A=1 and B=1      → OneToOne                                                                          │
 * │      If A=1 and B=many   → A has @OneToMany, B has @ManyToOne                                                │
 * │      If A=many and B=1   → A has @ManyToOne, B has @OneToMany                                                │
 * │      If A=many and B=many → ManyToMany (choose one side for @JoinTable)                                      │
 * │                                                                                                              │
 * │   3. The "MANY" side always has the foreign key                                                              │
 * │                                                                                                              │
 * │   4. The "MANY" side uses @JoinColumn                                                                        │
 * │                                                                                                              │
 * ���   5. The "ONE" side uses mappedBy                                                                            │
 * │                                                                                                              │
 * └──────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * PRACTICE EXERCISES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * Try to figure out these relationships yourself:
 *
 * 1. Library System:
 *    - Library and Book
 *    - Book and Author (consider co-authors!)
 *    - Member and BorrowedBook
 *
 * 2. Social Media:
 *    - User and Post
 *    - User and User (followers - tricky!)
 *    - Post and Like
 *    - User and Message
 *
 * 3. Hospital:
 *    - Doctor and Patient
 *    - Patient and Appointment
 *    - Doctor and Specialization
 *
 * 4. Your RealityGen Project:
 *    - Tenant and User
 *    - User and Property (listings)
 *    - Property and Image
 *    - User and SavedProperty (favorites)
 *
 * Answers are at the bottom of this file!
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 * ANSWERS TO PRACTICE EXERCISES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * 1. Library System:
 *    ───────────────
 *    Library ←1───*→ Book (OneToMany/ManyToOne)
 *    Book ←*───*→ Author (ManyToMany - because co-authors!)
 *    Member ←1───*→ BorrowedBook ←*───1→ Book (Member has many borrowed records)
 *
 * 2. Social Media:
 *    ──────────────
 *    User ←1───*→ Post (OneToMany/ManyToOne)
 *    User ←*───*→ User as followers (ManyToMany self-referencing!)
 *    Post ←1───*→ Like ←*───1→ User (Like is a join entity)
 *    User ←1───*→ Message (as sender), User ←1───*→ Message (as receiver)
 *
 * 3. Hospital:
 *    ──────────
 *    Doctor ←*───*→ Patient (ManyToMany - one doctor sees many patients, one patient sees many doctors)
 *    Patient ←1───*→ Appointment ←*───1→ Doctor (Appointment links them)
 *    Doctor ←*───*→ Specialization (ManyToMany - a doctor can have multiple specializations)
 *
 * 4. RealityGen:
 *    ───────────
 *    Tenant ←1───*→ User (OneToMany - one tenant has many users)
 *    User ←1───*→ Property (OneToMany - one user lists many properties)
 *    Property ←1───*→ Image (OneToMany - one property has many images)
 *    User ←*───*→ Property as SavedProperty (ManyToMany - users can save many properties)
 *
 */
public class HowToFindRelationships {
    // This is a documentation-only class
    // Use these tricks when designing your entities!
}

