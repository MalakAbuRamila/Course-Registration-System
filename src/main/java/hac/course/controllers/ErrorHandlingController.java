package hac.course.controllers;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Represents the Error handling controller, responsible for handling and displaying custom error messages and pages.
 */
@Controller
public class ErrorHandlingController  implements  ErrorController{

    /**
     * Handles errors and display an error page.
     * @param request The HTTP request.
     * @param model The model to pass attributes to the view.
     * @return The error page.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An unexpected error occurred";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            errorMessage = switch (statusCode) {
                case 400 -> "Bad Request";
                case 401 -> "Unauthorized";
                case 403 -> "Forbidden";
                case 404 -> "Page Not Found";
                case 500 -> "Internal Server Error";
                default -> errorMessage;
            };
        }

        model.addAttribute("statusCode", status);
        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }
}
