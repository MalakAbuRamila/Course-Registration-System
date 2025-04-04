package hac.course.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Represents the Web Configurer controller.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * The AuthenticationInterceptor bean.
     */
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    /**
     * Configures the interceptors for the application.
     * @param registry The registry to which interceptors can be added.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/admin/**");
    }
}
