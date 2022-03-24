package com.fullstack.completeFullStackJavaReact.Student;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");


    private String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
