package hac.course.repositories;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Course Entity.
 */
@Entity
public class Course {
    /**
     * The id of the course.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the course.
     */
    @NotBlank(message = "Course name is mandatory")
    private String name;

    /**
     * The maximum number of participants.
     */
    @Min(value = 1, message = "Maximum participants must be at least 1")
    private int maxParticipants;

    /**
     * full to indicate whether a course is full or not.
     */
    private boolean full;

    /**
     *  Defines a many-to-many relationship with the User entity
     *  Set of users participating in the course.
     */
    @ManyToMany(mappedBy = "courses", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<User> participants = new HashSet<>();


    /**
     * Getter for getting the id of the course.
     * @return the id of the course
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter that sets the id of the course.
     * @param id the id of the course.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for getting whether the course is full or not.
     * @return whether the course is full or not.
     */
    public boolean getFull(){
        return participants.size() >= maxParticipants;
    }

    /**
     * Setter that sets whether the course is full or not.
     * @param full whether the course is full or not.
     */
    public void setFull(boolean full){
        this.full = full;
    }

    /**
     * Getter for getting the name of the course.
     * @return the name of the course
     */
    public String getName() {
        return name;
    }

    /**
     * Setter that sets the name of the course.
     * @param name the name of the course.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for getting the number of maximum participants of the course.
     * @return the number of maximum participants of the course.
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Setter that sets the number of maximum participants of the course.
     * @param maxParticipants the number of maximum participants of the course.
     */
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    /**
     * Getter for getting the list of participants of the course.
     * @return the set of participants of the course.
     */
    public Set<User> getParticipants() {
        return participants;
    }

    /**
     * Setter that sets the number of participants of the course.
     * @param participants the list of participants of the course.
     */
    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }
}
