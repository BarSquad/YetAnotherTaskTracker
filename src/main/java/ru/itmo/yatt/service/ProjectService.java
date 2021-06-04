package ru.itmo.yatt.service;

import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.User;

import java.util.List;
import java.util.Optional;


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
