package com.example.beanassemble;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenLoadConfig {

    @Bean
    public Dog generateDog(){
        return new Dog();
    }
}
