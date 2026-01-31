package com.homefinder.realitygen.entity.relationships;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *                              PRACTICAL USAGE EXAMPLES
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 *
 * This class shows how to actually USE the entity relationships in your service layer.
 *
 * NOTE: This is example code showing patterns. You would need to create the actual
 * repository interfaces to run this code.
 *
 * ═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════
 */
@Service
public class RelationshipUsageExamples {

    // These would be your Spring Data JPA repositories
    // @Autowired private AuthorRepository authorRepository;
    // @Autowired private BookRepository bookRepository;
    // @Autowired private StudentRepository studentRepository;
    // @Autowired private CourseRepository courseRepository;

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 1: Creating entities with OneToMany/ManyToOne relationship
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void createAuthorWithBooks() {

        // Create an Author
        Author author = new Author("J.K. Rowling", "jk@example.com");

        // Create Books
        Book book1 = new Book("Harry Potter 1", "ISBN-001");
        Book book2 = new Book("Harry Potter 2", "ISBN-002");

        // METHOD 1: Using the helper method (RECOMMENDED)
        // ────────────────────────────────────────────────
        author.addBook(book1);  // This updates BOTH sides of the relationship
        author.addBook(book2);

        // METHOD 2: Manual way (you have to remember to set both sides!)
        // ──────────────────────────────────────────────────────────────
        // author.getBooks().add(book1);
        // book1.setAuthor(author);  // DON'T FORGET THIS!

        // Save only the Author - Books are saved automatically due to CascadeType.PERSIST
        // authorRepository.save(author);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // INSERT INTO authors (name, email) VALUES ('J.K. Rowling', 'jk@example.com');
        // INSERT INTO books (title, isbn, author_id) VALUES ('Harry Potter 1', 'ISBN-001', 1);
        // INSERT INTO books (title, isbn, author_id) VALUES ('Harry Potter 2', 'ISBN-002', 1);
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 2: Adding a Book to an existing Author
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void addBookToExistingAuthor(Long authorId) {

        // Find the existing author
        // Author author = authorRepository.findById(authorId).orElseThrow();

        // Create new book
        Book newBook = new Book("Harry Potter 3", "ISBN-003");

        // Add to author using helper method
        // author.addBook(newBook);

        // Save - the new book is persisted due to cascade
        // authorRepository.save(author);
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 3: Removing a Book (with orphanRemoval)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void removeBookFromAuthor(Long authorId, Long bookId) {

        // Find the author
        // Author author = authorRepository.findById(authorId).orElseThrow();

        // Find the book in the author's collection
        // Book bookToRemove = author.getBooks().stream()
        //     .filter(b -> b.getId().equals(bookId))
        //     .findFirst()
        //     .orElseThrow();

        // Remove using helper method
        // author.removeBook(bookToRemove);

        // Save - the book is DELETED from database due to orphanRemoval = true
        // authorRepository.save(author);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // DELETE FROM books WHERE id = ?;  (orphanRemoval kicks in!)
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 4: ManyToMany - Enrolling a Student in Courses
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void enrollStudentInCourses() {

        // Create a student
        Student student = new Student("John Doe", "john@university.edu");

        // Create courses
        Course math = new Course("Mathematics", 3, "Dr. Smith");
        Course physics = new Course("Physics", 4, "Dr. Johnson");

        // Enroll student in courses using helper method
        student.enrollInCourse(math);    // Updates BOTH sides
        student.enrollInCourse(physics); // Updates BOTH sides

        // Save student - courses are also saved due to CascadeType.PERSIST
        // studentRepository.save(student);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // INSERT INTO students (name, email) VALUES ('John Doe', 'john@university.edu');
        // INSERT INTO courses (name, credits, instructor) VALUES ('Mathematics', 3, 'Dr. Smith');
        // INSERT INTO courses (name, credits, instructor) VALUES ('Physics', 4, 'Dr. Johnson');
        // INSERT INTO student_course_join (student_id, course_id) VALUES (1, 1);
        // INSERT INTO student_course_join (student_id, course_id) VALUES (1, 2);
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 5: ManyToMany - Enrolling in an EXISTING Course
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void enrollInExistingCourse(Long studentId, Long courseId) {

        // Find existing student and course
        // Student student = studentRepository.findById(studentId).orElseThrow();
        // Course course = courseRepository.findById(courseId).orElseThrow();

        // Enroll using helper method
        // student.enrollInCourse(course);

        // IMPORTANT: Save from the OWNING side (Student)
        // studentRepository.save(student);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // INSERT INTO student_course_join (student_id, course_id) VALUES (?, ?);
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 6: ManyToMany - Dropping a Course
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void dropCourse(Long studentId, Long courseId) {

        // Find existing student and course
        // Student student = studentRepository.findById(studentId).orElseThrow();
        // Course course = courseRepository.findById(courseId).orElseThrow();

        // Drop the course
        // student.dropCourse(course);

        // Save from the OWNING side
        // studentRepository.save(student);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // DELETE FROM student_course_join WHERE student_id = ? AND course_id = ?;
        //
        // Note: The Course itself is NOT deleted! Only the relationship is removed.
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 7: What happens if you only update the NON-OWNING side?
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void wrongWayToAddStudent() {

        // Student student = studentRepository.findById(1L).orElseThrow();
        // Course course = courseRepository.findById(1L).orElseThrow();

        // WRONG! Only updating the non-owning side
        // course.getStudents().add(student);
        // courseRepository.save(course);

        // Result: NOTHING is saved to the join table!
        // Because Course is not the owner of the relationship.

        // The CORRECT way:
        // student.getCourses().add(course);  // Update the OWNING side
        // course.getStudents().add(student); // Keep both sides in sync
        // studentRepository.save(student);   // Save from OWNING side
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 8: Deleting an Author (with cascade)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void deleteAuthorWithBooks(Long authorId) {

        // Find the author
        // Author author = authorRepository.findById(authorId).orElseThrow();

        // Delete - all books are deleted too due to CascadeType.REMOVE
        // authorRepository.delete(author);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // DELETE FROM books WHERE author_id = ?;  (Cascade deletes books first)
        // DELETE FROM authors WHERE id = ?;       (Then deletes author)
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     * EXAMPLE 9: Deleting a Student (ManyToMany - no cascade on REMOVE)
     * ═══════════════════════════════════════════════════════════════════════════════════════════
     */
    @Transactional
    public void deleteStudent(Long studentId) {

        // Student student = studentRepository.findById(studentId).orElseThrow();

        // First, remove student from all courses (clean up join table)
        // for (Course course : new HashSet<>(student.getCourses())) {
        //     student.dropCourse(course);
        // }

        // Now delete the student
        // studentRepository.delete(student);

        // What SQL does JPA generate?
        // ─────────────────────────────
        // DELETE FROM student_course_join WHERE student_id = ?;  (Clean up join table)
        // DELETE FROM students WHERE id = ?;                     (Delete student)
        //
        // The courses are NOT deleted! They still exist for other students.
    }
}

