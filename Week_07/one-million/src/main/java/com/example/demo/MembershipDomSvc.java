package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j

@Service
public class MembershipDomSvc {
    private final String MOCK_CREATOR_ID = "UI-20200315-mkcreatr";
    private final String MOCK_UPDATER_ID = "UI-20200315-mkupdatr";

    @Autowired
    private MembershipRepo membershipRepo;

    @Autowired
    private MembershipIdentityObjectGenerator membershipIdentityObjectGenerator;

    /*
    * 生成批量的 INSERT 语句，每秒插入 5w条
    * */
    public void createOrders2(int ordersCnt){

        var rnd = new Random();

        List<Orders> ordersList = new ArrayList<>();

        for (var i = 0 ; i < ordersCnt; i++){
            ordersList.add(generateOrders(rnd));
        }

        // 9 fields
        var max = 4*1024*1024;
        var orderCntPerPartition = max / 9;

        var listsOfOrdersList = Lists.partition(ordersList, orderCntPerPartition);

        listsOfOrdersList.forEach((ol) -> {
            membershipRepo.addOrders(ol);
        });

    }

    /*
    * 每秒插入 1w 条
    * */
    public void createOrders(int ordersCnt){

        var rnd = new Random();

        List<Orders> ordersList = new ArrayList<>();

        for (var i = 0 ; i < ordersCnt; i++){
            ordersList.add(generateOrders(rnd));
        }

        // 9 fields
        var max = 4*1024*1024;
        var orderCntPerPartitionDbLimitation = max / 9;

        var threadCnt = 32;
        var threadLimitation = ordersCnt / threadCnt;

        var partitionLimit = Math.min(orderCntPerPartitionDbLimitation, threadLimitation);

        var listsOfOrdersList = Lists.partition(ordersList, partitionLimit);

        System.out.println("partition length: "+listsOfOrdersList.size());
        listsOfOrdersList.stream().parallel().peek((ol) -> {
            membershipRepo.addOrders(ol);
        }).collect(Collectors.toList());

    }

    private Orders generateOrders(Random rnd) {
        var ordersId = "OI-" + idSuffix();
        var commodityId = "CI-" + idSuffix();
        var buyerId = "BI-" + idSuffix();
        var commodityQuantity = rnd.nextInt(999999);

        LocalDateTime currentTime = LocalDateTime.now();
        MembershipUser creator = Membership.newMembershipUser(MembershipUserId.fromString(MOCK_CREATOR_ID));

        var order = Orders.builder()
                .ordersId(ordersId)
                .commodityId(commodityId)
                .buyerId(buyerId)
                .commodityQuantity(commodityQuantity)
                .createBy(creator)
                .createTime(currentTime)
                .updateBy(creator)
                .updateTime(currentTime)
                .status(ActivationStatus.ACTIVE)
                .build();
        return order;
    }

    @NotNull
    private String idSuffix() {
        String currentDate = BerylDateTime.currentDateTime("yyyyMMdd");
        var simpleUuid = SimpleUuid.getUuid();

        final String s = currentDate +"-"+ simpleUuid;
        return s;
    }


}