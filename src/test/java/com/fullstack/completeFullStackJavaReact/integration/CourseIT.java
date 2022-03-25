package com.fullstack.completeFullStackJavaReact.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.completeFullStackJavaReact.Course.Course;
import com.fullstack.completeFullStackJavaReact.Course.CourseRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
@Transactional
public class CourseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    private Faker faker = new Faker();

    @Test
    void canCreateNewCourse() throws Exception {
        //given
        String code = "B1234";

        Course course = new Course(
                code,
                "Math"
        );

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/courses/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course))
        );

        //then
        List<Course> courses = courseRepository.findAll();
        resultActions.andExpect(status().isOk());

        assertThat(courses).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(course);
    }

    @Test
    void canUpdateCourse() throws Exception {
        //given
        String code = "B1234";

        Course course = new Course(
                code,
                "Math"
        );

        //when
        Course newCourse = courseRepository.save(course);

        newCourse.setName("Physics");
        mockMvc.perform(put("/api/v1/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse))
                ).andExpect(status().isOk());

        //then
        List<Course> courses = courseRepository.findAll();

        assertThat(courses).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(newCourse);
    }

    @Test
    void canFindById() throws Exception {
        //given
        String code = "B1234";

        Course course = new Course(
                code,
                "Math"
        );

        MvcResult mvcResultNewCourse = mockMvc.perform(post("/api/v1/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResultNewCourse
                .getResponse()
                .getContentAsString();

        Course newCourse = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        // when
        MvcResult mvcResultExistentCourse = mockMvc
                .perform(get("/api/v1/courses/" + newCourse.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        contentAsString = mvcResultExistentCourse
                .getResponse()
                .getContentAsString();

        Course existentCourse = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        // then
        assertThat(newCourse).isEqualTo(existentCourse);

    }

    @Test
    void canDeleteCourse() throws Exception {
        //given
        String code = "B1234";

        Course course = new Course(
                code,
                "Math"
        );

        mockMvc.perform(post("/api/v1/courses/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());

        MvcResult getCoursesResult = mockMvc.perform(get("/api/v1/courses/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getCoursesResult
                .getResponse()
                .getContentAsString();

        List<Course> courses = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        Course existentCourse = courses.stream()
                .filter(c -> c.getCode().equals(course.getCode()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "course with code: " + code + " not found"));

        // when
        ResultActions resultActions = mockMvc
                //.perform(delete("/api/v1/courses/" + id));
                .perform(delete("/api/v1/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existentCourse)));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = courseRepository.existsById(existentCourse.getId());
        assertThat(exists).isFalse();
    }
}
