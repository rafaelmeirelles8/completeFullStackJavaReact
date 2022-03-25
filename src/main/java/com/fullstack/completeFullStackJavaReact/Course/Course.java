package com.fullstack.completeFullStackJavaReact.Course;

import com.fullstack.completeFullStackJavaReact.Student.Student;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Course {

    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "course_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 5)
    private String code;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students= new HashSet<>();

    public Course(String code, String name) {
        this(code,name, new HashSet<>());
    }

    public Course(String code, String name,Set<Student> students) {
        this.code = code;
        this.name = name;
        this.students = students;
    }

    public void addStudent(Student student){
        students.add(student);
    }

}
