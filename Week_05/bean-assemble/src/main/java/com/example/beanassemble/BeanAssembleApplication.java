package com.example.beanassemble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = OpenLoadConfig.class)
public class BeanAssembleApplication {

    @Autowired
    private Dog dog;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 通过 bean 的 id 获取 bean
        var animal123 = (Animal) context.getBean("animal123");
        System.out.println(animal123);

        // 获取 bean 中的 Dog dog
        animal123.getDog().hello();

    }

}
