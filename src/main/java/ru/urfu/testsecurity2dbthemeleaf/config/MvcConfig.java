package ru.urfu.testsecurity2dbthemeleaf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/list").setViewName("list-students");
        registry.addViewController("/addStudentForm").setViewName("add-student-form");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/users").setViewName("users");
        registry.addViewController("/addPresentForm").setViewName("add-present-form");
        registry.addViewController("/list-presents").setViewName("list-presents");
    }
}
