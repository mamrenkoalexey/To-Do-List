package project.ToDoList.service;

import org.springframework.stereotype.Service;
import project.ToDoList.entity.Task;
import project.ToDoList.entity.User;
import project.ToDoList.repository.TaskRepository;

import java.util.List;

@Service

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findAllByUser(user);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
