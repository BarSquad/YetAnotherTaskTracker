package ru.itmo.yatt.service.impl;

import ru.itmo.yatt.dto.SignUpRequest;
import ru.itmo.yatt.exceptions.BadRequestException;
import ru.itmo.yatt.model.Project;
import ru.itmo.yatt.model.Role;
import ru.itmo.yatt.model.UserStatus;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.repository.UserRepository;
import ru.itmo.yatt.security.SecurityUser;
import ru.itmo.yatt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Vladimir Goncharov
 * @created 08.05.2021
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void register(SignUpRequest userDTO) {
        if (this.existsByEmail(userDTO.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.WORKER);
        userRepository.saveAndFlush(user);
    }

    @Override
    public User getFromSecurityUser(SecurityUser securityUser) {
        return this.findByEmail(securityUser.getUsername()).get();
    }

    @Override
    public void addProject(Project project, User user) {
        user.getProjects().add(project);
        userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
