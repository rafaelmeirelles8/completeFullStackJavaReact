package com.fullstack.completeFullStackJavaReact.Student;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void createStudent(Student student) {
        Optional<Student> existentStudent = studentRepository.findByEmail(student.getEmail());
        if(existentStudent.isPresent()) {
            throw new BadRequestException("Student with email " + student.getEmail() + " already exists!");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Student student) {
        Optional<Student> existentStudent = studentRepository.findById(student.getId());
        existentStudent.orElseThrow(() -> new StudentNotFoundException("User with id " + student.getId() + " does not exist"));
        studentRepository.delete(student);
    }
}
