package com.xjr.httpget.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestOkHttp {

    @Test
    void test_get() throws IOException {
        var okhttp = new OkHttp();
        var result = okhttp.run("http://localhost:8801/");
        assertEquals("hello,nio1", result);
    }
}
