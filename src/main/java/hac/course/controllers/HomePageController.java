package hac.course.controllers;


import hac.course.repositories.Course;
import hac.course.repositories.User;
import hac.course.services.CourseService;
import hac.course.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.*;

/**
 * Represents the Home page controller, handles requests to the home page of the application
 */
@Controller
public class HomePageController {
    /**
     * Service for handling course-related operations
     */
    @Autowired
    private CourseService courseService;
    /**
     * Service for handling user-related operations
     */
    @Autowired
    private UserService userService;


    /**
     * Displays the home page.
     * @param model The model to pass attributes to the view.
     * @return The home page.
     */
    @GetMapping("/")
    public String viewHomePage(Model model) {

        //display the list of courses
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "index";
    }

    /**
     * Display the page to check courses.
     *
     * @return The check courses page.
     */
    @GetMapping("/check-courses")
    public String checkCoursesPage() {
        return "check-courses";
    }

    /**
     * Check if a user is registered for any courses.
     * @param email The email of the user to check.
     * @param session The HTTP session.
     * @param redirectAttributes for flash messages.
     * @return The redirect to the courses page if the user is registered, otherwise the check courses page with an error.
     */
    @PostMapping("/check-courses")
    public String checkCourses(@RequestParam String email, HttpSession session, RedirectAttributes redirectAttributes) {


        User user = userService.getUserByEmail(email);
        if (user != null && !user.getCourses().isEmpty()) {
            session.setAttribute("userEmail", email);
            return "redirect:/courses";
        }
        else {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not registered to any course.");
            return "redirect:/check-courses";
        }
    }

    /**
     * Display the courses page with the user's registered courses.
     * @param model The model to pass attributes to the view.
     * @param session The HTTP session.
     * @return The courses page
     */
    @GetMapping("/courses")
    public String viewCourses(Model model, HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                Set<Course> coursesSet = user.getCourses();
                List<Course> registeredCourses = new ArrayList<>(coursesSet);
                registeredCourses.sort(Comparator.comparing(Course::getId));
                model.addAttribute("registeredCourses", registeredCourses);
            }
        } else {
            return "redirect:/check-courses";
        }
        return "courses";
    }

}
