package com.example.dbl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuhuan
 * @Description: 首页跳转
 * @date: 2019/12/1
 */
@RestController
public class IndexController {

    @RequestMapping("/test")
    public String test(){
        return "ok";
    }
}
