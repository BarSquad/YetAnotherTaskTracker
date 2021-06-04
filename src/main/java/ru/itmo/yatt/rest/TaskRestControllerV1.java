package ru.itmo.yatt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itmo.yatt.dto.TaskDto;
import ru.itmo.yatt.exceptions.BadRequestException;
import ru.itmo.yatt.exceptions.ForbiddenRequestException;
import ru.itmo.yatt.exceptions.NoContentException;
import ru.itmo.yatt.model.Role;
import ru.itmo.yatt.model.Task;
import ru.itmo.yatt.security.SecurityUser;
import ru.itmo.yatt.service.ProjectService;
import ru.itmo.yatt.service.TaskService;
import ru.itmo.yatt.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/tasks")
public class TaskRestControllerV1 {

    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public TaskRestControllerV1(TaskService taskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
    }


    @GetMapping
    public List<Task> getUsersTasks(@AuthenticationPrincipal SecurityUser user) {
        return taskService.findByUser(userService.getFromSecurityUser(user));
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id, @AuthenticationPrincipal SecurityUser securityUser) {
        if (securityUser.hasRole(Role.ADMIN))
            return taskService.findById(id).orElseThrow(() -> new NoContentException("Task doesn't exists"));
        else if (securityUser.hasRole(Role.MANAGER)){
            Task task = taskService.findById(id).orElseThrow(() -> new NoContentException("Task doesn't exists"));
            if (task.getProject().isMember(securityUser.getUsername()))
                return task;
            else
                throw new ForbiddenRequestException("You have no rights to access the task");
        }
        return taskService
                .findByUserAndId(userService.getFromSecurityUser(securityUser), id)
                .orElseThrow(() -> new BadRequestException("Task doesn't exists or you aren't reporter or assigner"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getAll() {
        return taskService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> createTask(@AuthenticationPrincipal SecurityUser securityUser, @Valid @RequestBody TaskDto taskDto){
        if (securityUser.hasRole(Role.MANAGER) && !projectService.findById(taskDto.getProjectId()).orElseThrow(() -> new BadRequestException("Project doesn't exist")).isMember(securityUser.getUsername()))
            throw new ForbiddenRequestException("You have no rights to create tasks for the project specified");

        taskService.create(taskDto, userService.getFromSecurityUser(securityUser));

        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<String> updateTask(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable Long id, @RequestBody @Valid TaskDto taskDto){
        if (securityUser.hasRole(Role.MANAGER) && !projectService.findById(taskDto.getProjectId()).orElseThrow(() -> new BadRequestException("Project doesn't exist")).isMember(securityUser.getUsername()))
            throw new ForbiddenRequestException("You have no rights to create tasks for the project specified");

        Task task = taskService.findById(id).orElseThrow(() -> new NoContentException("Task doesn't exist"));
        if (securityUser.hasRole(Role.MANAGER) && !task.getProject().isMember(securityUser.getUsername()))
            throw new ForbiddenRequestException("You have no rights to update tasks from the task's project");

        taskService.update(task, taskDto);
        return ResponseEntity.status(HttpStatus.OK).body("Task updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<String> deleteTask(@AuthenticationPrincipal SecurityUser securityUser, @PathVariable Long id){
        Task task = taskService.findById(id).orElseThrow(() -> new NoContentException("Task doesn't exist"));
        if (securityUser.hasRole(Role.MANAGER) && !task.getProject().isMember(securityUser.getUsername()))
            throw new ForbiddenRequestException("You have no rights delete tasks from the specified project");

        taskService.delete(task);

        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }
}

