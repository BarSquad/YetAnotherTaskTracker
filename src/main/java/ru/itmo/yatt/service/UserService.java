package ru.itmo.yatt.service;

import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.dto.SignUpRequest;
import ru.itmo.yatt.security.SecurityUser;

import java.util.List;
import java.util.Optional;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Boolean existsByEmail(String email);
    void register(SignUpRequest userDTO);
    User getFromSecurityUser(SecurityUser securityUser);
    void addProject(Project project, User user);
    List<User> findAll();

}
