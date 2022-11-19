package dev.forum.forum.controller;

import dev.forum.forum.model.user.User;
import dev.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(OK)
    public User getUser(@PathVariable(value = "userId") @Min(1) Long id) {
        return userService.getUserById(id);
    }
}

