package hac.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Provides methods for performing operations on User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds the user by their email.
     * @param email The email of the user
     * @return The user with the specified email.
     */
    User findByEmail(String email);
}
