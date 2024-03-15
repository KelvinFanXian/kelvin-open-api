package com.kelvin.openapi.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 范显
 * @Date: 2024/3/15 15:58
 **/
@RestController
public class DefaultController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping
    public String hello() {
        return StrUtil.format("Welcome to {}", appName);
    }

    @GetMapping("/now")
    public Long now() {
        return System.currentTimeMillis();
    }
}
