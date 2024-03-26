package com.kelvin.openapi.controller;

import cn.hutool.core.util.StrUtil;
import com.kelvin.openapi.common.pojo.CommonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kelvin.openapi.common.pojo.CommonResult.success;

/**
 * @author: 范显
 * @Date: 2024/3/15 15:58
 **/
@RestController
public class DefaultController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping
    public CommonResult<String> hello() {
        return success(StrUtil.format("Welcome to {}", appName));
    }

    @GetMapping("/now")
    public CommonResult<Long> now() {
        return success(System.currentTimeMillis());
    }
}
