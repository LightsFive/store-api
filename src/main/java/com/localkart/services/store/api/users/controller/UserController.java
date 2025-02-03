package com.localkart.services.store.api.users.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.localkart.services.store.api.users.entity.UserEntity;
import com.localkart.services.store.api.users.model.CreateUserRequest;
import com.localkart.services.store.api.users.model.UserLoginRequest;
import com.localkart.services.store.api.users.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody @Valid CreateUserRequest request) {

        LOG.info("Registering user {}", request);
        userService.createUser(request);
    }

    @PostMapping("/login")
    public void login(@RequestBody UserLoginRequest request) {
        LOG.info("Login with OTP for user with phoneNumber {}", request.getPhoneNumber());

        //userService.findUserByPhoneNumber(request.getPhoneNumber());
        userService.loginWithOtp(request.getPhoneNumber());
    }

    @PostMapping("/validate")
    public Map<String, String> validateOTP(@RequestHeader String phoneNumber, @RequestHeader String otp) {
        LOG.info("OTP validation initiated for user with phoneNumber {}, {}", phoneNumber, otp);

        //boolean isValid = userService.validateUser(request.getPhoneNumber(), request.getOtp());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("Valid user {}", authentication.getName());
        var status =  authentication.getName() != null ? "SUCCESS" : "FAILED";
        return Map.of("status", status);
    }

    @DeleteMapping
    public UserEntity deleteUser(@RequestBody UserLoginRequest request) {

        LOG.info("Delete user with phoneNumber {}", request.getPhoneNumber());

        return userService.deleteUser(request.getPhoneNumber());
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("Get all users {}, {}", authentication.getName() , authentication.getPrincipal());

        return userService.getAllUsers();
    }
}
