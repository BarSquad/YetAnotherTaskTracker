package ru.itmo.yatt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.Task;
import ru.itmo.yatt.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignee(User user);
    List<Task> findByReporter(User user);
    List<Task> findByReporterOrAssignee(User reporter, User assignee);
    Optional<Task> findByReporterOrAssigneeAndId(User reporter, User assignee, Long id);
    List<Task> findByProjectIn(Collection<Project> project);
}

