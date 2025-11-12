package project.ToDoList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ToDoList.entity.Task;
import project.ToDoList.entity.User;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);
}