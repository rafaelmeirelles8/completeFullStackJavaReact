package com.fullstack.completeFullStackJavaReact.Course;

import com.fullstack.completeFullStackJavaReact.Exceptions.BadRequestException;
import com.fullstack.completeFullStackJavaReact.Exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)  //initialize mocks
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    private CourseService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CourseService(courseRepository);
    }

    @Test
    void canGetAllCourses() {
        //when
        underTest.getAllCourses();

        //then
        verify(courseRepository).findAll(Sort.by("id").ascending());  //checking that getAllCourses invoked findAll Method
    }

    @Test
    void canCreateCourse() {
        //given
        Course course = new Course(
                "B1234",
                "Math"
        );

        //when
        underTest.createCourse(course);

        //then
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());

        Course capturedCourse = courseArgumentCaptor.getValue();
        assertThat(capturedCourse).isEqualTo(course);
    }

    @Test
    void canUpdateCourse() {
        //given
        Course course = new Course(
                "B1234",
                "Math"
        );

        //when
        underTest.updateCourse(course);

        //then
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseArgumentCaptor.capture());

        Course capturedCourse = courseArgumentCaptor.getValue();
        assertThat(capturedCourse).isEqualTo(course);
    }

    @Test
    void willThrownWhenCodeIsTaken() {
        //given
        String code = "B1234";

        Course course = new Course(
                code,
                "Math"
        );

        given(courseRepository.findByCode(anyString()))
                .willReturn(Optional.of(course));

        //when

        //then
        assertThatThrownBy(() -> underTest.createCourse(course))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Course with code " + course.getCode() + " already exists!");

        verify(courseRepository, never()).save(any());     //validating that save method is never being called as this method threw an Exception
    }

    @Test
    void canDeleteCourse() {
        //given
        String code = "B1234";
        Course course = new Course(
                code,
                "Math"
        );
        given(courseRepository.findById(any()))
                .willReturn(Optional.of(course));

        //when

        //then
        underTest.deleteCourse(course);
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).delete(courseArgumentCaptor.capture());

        Course capturedCourse = courseArgumentCaptor.getValue();
        assertThat(capturedCourse).isEqualTo(course);
    }

    @Test
    void canGetCourseById() {
        //given
        String code = "B1234";
        Course course = new Course(
                code,
                "Math"
        );
        given(courseRepository.findById(1L))
                .willReturn(Optional.of(course));

        //when
        underTest.getCourseById(1L);

        //then
        verify(courseRepository).findById(1L);
    }

    @Test
    void courseDoesNotExist() {
        //given
        long id = anyLong();

        //when

        //then
        assertThatThrownBy(() -> underTest.getCourseById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course with id " + id + " does not exist");
    }

    @Test
    void willThrownWhenCourseDoesNotExist() {
        //given
        String code = "B1234";
        Course course = new Course(
                code,
                "Math"
        );

        //when

        //then
        assertThatThrownBy(() -> underTest.deleteCourse(course))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course with id " + course.getId() + " does not exist");

        verify(courseRepository, never()).delete(any());     //validating that save method is never being called as this method threw an Exception

    }
}