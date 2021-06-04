package ru.itmo.yatt.service;

import ru.itmo.yatt.dto.TaskDto;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.Task;
import ru.itmo.yatt.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface TaskService {
    void create(TaskDto taskDto, User reporter);
    List<Task> findAll();
    List<Task> findByUser(User user);
    Optional<Task> findByUserAndId(User user, Long id);
    Optional<Task> findById(Long id);
    List<Task> findByAssignee(User user);
    List<Task> findByReporter(User user);
    List<Task> findByProjects(Collection<Project> projects);
    void update(Long id, TaskDto taskDto);
    void update(Task task, TaskDto taskDto);
    void delete(Long id);
    void delete(Task id);
}
