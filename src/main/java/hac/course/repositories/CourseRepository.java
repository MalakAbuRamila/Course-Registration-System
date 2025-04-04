package hac.course.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides methods for performing operations on Course entities.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    /**
     * Finds a course by its id.
     * @param id The id of the course
     * @return The course with the specified id.
     */
    Course findById(long id);

    /**
     * Finds a course by its name.
     * @param name The name of the course.
     * @return The course with the specified name.
     */
    Course findByName(String name);
}
