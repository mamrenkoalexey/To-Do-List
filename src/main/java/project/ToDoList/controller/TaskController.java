package project.ToDoList.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.ToDoList.entity.Status;
import project.ToDoList.entity.Task;
import project.ToDoList.entity.User;
import project.ToDoList.service.TaskService;
import project.ToDoList.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class TaskNotFoundException extends RuntimeException {
        public TaskNotFoundException(Long id) {
            super("Task not found with id: " + id);
        }
    }

    @GetMapping("/tasks")
    public String taskPage(Model model, Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        List<Task> listTasksForUser = taskService.getTasksForUser(user);

        model.addAttribute("user", user);
        model.addAttribute("listTasks", listTasksForUser);


        return "tasks";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        User user = userService.findByUserName(authentication.getName());
        task.setUser(user);
        task.setStatus(Status.CREATED);
        task.setCreatedAt(LocalDate.now());
        task.setUpdatedAt(LocalDate.now());
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }


    @PostMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id,
                                             @RequestParam Status status,
                                             Authentication auth) {

        User user = userService.findByUserName(auth.getName());

        Task task = taskService.getTasksForUser(user).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);


        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        task.setStatus(status);
        task.setUpdatedAt(LocalDate.now());

        return ResponseEntity.ok(taskService.updateTask(task));
    }


    @PostMapping("/{id}/edit")
    public ResponseEntity<Task> editTask(@PathVariable Long id,
                                         @RequestBody Task updatedTask, // ИСПРАВЛЕНО: используем @RequestBody для JSON
                                         Authentication auth) {
        User user = userService.findByUserName(auth.getName());
        Task task = taskService.getTasksForUser(user).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));


        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDeadline(updatedTask.getDeadline());
        task.setPriority(updatedTask.getPriority());

        task.setStatus(updatedTask.getStatus());
        task.setUpdatedAt(LocalDate.now());

        return ResponseEntity.ok(taskService.updateTask(task));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUserName(auth.getName());
        Task task = taskService.getTasksForUser(user).stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(id));


        if (task.getStatus() == Status.INACTIVE) {
            taskService.deleteTask(task.getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
