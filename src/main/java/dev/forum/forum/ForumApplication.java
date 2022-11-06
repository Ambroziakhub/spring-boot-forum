package dev.forum.forum;

import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@SpringBootApplication
public class ForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(UserRepo userRepo, PasswordEncoder passwordEncoder) {


        return args -> {
            User user1 = new User(
                    null,
                    "janek",
                    passwordEncoder.encode("password"),
                    "jan.kowalski@email.com",
                    Instant.now(),
                    Boolean.TRUE,
                    UserRole.USER
            );

            userRepo.save(user1);
        };
    }

}
