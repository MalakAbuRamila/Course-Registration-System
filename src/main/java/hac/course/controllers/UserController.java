package hac.course.controllers;


import hac.course.repositories.User;
import hac.course.repositories.Course;
import hac.course.services.CourseService;
import hac.course.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Represents the User controller, handles functionalities related to user registration and management.
 */
@Controller
@RequestMapping("/registration")
public class UserController {

    /**
     * Service for handling user-related operations
     */
    @Autowired
    private UserService userService;
    /**
     * Service for handling course-related operations
     */
    @Autowired
    private CourseService courseService;

    /**
     * Displays the user registration form page with the selected courses.
     * @param selectedCourses List of course IDs that the user has selected for registration.
     * @param model The model object used to pass data to the view.
     * @return The registration page.
     */
    @GetMapping
    public String showRegistrationForm(@RequestParam(value = "selectedCourses", required = false) List<Long> selectedCourses, Model model) {
        //add new user
        model.addAttribute("user", new User());

        //get all courses and mark them as full or not.
        List<Course> courses = courseService.getAllCourses().stream()
                .peek(course -> course.setFull(courseService.isCourseFull(course.getId())))
                .collect(Collectors.toList());

        //add the list of courses to the model
        model.addAttribute("courses", courses);

        if (selectedCourses != null) {
            //map course IDs to Course objects and add them to the model if they are selected
            List<Course> selected = selectedCourses.stream()
                    .map(courseService::getCourseById)
                    .collect(Collectors.toList());

            model.addAttribute("selectedCourses", selected);
        }

        return "registration";
    }

    /**
     * Handles the submission of the user registration form.
     * @param user The User object containing registration details.
     * @param bindingResult The binding result for handling validation error.
     * @param selectedCourses List of course IDs that the user selected.
     * @param model The model to pass attributes to the view.
     * @param redirectAttributes for flash messages.
     * @return The register success page if registration is successful or the redirect to the registration page with errors.
     */
    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                               @RequestParam(value = "selectedCourses", required = false) List<Long> selectedCourses,
                               Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("user", user);
            model.addAttribute("courses", courseService.getAllCourses());

            if (selectedCourses != null && !selectedCourses.isEmpty()) {
                redirectAttributes.addAttribute("selectedCourses", selectedCourses);
            }

            return "registration";
        }

        //check if any of the selected courses are full
        if (selectedCourses != null) {
            List<String> fullCourses = new ArrayList<>();
            for (Long courseId : selectedCourses) {
                if (courseService.isCourseFull(courseId)) {
                    Course course = courseService.getCourseById(courseId);
                    fullCourses.add(course.getName());
                }
            }

            //if courses are full display an error message
            if (!fullCourses.isEmpty()) {
                String errorMessage = "The following courses are full: " + String.join(", ", fullCourses);

                redirectAttributes.addFlashAttribute("error", errorMessage);
                redirectAttributes.addAttribute("selectedCourses", selectedCourses);

                //redirect to the registration page with query parameters
                return "redirect:/registration";

            }
        }

        //check if the user already exists
        User existingUser = userService.getUserByEmail(user.getEmail());

        if (existingUser != null) {
            //check if the user is already registered for any of the selected courses
            if (selectedCourses != null) {
                List<String> alreadyRegisteredCourses = new ArrayList<>();
                Set<Course> newCourses = new HashSet<>();
                for (Long courseId : selectedCourses) {
                    Course course = courseService.getCourseById(courseId);
                    if (existingUser.getCourses().contains(course)) {
                        alreadyRegisteredCourses.add(course.getName());
                    } else {
                        newCourses.add(course);
                    }
                }

                //if the user is already registered to the courses display an error message
                if (!alreadyRegisteredCourses.isEmpty()) {
                    String errorMessage = "You are already registered for the following courses: " + String.join(", ", alreadyRegisteredCourses);

                    redirectAttributes.addFlashAttribute("error", errorMessage);
                    redirectAttributes.addAttribute("selectedCourses", selectedCourses);

                    //redirect to the registration page with query parameters
                    return "redirect:/registration";

                }
                //add new courses if there's no conflict
                existingUser.getCourses().addAll(newCourses);
            }
            userService.saveUser(existingUser);
        } else {
            if (selectedCourses != null && !selectedCourses.isEmpty()) {
                Set<Course> courses = selectedCourses.stream()
                        .map(courseService::getCourseById)
                        .collect(Collectors.toSet());
                user.setCourses(courses);
            }
            userService.saveUser(user);
        }
        return "redirect:/registration/register-success";
    }


    /**
     * View the register success page.
     * @return The register success page.
     */
    @GetMapping("/register-success")
    public String registrationSuccess() {
        return "register-success";
    }
}
