package com.fullstack.completeFullStackJavaReact.Student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll(); //delete All Data after each test
    }

    @Test
    void itShouldFindStudentByEmail() {

        //given
        String email = "rafa@gmail.com";
        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE
        );
        underTest.save(student);

        //when
        Optional<Student> studentByEmail = underTest.findByEmail(email);

        //then
        assertThat(studentByEmail.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindStudentByEmail() {

        //given
        String email = "rafa@gmail.com";

        //when
        Optional<Student> studentByEmail = underTest.findByEmail(email);

        //then
        assertThat(studentByEmail.isPresent()).isFalse();
    }
}