package ru.itmo.yatt.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.itmo.yatt.dto.ProjectDto;
import ru.itmo.yatt.exceptions.BadRequestException;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.repository.ProjectRepository;
import ru.itmo.yatt.security.SecurityUser;
import ru.itmo.yatt.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.yatt.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> findByUser(User user) {
        return projectRepository.findDistinctByParticipantsContains(user);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public void create(String name, String description) {
        if(this.existsByName(name))
            throw new BadRequestException("Project does exist");

        Project project = Project
                .builder()
                .setName(name)
                .setDescription(description)
                .build();

        projectRepository.save(project);
    }

    @Override
    public Boolean existsByName(String name) {
        return projectRepository.existsByName(name);
    }

    @Override
    public Optional<Project> findByUsersAndId(User participant, Long id) {
        return projectRepository.findByParticipantsContainsAndId(participant, id);
    }

    @Override
    public void addParticipant(Long projectId, User user) {
        addParticipant(projectRepository.findById(projectId).orElseThrow(() -> new BadRequestException("Project doesn't exist")), user);
    }

    @Override
    public void addParticipant(Project project, User user) {
        userService.addProject(project, user);
    }

    @Override
    public void addParticipant(Long projectId, Long userId) {
        addParticipant(projectId, userService.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists")));
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
