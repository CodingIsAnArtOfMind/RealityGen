package com.homefinder.realitygen.entity.relationships;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 * COURSE ENTITY - Demonstrates MANY-TO-MANY relationship (INVERSE/NON-OWNING SIDE)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * RELATIONSHIP: Many Courses can have Many Students enrolled
 *
 * This is the INVERSE (non-owning) side of the relationship.
 *
 * KEY DIFFERENCE FROM OWNING SIDE:
 * ════════════════════════════════════════════════════════════════════════════════
 *
 * - Uses mappedBy instead of @JoinTable
 * - Changes on this side ALONE are NOT persisted to the join table
 * - You MUST also update the owning side (Student) for changes to save
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer credits;

    private String instructor;

    /**
     * MANY-TO-MANY RELATIONSHIP - This is the INVERSE (non-owning) side
     *
     * mappedBy = "courses"
     *   → Points to the field name in Student class that has @JoinTable
     *   → This tells JPA: "Student owns this relationship, look there for config"
     *   → NO @JoinTable here because Student already defines it
     *
     * IMPORTANT: Changes on this side ALONE are NOT saved!
     * ════════════════════════════════════════════════════════════════════════════
     *
     * If you only do:
     *   course.getStudents().add(student);
     *   courseRepository.save(course);
     *
     * → The join table is NOT updated!
     * → Because Course is not the owner of the relationship
     *
     * You MUST also do:
     *   student.getCourses().add(course);
     *   studentRepository.save(student);
     *
     * → NOW the join table is updated!
     * → Because Student IS the owner of the relationship
     */
    @ManyToMany(
        mappedBy = "courses",      // Field name in Student class
        fetch = FetchType.LAZY
    )
    private Set<Student> students = new HashSet<>();

    // ═══════════════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Even though this is the inverse side, we provide helper methods
     * that update BOTH sides of the relationship
     */
    public void addStudent(Student student) {
        this.students.add(student);
        student.getCourses().add(this);  // MUST update owning side!
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getCourses().remove(this);  // MUST update owning side!
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════════════════

    public Course() {}

    public Course(String name, Integer credits, String instructor) {
        this.name = name;
        this.credits = credits;
        this.instructor = instructor;
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

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EQUALS AND HASHCODE
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return id != null && id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

