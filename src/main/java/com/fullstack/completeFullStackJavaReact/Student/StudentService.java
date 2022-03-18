package com.fullstack.completeFullStackJavaReact.Student;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by("id").ascending());
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " does not exist"));
    }

    public void createStudent(Student student) {
        saveStudent(student, true);
    }

    public void deleteStudent(Student student) {
        Optional<Student> existentStudent = studentRepository.findById(student.getId());

        existentStudent.orElseThrow(() -> new StudentNotFoundException("Student with id " + student.getId() + " does not exist"));

        studentRepository.delete(student);
    }

    public void updateStudent(Student student) {
        saveStudent(student, false);
    }

    private void saveStudent(Student student, Boolean isCreating) {
        Optional<Student> existentStudent = studentRepository.findByEmail(student.getEmail());
        if(existentStudent.isPresent() && isCreating) {
            throw new BadRequestException("Student with email " + student.getEmail() + " already exists!");
        }

        studentRepository.save(student);
    }
}
