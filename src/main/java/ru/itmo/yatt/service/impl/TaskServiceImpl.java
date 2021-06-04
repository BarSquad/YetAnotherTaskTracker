package ru.itmo.yatt.service.impl;

import org.springframework.stereotype.Service;
import ru.itmo.yatt.dto.TaskDto;
import ru.itmo.yatt.exceptions.BadRequestException;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.Task;
import ru.itmo.yatt.model.TaskStatus;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.repository.TaskRepository;
import ru.itmo.yatt.service.ProjectService;
import ru.itmo.yatt.service.TaskService;
import ru.itmo.yatt.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final static TaskStatus DEFAULT_TASK_STATUS = TaskStatus.BACKLOG;

    public TaskServiceImpl(UserService userService, TaskRepository taskRepository, ProjectService projectService) {
        this.userService = userService;
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }

    @Override
    public void create(TaskDto taskDto, User reporter) {
        Task task = Task
                .builder()
                .withName(taskDto.getName())
                .withDescription(taskDto.getDescription())
                .withStatus(DEFAULT_TASK_STATUS)
                .withAssignee(taskDto.getAssigneeId() == null? null : userService.findById(taskDto.getAssigneeId()).orElse(null))
                .withReporter(reporter)
                .withProject(projectService.findById(taskDto.getProjectId()).orElseThrow(() -> new BadRequestException("Project doesn't exists")))
                .build();

        taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByUser(User user) {
        return taskRepository.findByReporterOrAssignee(user, user);
    }

    @Override
    public Optional<Task> findByUserAndId(User user, Long id) {
        return taskRepository.findByReporterOrAssigneeAndId(user, user, id);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findByAssignee(User user) {
        return taskRepository.findByAssignee(user);
    }

    @Override
    public List<Task> findByReporter(User user) {
        return taskRepository.findByReporter(user);
    }

    @Override
    public List<Task> findByProjects(Collection<Project> projects) {
        return taskRepository.findByProjectIn(projects);
    }

    @Override
    public void update(Long id, TaskDto taskDto) {
        update(findById(id).orElseThrow(() -> new BadRequestException("Task doesn't exist")), taskDto);
    }

    @Override
    public void update(Task task, TaskDto taskDto) {
        task.setName(taskDto.getName());
        task.setProject(projectService.findById(taskDto.getProjectId()).orElseThrow(() -> new BadRequestException("Project doesn't exists")));
        task.setDescription(taskDto.getDescription());
        if(taskDto.getStatus() != null)
            task.setStatus(TaskStatus.valueOf(taskDto.getStatus()));
        task.setAssignee(userService.findById(taskDto.getAssigneeId()).orElse(null));
        taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
