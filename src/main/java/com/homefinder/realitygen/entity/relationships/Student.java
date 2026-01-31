package com.homefinder.realitygen.entity.relationships;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * STUDENT ENTITY - Demonstrates MANY-TO-MANY relationship (OWNING SIDE)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * RELATIONSHIP: Many Students can enroll in Many Courses
 *
 * MANY-TO-MANY CREATES A JOIN TABLE:
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * Unlike OneToMany/ManyToOne, ManyToMany needs a THIRD table (join table)
 * to store the relationships.
 *
 * DATABASE STRUCTURE:
 *
 * ┌──────────────────┐     ┌────────────────────────┐     ┌──────────────────┐
 * │    students      │     │  student_course_join   │     │     courses      │
 * ├──────────────────┤     ├────────────────────────┤     ├──────────────────┤
 * │ id (PK)          │◄────│ student_id (FK, PK)    │     │ id (PK)          │
 * │ name             │     │ course_id (FK, PK)     │────►│ name             │
 * │ email            │     └────────────────────────┘     │ credits          │
 * └──────────────────┘                                    └──────────────────┘
 *
 * The JOIN TABLE has:
 * - student_id → foreign key to students table
 * - course_id → foreign key to courses table
 * - Together they form a composite primary key
 *
 * WHICH SIDE IS THE OWNER?
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * In ManyToMany, you CHOOSE which side owns the relationship:
 * - The OWNER side has @JoinTable annotation
 * - The INVERSE side has mappedBy
 * - Only the OWNER side's changes are persisted to the join table!
 *
 * Here, Student is the OWNER (has @JoinTable)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    /**
     * MANY-TO-MANY RELATIONSHIP - This is the OWNING side
     *
     * @ManyToMany = Many Students can enroll in Many Courses
     *
     * @JoinTable EXPLAINED IN DETAIL:
     * ════════════════════════════════════════════════════════════════════════════
     *
     * name = "student_course_join"
     *   → The NAME of the join table that will be created
     *   → This table stores the relationships (student_id, course_id pairs)
     *
     * joinColumns = @JoinColumn(name = "student_id")
     *   → Column in join table that references THIS entity (Student)
     *   → "student_id" will hold Student's primary key
     *   → WHERE DOES student_id COME FROM? JPA creates it automatically!
     *   → It will store the ID from the Student entity
     *
     * inverseJoinColumns = @JoinColumn(name = "course_id")
     *   → Column in join table that references THE OTHER entity (Course)
     *   → "course_id" will hold Course's primary key
     *   → WHERE DOES course_id COME FROM? JPA creates it automatically!
     *   → It will store the ID from the Course entity
     *
     * foreignKey and inverseForeignKey
     *   → Names for the foreign key constraints
     *   → Just for better database documentation
     *
     * GENERATED JOIN TABLE:
     * ┌─────────────────────────────────────────┐
     * │        student_course_join              │
     * ├─────────────────────────────────────────┤
     * │ student_id (FK → students.id)           │
     * │ course_id (FK → courses.id)             │
     * │ PRIMARY KEY (student_id, course_id)     │
     * └─────────────────────────────────────────┘
     *
     * CASCADE IN MANY-TO-MANY:
     * ════════════════════════════════════════════════════════════════════════════
     *
     * BE CAREFUL with cascade in ManyToMany!
     *
     * - CascadeType.PERSIST: When you save Student, also save new Courses
     * - CascadeType.MERGE: When you update Student, also update Courses
     * - CascadeType.REMOVE: DANGEROUS! Deleting Student deletes Courses!
     *                       Other students might still need those courses!
     *
     * Usually, you DON'T want CascadeType.ALL or CascadeType.REMOVE in ManyToMany
     *
     * Here we use PERSIST and MERGE, but NOT REMOVE
     */
    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE}  // NOT REMOVE!
    )
    @JoinTable(
        name = "student_course_join",                        // Join table name

        joinColumns = @JoinColumn(
            name = "student_id",                             // Column for this entity
            foreignKey = @ForeignKey(name = "fk_enrollment_student")
        ),

        inverseJoinColumns = @JoinColumn(
            name = "course_id",                              // Column for other entity
            foreignKey = @ForeignKey(name = "fk_enrollment_course")
        )
    )
    private Set<Course> courses = new HashSet<>();  // Use Set to avoid duplicates

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHODS - CRITICAL for ManyToMany!
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * IMPORTANT: Only changes on the OWNING side are persisted!
     *
     * If you only do: course.getStudents().add(student)
     * → This WON'T save to database because Course is not the owner!
     *
     * You MUST also do: student.getCourses().add(course)
     * → This WILL save to database because Student is the owner!
     *
     * Best practice: Always update both sides
     */
    public void enrollInCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);  // Keep both sides in sync
    }

    public void dropCourse(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this);  // Keep both sides in sync
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════

    public Student() {}

    public Student(String name, String email) {
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

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EQUALS AND HASHCODE - Important for Sets in ManyToMany!
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id != null && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();  // Constant hashcode for JPA entities
    }
}

