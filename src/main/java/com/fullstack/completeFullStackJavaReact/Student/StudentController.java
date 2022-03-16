package com.fullstack.completeFullStackJavaReact.Student;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/students")
@AllArgsConstructor
//@CrossOrigin("*") added proxy on the package.json in the frontend
public class StudentController {

    @Autowired
    private final StudentService studentService;

    @GetMapping("/")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/")
    public void createStudent(@Valid @RequestBody Student student) { //@Valid to activate validation dependency like Student @NotBlank, @Email
        studentService.createStudent(student);
    }

    @DeleteMapping("/")
    public void deleteStudent(@RequestBody Student student) {
        studentService.deleteStudent(student);
    }

}
