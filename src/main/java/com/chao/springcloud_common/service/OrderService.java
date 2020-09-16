package com.chao.springcloud_common.service;

import com.chao.springcloud_common.pojo.Order;

public interface OrderService {

    Order getOrder(String orderId);
    void addOrder(Order order);

}
