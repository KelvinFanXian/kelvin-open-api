package com.kelvin.openapi.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 对接方信息
 * @author: 范显
 * @Date: 2024/3/18 14:01
 **/
@Data
@TableName("open_api_account_info")
public class OpenApiAccountInfo {

    @TableId
    private String appId;
    private String appSecret;
    private String systemName;
    private String createTime;

    private Boolean isLimitIp;
    private String ipWhite;
    private String apiPermit;
    private Integer status;
}
