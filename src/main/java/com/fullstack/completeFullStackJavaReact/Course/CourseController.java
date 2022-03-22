package com.fullstack.completeFullStackJavaReact.Course;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/courses")
@AllArgsConstructor
//@CrossOrigin("*") added proxy on the package.json in the frontend
public class CourseController {

    @Autowired
    private final CourseService courseService;

    @GetMapping("/")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("/")
    public Course createCourse(@Valid @RequestBody Course course) { //@Valid to activate validation dependency like @NotBlank, @Email
        return courseService.createCourse(course);
    }

    @PutMapping("/")
    public void updateCourse(@Valid @RequestBody Course course) {
        courseService.updateCourse(course);
    }

    @DeleteMapping("/")
    public void deleteCourse(@RequestBody Course course) {
        courseService.deleteCourse(course);
    }

}
