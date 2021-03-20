package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.IOrderService;

public class OrderServiceImpl implements IOrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
