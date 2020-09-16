package com.chao.springcloud_common.service;

import com.chao.springcloud_common.pojo.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItems(String orderId);
    //减库存
    void decreaseNumbers(List<Item> items);

}
