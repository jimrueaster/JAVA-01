package com.example.beanassemble;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Animal {
    private int id;
    private String name;
    private Dog dog;
}
