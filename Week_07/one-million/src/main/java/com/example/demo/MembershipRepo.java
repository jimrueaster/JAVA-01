package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MembershipRepo {

    @Autowired
    private MembershipMapper membershipMapper;




    public void addOrders(List<Orders> aOrdersList){
        membershipMapper.addOrders(aOrdersList);
    }
}
