package com.fullstack.completeFullStackJavaReact.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)  //initialize mocks
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }


    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();

        //then
        verify(studentRepository).findAll();  //checking that getAllStudents invoked findAllMethod
    }

    @Test
    void canCreateStudent() {
        //give
        String email = "rafa@gmail.com";
        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE
        );

        //when
        underTest.createStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrownWhenEmailIsTaken() {
        //give
        String email = "rafa@gmail.com";
        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE
        );

        //when

        //then
        //when(studentRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));  //when or given do the same
        given(studentRepository.findByEmail(anyString()))  //using anyString or student.getEmail() is the same
                        .willReturn(Optional.of(student));
        assertThatThrownBy(() -> underTest.createStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student with email " + student.getEmail() + " already exists!");

        verify(studentRepository, never()).save(any());     //validating that save method is never being called as this method threw an Exception

    }

    @Test
    void canDeleteStudent() {
        /* In case if method receives only ID
            //given
            long id = 10;
            given(studentRepository.existsById(id))
                    .willReturn(true);
            // when
            underTest.deleteStudent(id);

            // then
            verify(studentRepository).deleteById(id);
         */

        //given
        String email = "rafa@gmail.com";
        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE
        );
        given(studentRepository.findById(any()))
                .willReturn(Optional.of(student));

        //when

        //then

        underTest.deleteStudent(student);
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).delete(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrownWhenStudentDoesNotExist() {
        //give
        String email = "rafa@gmail.com";
        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE
        );

        //when

        //then
        assertThatThrownBy(() -> underTest.deleteStudent(student))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("User with id " + student.getId() + " does not exist");

        verify(studentRepository, never()).delete(any());     //validating that save method is never being called as this method threw an Exception

    }



    @Test
    @Disabled
    void findStudent() {
    }
}