package com.example.demo;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MembershipMapper {

    @Insert("<script> "
            + " INSERT INTO orders(orders_id, commodity_id, buyer_id, commodity_quantity, "
            + " status, create_by, create_time, update_by, update_time) "
            + " VALUES "
            + " <foreach collection='ordersList' item='orders' separator=',' >"
            + "     (#{orders.ordersId}, #{orders.commodityId}, #{orders.buyerId}, #{orders.commodityQuantity}, "
            + "         #{orders.status}, #{orders.createBy}, #{orders.createTime}, #{orders.updateBy}, "
            + "         #{orders.updateTime})"
            + " </foreach>"
            + "</script>")
    @Options(useGeneratedKeys = true)
    void addOrders(@Param("ordersList") List<Orders> ordersList);


    @Update("TRUNCATE TABLE orders")
    void truncateOrders();
}
