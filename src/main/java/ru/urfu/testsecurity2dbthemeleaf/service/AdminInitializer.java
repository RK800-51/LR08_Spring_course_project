package ru.urfu.testsecurity2dbthemeleaf.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:admin}") // Имя администратора по умолчанию
    private String adminUsername;

    @Value("${admin.password:admin123}") // Пароль администратора по умолчанию
    private String adminPassword;

    @Value("${admin.email:admin@mail.com}") // Почта администратора по умолчанию
    private String adminEmail;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminIfNotExists() {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setRoles(new HashSet<>(Set.of("ADMIN")));
            userRepository.save(admin);
            System.out.println("Администратор создан: " + adminUsername);
        }
    }
}