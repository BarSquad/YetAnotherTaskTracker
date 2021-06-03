package ru.itmo.yatt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.yatt.dto.ProjectDto;
import ru.itmo.yatt.exceptions.BadRequestException;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.Role;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.security.SecurityUser;
import ru.itmo.yatt.service.ProjectService;
import ru.itmo.yatt.service.UserService;

import java.util.List;
import java.util.Map;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectRestControllerV1 {

    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public ProjectRestControllerV1(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getUsersProjects(@AuthenticationPrincipal SecurityUser user) {
        return projectService.findByUser(userService.getFromSecurityUser(user));
    }

    @GetMapping("/{id}")
    public Project getById(@PathVariable Long id, @AuthenticationPrincipal SecurityUser user) {
        if (user.hasRole(Role.ADMIN))
            return projectService.findById(id).orElseThrow(() -> new BadRequestException("Project doesn't exists"));
        return projectService
                .findByUsersAndId(userService.getFromSecurityUser(user), id)
                .orElseThrow(() -> new BadRequestException("Project doesn't exists or you aren't part of it"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Project> getAll() {
        return projectService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> createProject(@Validated @RequestBody ProjectDto projectDto){
        projectService.create(projectDto.getName(), projectDto.getDescription());

        return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully");
    }

    @PostMapping("/addParticipant")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<String> addParticipant(@AuthenticationPrincipal SecurityUser securityUser, @RequestBody Map<String, Long> payload){
        if (securityUser.hasRole(Role.MANAGER)) {
            User user = userService.getFromSecurityUser(securityUser);
            Project project = projectService
                    .findByUsersAndId(user, payload.get("projectId"))
                    .orElseThrow(() -> new BadRequestException("Project doesn't exists or you aren't part of it"));
            projectService.addParticipant(project, user);
        }
        projectService.addParticipant(payload.get("projectId"), payload.get("userId"));

        return ResponseEntity.status(HttpStatus.CREATED).body("User added to project successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProject(@PathVariable Long id){
        projectService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Project deleted successfully");
    }



}
