package hac.course.controllers;


import hac.course.repositories.Course;
import hac.course.repositories.UserRepository;
import hac.course.repositories.CourseRepository;
import hac.course.services.CourseService;
import hac.course.repositories.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


import java.util.Comparator;
import java.util.List;


/**
 * Represents the Admin controller, handles admin functionalities.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Service for handling course-related operations
     */
    @Autowired
    private CourseService courseService;
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
     * View the main admin page.
     * @param keyword The keyword for filtering courses.
     * @param session The HTTP session.
     * @param model The model to pass attributes to the view.
     * @return the main admin page.
     */
    @GetMapping("/main")
    public String viewAdminMain(@RequestParam(name = "keyword", required = false) String keyword, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null || !"admin".equals(user.toString())) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.toString());

        List<Course> courses;
        if (keyword != null && !keyword.isEmpty()) {
            courses = courseService.searchCourses(keyword);
        }
        else {
            courses = courseService.getAllCourses();
        }
        model.addAttribute("courses", courses);
        return "admin/main";
    }

    /**
     * Show the form for creating a new course.
     * @param model The model to pass attributes to the view.
     * @return The new course form page.
     */
    @GetMapping("/courses/new-course")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", new Course());

        //initially set error message to null
        model.addAttribute("errorMessage", null);
        return "admin/new-course";
    }

    /**
     * Save the new course.
     * @param course The course to save.
     * @param bindingResult The binding result for handling validation error.
     * @param model The model to pass attributes to the view.
     * @return The redirect to the main admin page or the new course form page if there are errors.
     */
    @PostMapping("/courses")
    public String saveCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/new-course";
        }
        try {
            courseService.saveCourse(course);
            return "redirect:/admin/main";
        }

        catch (IllegalArgumentException e) {
            //set error message if the course name already exists
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/new-course";
        }
    }


    /**
     * Deletes a course by its id.
     * @param id The id of the course to delete.
     * @param model The model to pass attributes to the view.
     * @return The redirect to the main admin page.
     */
    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, Model model) {

        //delete the course by its id
        try {
            courseService.deleteCourse(id);
            return "redirect:/admin/main";
        }

        catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("courses", courseService.getAllCourses());
            return "admin/main";
        }
    }

    /**
     * Show the form to edit an existing course.
     * @param id The id of the course to be edited.
     * @param model The model to pass attributes to the view.
     * @return The edit course form page.
     */
    @GetMapping("/courses/edit-course/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("errorMessage", null);
        return "admin/edit-course";
    }

    /**
     * Updates an existing course.
     * @param id the id of the course to update.
     * @param course The updated course.
     * @param bindingResult The binding result for handling validation errors.
     * @param model The model to pass attributes to the view.
     * @return The redirect to the main admin page or the edit course form page if there are errors.
     */
    @PostMapping("/courses/{id}")
    public String updateCourse(@PathVariable Long id, @Valid @ModelAttribute("course") Course course, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/edit-course";
        }
        Course existingCourse = courseService.getCourseById(id);

        try {
            courseService.updateCourse(existingCourse, course);
            return "redirect:/admin/main";
        }
        catch (IllegalArgumentException e) {

            //set error message if the course name already exists
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("course", existingCourse);
            return "admin/edit-course";
        }
    }


    /**
     * Views participants of a course.
     * @param id The id of the course.
     * @param model The model to pass attributes to the view.
     * @return The course participants page.
     */
    @GetMapping("/courses/{id}/participants")
    public String viewCourseParticipants(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);

        List<User> participants = course.getParticipants().stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();

        model.addAttribute("course", course);
        model.addAttribute("participants", participants);
        return "admin/participants";
    }


    /**
     * Removes a participant from a course.
     *
     * @param courseId The id of the course.
     * @param userId The id of the user to remove.
     * @return The redirect to the course participants page.
     */
    @PostMapping("/removeParticipant")
    public String removeParticipant(@RequestParam Long courseId, @RequestParam Long userId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Invalid course id:" + courseId));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id:" + userId));

        course.getParticipants().remove(user);
        user.getCourses().remove(course);


        courseRepository.save(course);
        userRepository.save(user);

        //check if the user is enrolled in any other courses, if not delete the user
        if (user.getCourses().isEmpty()) {
            userRepository.delete(user);
        }

        return "redirect:/admin/courses/" + courseId + "/participants";
    }


}
