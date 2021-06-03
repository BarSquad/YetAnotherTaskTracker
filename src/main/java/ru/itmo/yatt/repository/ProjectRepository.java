package ru.itmo.yatt.repository;

import lombok.NonNull;
import ru.itmo.yatt.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.yatt.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Boolean existsByName(String name);
    List<Project> findDistinctByParticipantsContains(User participants);
    Optional<Project> findByParticipantsContainsAndId(User participants, Long id);
}
