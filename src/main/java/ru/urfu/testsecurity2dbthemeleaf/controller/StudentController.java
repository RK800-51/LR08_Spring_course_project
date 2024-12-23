package ru.urfu.testsecurity2dbthemeleaf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.testsecurity2dbthemeleaf.entity.Present;
import ru.urfu.testsecurity2dbthemeleaf.entity.Student;
import ru.urfu.testsecurity2dbthemeleaf.repository.PresentRepository;
import ru.urfu.testsecurity2dbthemeleaf.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PresentRepository presentRepository;

    @GetMapping("/list")
    public ModelAndView getAllStudents(Authentication authentication) {
        log.info("/list -> connection");
        ModelAndView modelAndView = new ModelAndView("list-students");
        modelAndView.addObject("students", studentRepository.findAll());
        for (Student student : studentRepository.findAll()) {
            System.out.println(student.getBirthday());
            System.out.println(student.getPresent());
        }
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
        boolean isReadOnly = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_READ_ONLY"));

        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("isUser", isUser);
        modelAndView.addObject("isReadOnly", isReadOnly);

        return modelAndView;
    }

    @GetMapping("/list-presents")
    public ModelAndView getAllPresents(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("list-presents");
        modelAndView.addObject("presents", presentRepository.findAll());
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isUser = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
        boolean isReadOnly = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_READ_ONLY"));

        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("isUser", isUser);
        modelAndView.addObject("isReadOnly", isReadOnly);

        return modelAndView;
    }

    @GetMapping("/addPresentForm")
    public ModelAndView addPresentForm() {
        ModelAndView modelAndView = new ModelAndView("add-present-form");
        Present present = new Present();
        modelAndView.addObject("present", present);
        return modelAndView;
    }

    @PostMapping("/savePresent")
    public String savePresent(@ModelAttribute Present present) {
        presentRepository.save(present);
        return "redirect:/list-presents";
    }

    @GetMapping("/deletePresent")
    public String deletePresent(@RequestParam Long id) {
        presentRepository.deleteById(id);
        return "redirect:/list-presents";
    }

    @GetMapping("/addStudentForm")
    public ModelAndView addStudentForm() {
        ModelAndView modelAndView = new ModelAndView("add-student-form");
        modelAndView.addObject("presents", presentRepository.findAll());
        Student student = new Student();
        modelAndView.addObject("student", student);
        return modelAndView;
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute Student student, @RequestParam(required = false) Long presentId) {
        if (presentId != null) {
            Present present = presentRepository.findById(presentId).
                    orElseThrow(() -> new IllegalArgumentException("Invalid Present ID: " + presentId));
            student.setPresent(present);
        }
        studentRepository.save(student);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long studentId) {
        ModelAndView modelAndView = new ModelAndView("add-student-form");
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Student student = new Student();
        if (optionalStudent.isPresent()) {
            student = optionalStudent.get();
        }
        modelAndView.addObject("presents", presentRepository.findAll());
        modelAndView.addObject("student", student);
        return modelAndView;
    }

    @GetMapping("/showUpdatePresentForm")
    public ModelAndView showUpdatePresentForm(@RequestParam Long presentId) {
        ModelAndView modelAndView = new ModelAndView("add-present-form");
        Optional<Present> optionalPresent = presentRepository.findById(presentId);
        Present present = new Present();
        if (optionalPresent.isPresent()) {
            present = optionalPresent.get();
        }
        modelAndView.addObject("present", present);
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long studentId) {
        studentRepository.deleteById(studentId);
        return "redirect:/list";
    }
}
