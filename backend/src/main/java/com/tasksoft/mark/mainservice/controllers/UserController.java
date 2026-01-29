package com.tasksoft.mark.mainservice.controllers;

import com.tasksoft.mark.mainservice.dto.UserCreateDto;
import com.tasksoft.mark.mainservice.dto.UserDto;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateDto user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> allUsers = userService.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        for (User allUser : allUsers) {
            UserDto convert = UserDto.convert(allUser);
            userDtos.add(convert);
        }
        return ResponseEntity.ok(userDtos);
    }
}
