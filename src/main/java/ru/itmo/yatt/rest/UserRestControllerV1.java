package ru.itmo.yatt.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.yatt.model.User;
import ru.itmo.yatt.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1{

    private UserService userService;


    @Autowired
    public UserRestControllerV1(UserService userService){
        this.userService = userService;
    }


    @GetMapping
    public List<User> getUsers(){
        return userService.findAll();
    }
}
