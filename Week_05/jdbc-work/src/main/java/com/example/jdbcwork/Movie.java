package com.example.jdbcwork;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class Movie {

    private Integer id;
    private  String name;
}
