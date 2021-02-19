package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // 通过 bean 的 id 获取 bean
        var student123 = (Student) context.getBean("student123");
        System.out.println(student123.toString());

        var student100 = (Student) context.getBean("student100");
        System.out.println(student100.toString());

        // 通过 类 获取 bean
        var klass01 = (Klass) context.getBean(Klass.class);
        System.out.println(klass01.toString());

        // 通过 interface 和 annotation 获取 bean
        ISchool school = context.getBean(ISchool.class);
        System.out.println(school);
        System.out.println("ISchool接口的对象AOP代理后的实际类型："+school.getClass());

    }
}
