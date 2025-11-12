package project.ToDoList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ToDoList.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
