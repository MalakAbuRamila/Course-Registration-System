package hac.course.services;


import hac.course.repositories.User;
import hac.course.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Provides logic related to users.
 */
@Service
@Transactional
public class UserService {
    /**
     * Repository for handling user-related operations
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Saves a new user.
     * @param user The user to be saved.
     */
    public synchronized void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Gets a user by their email.
     * @param email The email of the user.
     * @return The user with the specified email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}