package com.chao.springcloud_common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String id;
    private User user;
    private List<Item> items;
}
