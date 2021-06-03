package ru.itmo.yatt.service;

import ru.itmo.yatt.dto.ProjectDto;
import ru.itmo.yatt.dto.TaskDto;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.security.SecurityUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */
public interface ProjectService {
    Optional<Project> findById(Long id);
    List<Project> findByUser(User user);
    List<Project> findAll();
    void create(String name, String description);
    Boolean existsByName(String name);
    Optional<Project> findByUsersAndId(User participants, Long id);
    void addParticipant(Long projectId, User user);
    void addParticipant(Project project, User user);
    void addParticipant(Long projectId, Long userId);
    void delete(Long id);
}
