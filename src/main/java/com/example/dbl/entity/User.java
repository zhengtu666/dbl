package com.example.dbl.entity;

import lombok.Data;

/**
 * @author: wangzhichao
 * @Description: 用户实体类
 * @date: 2019/12/1
 */

@Data
public class User {
    //用户名
    private String name;
    //用户id
    private Long id;
    //用户身份证号
    private String IDNumber;
}
