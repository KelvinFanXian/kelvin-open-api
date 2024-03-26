package com.kelvin.openapi.controller;

import cn.hutool.core.util.StrUtil;
import com.kelvin.openapi.annotation.OpenAPISign;
import com.kelvin.openapi.common.pojo.CommonResult;
import com.kelvin.openapi.controller.vo.TestVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kelvin.openapi.common.pojo.CommonResult.success;

/**
 * @author: 范显
 * @Date: 2024/3/19 10:31
 **/

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/test")
    @OpenAPISign(rateLimitCount = 1)
    public CommonResult test(@RequestBody TestVO testVO) {
        return success(StrUtil.format("test, {}", testVO.getName()));
    }
}
