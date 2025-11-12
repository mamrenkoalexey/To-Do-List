package project.ToDoList.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.ToDoList.entity.Status;
import project.ToDoList.entity.Task;
import project.ToDoList.entity.User;
import project.ToDoList.service.TaskService;
import project.ToDoList.service.UserService;

import java.util.List;


@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {

        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public List<Task> getTasks(Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        return taskService.getTasksForUser(user);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        task.setUser(user);
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask, Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        Task task = taskService.getTasksForUser(user).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        task.setTitle(updatedTask.getTitle());
        task.setDesc(updatedTask.getDesc());
        task.setDeadline(updatedTask.getDeadline());
        task.setPriority(updatedTask.getPriority());
        task.setStatus(updatedTask.getStatus());

        return taskService.updateTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        Task task = taskService.getTasksForUser(user).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        taskService.deleteTask(id);
    }

    @PatchMapping("/{id}/status")
    public Task toggleComplete(@PathVariable Long id, @RequestParam Status status, Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        Task task = taskService.getTasksForUser(user).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found or not authorized"));

        task.setStatus(status);
        return taskService.updateTask(task);
    }
}
