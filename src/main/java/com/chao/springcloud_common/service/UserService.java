package com.chao.springcloud_common.service;

import com.chao.springcloud_common.pojo.Item;
import com.chao.springcloud_common.pojo.User;

public interface UserService {

    User getUser(Integer userId);
    //增加积分
    void addScore(Integer userId, Integer score);
}
