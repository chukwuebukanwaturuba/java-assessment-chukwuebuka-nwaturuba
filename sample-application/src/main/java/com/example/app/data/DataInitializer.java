package com.example.app.data;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Creates two demo users on startup so there's something to test with.
// user / user123 gets ROLE_USER, admin / admin123 gets both ROLE_USER and ROLE_ADMIN.
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setPassword(passwordEncoder.encode("user123"));
        regularUser.getRoles().add("ROLE_USER");
        userRepository.save(regularUser);

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.getRoles().add("ROLE_USER");
        adminUser.getRoles().add("ROLE_ADMIN");
        userRepository.save(adminUser);

        log.info("Demo users seeded: user / admin");
    }
}
