package dev.forum.forum.service;

import dev.forum.forum.exception.UserNotFoundException;
import dev.forum.forum.model.User;
import dev.forum.forum.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepo userRepo;

    public User get(Long id) {
        log.info("Fetching user by id {}", id);
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
    }

}
