package hac.course.services;


import hac.course.repositories.Course;
import hac.course.repositories.CourseRepository;
import hac.course.repositories.User;
import hac.course.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Provides logic related to courses.
 */
@Service
public class CourseService {
    /**
     * Repository for handling course-related operations
     */
    @Autowired
    private CourseRepository courseRepository;
    /**
     * Repository for handling user-related operations
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Retrieves a list of all courses.
     * @return A list of all courses.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Saves a new course. Ensures that no course with the same name exists.
     *
     * @param course The course to be saved.
     */
    @Transactional
    public synchronized void saveCourse(Course course) {
        //check if a course with the same name already exists
        Course existingCourse = courseRepository.findByName(course.getName());
        if (existingCourse != null) {
            //course with the same name already exists error
            throw new IllegalArgumentException("The name of the course '" + course.getName() + "' already exists.");
        }

        courseRepository.save(course);
    }

    /**
     * Deletes a course by its id. Removes all participants from the course before deletion.
     * @param id The id of the course to be deleted.
     */
    @Transactional
    public synchronized void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

        //remove all participants from the course and delete users who are only registered for this course
        for (User participant : course.getParticipants()) {
            participant.getCourses().remove(course);
            if (participant.getCourses().isEmpty()) {
                userRepository.delete(participant);
            } else {
                userRepository.save(participant);
            }
        }

        course.getParticipants().clear();
        courseRepository.save(course);

        courseRepository.deleteById(id);
    }

    /**
     *  Retrieves a course by its id.
     * @param id The id of the course.
     * @return The course with the specified id.
     */
    public Course getCourseById(long id) {
        return courseRepository.findById(id);
    }


    /**
     *  Checks if a course is full based on its maximum number of participants.
     * @param id The id of the course.
     * @return true if the course is full, otherwise false.
     */
    public boolean isCourseFull(Long id) {
        Course course = getCourseById(id);
        return course.getParticipants().size() >= course.getMaxParticipants();
    }

    /**
     * Updates an existing course
     * @param existingCourse The course to be updated.
     * @param updatedCourse The new details for the course.
     */
    @Transactional
    public synchronized void updateCourse(Course existingCourse, Course updatedCourse) {
        if (!existingCourse.getName().equals(updatedCourse.getName())) {

            //check if a course with the new name already exists
            Course courseWithName = courseRepository.findByName(updatedCourse.getName());
            if (courseWithName != null) {
                throw new IllegalArgumentException("The name of the course '" + updatedCourse.getName() + "' already exists.");
            }
        }

        //check if the new maxParticipants is less than the current number of participants
        if (updatedCourse.getMaxParticipants() < existingCourse.getParticipants().size()) {
            throw new IllegalArgumentException("The maximum number of participants cannot be less than the current number of participants.");
        }

        existingCourse.setName(updatedCourse.getName());
        existingCourse.setMaxParticipants(updatedCourse.getMaxParticipants());
        courseRepository.save(existingCourse);
    }

    /**
     * Searches for courses by keyword.
     * @param keyword The search keyword.
     * @return A list of courses that match the keyword.
     */
    public List<Course> searchCourses(String keyword) {
        List<Course> allCourses = courseRepository.findAll();

        //filter courses that match the keyword
        List<Course> filteredCourses = allCourses.stream()
                .filter(course -> course.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return filteredCourses;
    }

}