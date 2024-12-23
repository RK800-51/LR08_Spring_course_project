package ru.urfu.testsecurity2dbthemeleaf.controller;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.urfu.testsecurity2dbthemeleaf.dto.UserDto;
import ru.urfu.testsecurity2dbthemeleaf.entity.User;
import ru.urfu.testsecurity2dbthemeleaf.repository.UserRepository;
import ru.urfu.testsecurity2dbthemeleaf.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SecurityController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public SecurityController(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping
    public String logout() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", "На этот адрес электронной почты уже зарегистрирована учетная запись");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        List<String> availableRoles = Arrays.asList("ADMIN", "USER", "READ_ONLY");
        model.addAttribute("availableRoles", availableRoles);
        return "users";
    }

    @PostMapping("/users/change-role")
    public String changeUserRole(@RequestParam String email, @RequestParam String role) {
        User user = userRepository.findByEmail(email).isPresent() ? userRepository.findByEmail(email).get() : null;
        Set <String> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.clear();
        updatedRoles.add(role);
        user.setRoles(updatedRoles);

        userRepository.save(user);

        return "redirect:/users";
    }
}
