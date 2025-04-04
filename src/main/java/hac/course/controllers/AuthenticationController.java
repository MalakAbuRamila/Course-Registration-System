package hac.course.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Represents the Authentication controller, handles user authentication processes such as login and logout.
 */
@Controller

public class AuthenticationController {


    /**
     * Displays the login page.
     * @param model The model to pass attributes to the view.
     * @param error to indicate a login error.
     * @param redirectAttributes for flash messages
     * @return The login page.
     */
    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(name = "error", required = false) String error,
                            RedirectAttributes redirectAttributes) {
        model.addAttribute("error", error != null);

        if (redirectAttributes.getFlashAttributes().containsKey("logoutMessage")) {
            model.addAttribute("logoutMessage", redirectAttributes.getFlashAttributes().get("logoutMessage"));
        }
        return "login";
    }

    /**
     * Handles the login process.
     * @param username the entered username.
     * @param password the entered password.
     * @param session The HTTP session.
     * @param model The model to pass attributes to the view.
     * @return The redirect to the admin main page if successful, otherwise the login page with an error.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {

        //check the stored admin username and password
        if ("admin".equals(username) && "password".equals(password)) {
            session.setAttribute("user", username);
            return "redirect:/admin/main";
        }
        else {
            model.addAttribute("error", true);
            return "login";
        }
    }


    /**
     * Handles the logout process.
     * @param session The HTTP session.
     * @param attributes for flash messages.
     * @return The redirect to the login page with a message confirming the logout.
     */
    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes attributes) {
        session.invalidate();
        attributes.addFlashAttribute("logoutMessage", "You have logged out");
        return "redirect:/login";
    }
}
