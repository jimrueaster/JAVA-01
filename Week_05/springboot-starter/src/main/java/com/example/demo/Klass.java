package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Klass {

    List<Student> students;

    public void dong(){
        System.out.println(this.getStudents());
    }

}
