package hac.course.repositories;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Represents the User Entity.
 */
@Entity
public class User {
    /**
     * The id of the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the user.
     */
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    /**
     * The last name of the user.
     */
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    /**
     * The email of the user.
     */
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    /**
     * Defines a many-to-many relationship with the Course entity.
     * Specifies the join table for the many-to-many relationship.
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "user_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    /**
     * Getter for getting the id of the user.
     * @return the id of the user.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter that sets the id of the user.
     * @param id the id of the user.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for getting the first name of the user.
     * @return the first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter that sets the first name of the user.
     * @param firstName the first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for getting the last name of the user.
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter that sets the last name of the user.
     * @param lastName the last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for getting the email of the user.
     * @return the email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter that sets the email of the user.
     * @param email the email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for getting the set of courses the user is enrolled in.
     * @return the set of courses the user is enrolled in.
     */
    public Set<Course> getCourses() {
        return courses;
    }

    /**
     * Setter that sets the set of courses the user is enrolled in.
     * @param courses the set of courses the user is enrolled in.
     */
    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

}
