package com.fullstack.completeFullStackJavaReact.Course;

import com.fullstack.completeFullStackJavaReact.Exceptions.BadRequestException;
import com.fullstack.completeFullStackJavaReact.Exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    @Autowired
    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll(Sort.by("id").ascending());
    }

    public Course getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);

        return course.orElseThrow(() -> new NotFoundException("Course with id " + id + " does not exist"));
    }

    public Course createCourse(Course course) {
        return saveCourse(course, true);
    }

    public void deleteCourse(Course course) {
        Optional<Course> existentCourse = courseRepository.findById(course.getId());

        existentCourse.orElseThrow(() -> new NotFoundException("Course with id " + course.getId() + " does not exist"));

        courseRepository.delete(course);
    }

    public void updateCourse(Course course) {
        saveCourse(course, false);
    }

    private Course saveCourse(Course course, Boolean isCreating) {
        Optional<Course> existentCourse = courseRepository.findByCode(course.getCode());
        if(existentCourse.isPresent() && isCreating) {
            throw new BadRequestException("Course with code " + course.getCode() + " already exists!");
        }

        return courseRepository.save(course);
    }
}
