package hac.course.config;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Represents an authentication interceptor.
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    /**
     * Intercepts HTTP requests before they reach the controller methods.
     * It checks if the user is authenticated by inspecting the session.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param handler The handler that will handle the request.
     * @return true if the request should proceed to the controller, otherwise false.
     * @throws Exception if an error occurs during the interception process.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        //check if there is no session or the session does not have a "user" attribute
        if (session == null || session.getAttribute("user") == null) {
            //redirect to the login page if the user is not authenticated
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

}

