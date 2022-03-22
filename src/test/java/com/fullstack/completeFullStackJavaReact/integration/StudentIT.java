package com.fullstack.completeFullStackJavaReact.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.completeFullStackJavaReact.Student.Gender;
import com.fullstack.completeFullStackJavaReact.Student.Student;
import com.fullstack.completeFullStackJavaReact.Student.StudentRepository;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-it.properties")
@AutoConfigureMockMvc
@Transactional
public class StudentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private Faker faker = new Faker();

    @Test
    void canCreateNewStudent() throws Exception {
        //given
        String email = faker.internet().emailAddress();

        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE,
                LocalDate.of(1987,12,24),
                new HashSet<>()
        );

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student))
        );

        //then
        List<Student> students = studentRepository.findAll();
        resultActions.andExpect(status().isOk());

        assertThat(students).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(student);
    }

    @Test
    void canUpdateStudent() throws Exception {
        //given
        String email = faker.internet().emailAddress();

        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE,
                LocalDate.of(1987,12,24),
                new HashSet<>()
        );

        //when
        Student newStudent = studentRepository.save(student);

        newStudent.setName("Fael");
        mockMvc.perform(put("/api/v1/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                ).andExpect(status().isOk());

        //then
        List<Student> students = studentRepository.findAll();

        assertThat(students).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(newStudent);
    }

    @Test
    void canFindById() throws Exception {
        //given
        String email = faker.internet().emailAddress();

        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE,
                LocalDate.of(1987,12,24),
                new HashSet<>()
        );

        MvcResult mvcResultNewStudent = mockMvc.perform(post("/api/v1/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = mvcResultNewStudent
                .getResponse()
                .getContentAsString();

        Student newStudent = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        // when
        MvcResult mvcResultExistentStudent = mockMvc
                .perform(get("/api/v1/students/" + newStudent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        contentAsString = mvcResultExistentStudent
                .getResponse()
                .getContentAsString();

        Student existentStudent = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        // then
        assertThat(newStudent).isEqualTo(existentStudent);

    }

    @Test
    void canDeleteStudent() throws Exception {
        //given
        String email = faker.internet().emailAddress();

        Student student = new Student(
                "Rafael",
                email,
                Gender.MALE,
                LocalDate.of(1987,12,24),
                new HashSet<>()
        );

        mockMvc.perform(post("/api/v1/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        MvcResult getStudentsResult = mockMvc.perform(get("/api/v1/students/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getStudentsResult
                .getResponse()
                .getContentAsString();

        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        Student existentStudent = students.stream()
                .filter(s -> s.getEmail().equals(student.getEmail()))
                //.map(Student::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "student with email: " + email + " not found"));

        // when
        ResultActions resultActions = mockMvc
                //.perform(delete("/api/v1/students/" + id));
                .perform(delete("/api/v1/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existentStudent)));

        // then
        resultActions.andExpect(status().isOk());
        boolean exists = studentRepository.existsById(existentStudent.getId());
        assertThat(exists).isFalse();
    }
}
