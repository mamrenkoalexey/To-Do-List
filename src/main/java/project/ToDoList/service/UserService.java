package project.ToDoList.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.ToDoList.entity.Role;
import project.ToDoList.entity.User;
import project.ToDoList.repository.UserRepository;

@Service

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder password;

    public UserService(UserRepository userRepository, PasswordEncoder password) {
        this.userRepository = userRepository;
        this.password = password;
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User: " + user.getUsername() + " already exists.");
        }
        user.setPassword(password.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User: " + username + " not found."));
    }

}
