package com.fullstack.completeFullStackJavaReact.Student;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstack.completeFullStackJavaReact.Course.Course;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

//@Data set fields to final
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Student {

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "student_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dob;

    @Transient
    private Integer age;

    @Transient
    private String genderToDisplay;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "students")
    private Set<Course> coursesTaken = new HashSet<>();

    public Student(String name, String email, Gender gender) {
        this(name, email, gender, LocalDate.now());
    }

    public Student(String name, String email, Gender gender, LocalDate dob) {
        this(name, email, gender,dob, new HashSet<>());
    }

    public Student(String name, String email, Gender gender, LocalDate dob,Set<Course> coursesTaken) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.coursesTaken = coursesTaken;
    }

    public Integer getAge() {
        return this.dob != null ? Period.between(this.dob, LocalDate.now()).getYears() : null;
    }

    public String getGenderToDisplay() {
        return gender.getName();
    }
}
