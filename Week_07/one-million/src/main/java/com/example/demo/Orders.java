package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
public class Orders extends UpdatableEntityBase {

    private final String ordersId;

    private String commodityId;

    private String buyerId;

    private Integer commodityQuantity;

    private ActivationStatus status;

    public Orders(String aOrdersId, String aCommodityId,
                  String aBuyerId, Integer aCommodityQuantity,
                  ActivationStatus aStatus,
                  LocalDateTime createTime, MembershipUser createBy, LocalDateTime updateTime,
                  MembershipUser updateBy, RowVersion rowVersion
    ) {
        super(createTime, createBy, updateTime, updateBy, rowVersion);
        ordersId = aOrdersId;
        commodityId = aCommodityId;
        buyerId = aBuyerId;
        commodityQuantity = aCommodityQuantity;
        status = aStatus;
    }
}
