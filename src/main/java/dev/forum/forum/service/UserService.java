package dev.forum.forum.service;

import dev.forum.forum.exception.ForumException;
import dev.forum.forum.model.user.SecurityUser;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found in the database."));
        log.info("User found in the database: {}", username);
        return new SecurityUser(user);
    }

    public User getUserById(Long id) {
        log.info("Fetching user by id {}", id);
        return userRepo.findById(id)
                .orElseThrow(() -> new ForumException("User with id " + id + " not found."));
    }

}
