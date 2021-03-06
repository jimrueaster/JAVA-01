package com.example.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrdersTest {

    @Autowired
    private MembershipDomSvc membershipDomSvc;

    @Autowired
    private MembershipMapper membershipMapper;

    private void truncateOrders() {
        System.out.println("TRUNCATE `orders`!!!!");
        membershipMapper.truncateOrders();
    }

    @BeforeAll
    void beforeAll() {
        truncateOrders();
    }

    @Test
    void createOrders() {

        final long start = System.currentTimeMillis();
        membershipDomSvc.createOrders(200000);
        final long end = System.currentTimeMillis();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.printf("Total time: %d ms", end - start);
        System.out.println();
        System.out.println();
        System.out.println();
    }
}
