# JPA Entity Relationships - Complete Guide

I've created comprehensive example entities in your project at:
`src/main/java/com/homefinder/realitygen/entity/relationships/`

## Files Created:

| File | Purpose |
|------|---------|
| `Author.java` | **OneToMany** example (the ONE side) |
| `Book.java` | **ManyToOne** example (the MANY side) |
| `Category.java` | Another OneToMany example |
| `Student.java` | **ManyToMany** owner side (has @JoinTable) |
| `Course.java` | **ManyToMany** inverse side (has mappedBy) |
| `RelationshipGuide.java` | Complete documentation with all concepts |
| `RelationshipUsageExamples.java` | Practical code examples |

---

## Quick Answer to Your Questions:

### 1. Where to put @OneToMany vs @ManyToOne?
```
Read from the entity you're in:
- "ONE Author has MANY Books" → @OneToMany in Author
- "MANY Books belong to ONE Author" → @ManyToOne in Book
```

### 2. Where does `author_id` come from?
```java
@ManyToOne
@JoinColumn(name = "author_id")  // JPA CREATES this column automatically!
private Author author;
```
**You don't create it!** JPA creates the `author_id` column in the database and manages it for you.

### 3. Which side gets @JoinColumn?
```
- In OneToMany/ManyToOne: The @ManyToOne side (ALWAYS)
- In ManyToMany: The owner side (inside @JoinTable)
```

### 4. What is mappedBy?
```java
// In Author (the ONE side):
@OneToMany(mappedBy = "author")  // Points to field name in Book class
private List<Book> books;
```
It means: "I don't own this relationship - look at the 'author' field in Book"

### 5. Cascade Types:
| Type | What it does |
|------|--------------|
| `PERSIST` | Save parent → also saves children |
| `MERGE` | Update parent → also updates children |
| `REMOVE` | Delete parent → also deletes children |
| `ALL` | All of the above |

### 6. OrphanRemoval:
```java
@OneToMany(orphanRemoval = true)
```
If you do `author.getBooks().remove(book)` → that book is **DELETED** from database

---

## Database Structure Generated:

### OneToMany/ManyToOne:
```
┌──────────────────┐         ┌──────────────────────┐
│    authors       │         │       books          │
├──────────────────┤         ├──────────────────────┤
│ id (PK)          │◄────────│ author_id (FK)       │
│ name             │         │ id (PK)              │
│ email            │         │ title                │
└──────────────────┘         └──────────────────────┘

The FK is always on the @ManyToOne side (Book has author_id)
```

### ManyToMany:
```
┌─────────────┐    ┌─────────────────────┐    ┌─────────────┐
│  students   │    │ student_course_join │    │  courses    │
├─────────────┤    ├─────────────────────┤    ├─────────────┤
│ id (PK)     │◄───│ student_id (FK)     │    │ id (PK)     │
│ name        │    │ course_id (FK)      │───►│ name        │
└─────────────┘    └─────────────────────┘    └─────────────┘

A separate JOIN TABLE is created for ManyToMany
```

---

## The Golden Rules:

1. **@ManyToOne** side = has the foreign key column = is the OWNER
2. **@OneToMany** side = uses `mappedBy` = is NOT the owner
3. **@JoinColumn** goes on the OWNER side
4. **mappedBy** goes on the NON-OWNER side
5. In ManyToMany, YOU choose which side is the owner (the one with @JoinTable)
6. Only changes on the OWNER side are persisted to the database!

---

Check the Java files for detailed comments explaining every annotation!

