package com.fullstack.completeFullStackJavaReact.Course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll(); //delete All Data after each test
    }

    @Test
    void itShouldFindCourseByCode() {

        //given
        String code = "B1234";

        Course course = new Course(
                code,
                "Math"
        );
        underTest.save(course);

        //when
        Optional<Course> courseByCode = underTest.findByCode(code);

        //then
        assertThat(courseByCode.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindCourseByCode() {

        //given
        String code = "B1234";

        //when
        Optional<Course> courseByCode = underTest.findByCode(code);

        //then
        assertThat(courseByCode.isPresent()).isFalse();
    }
}