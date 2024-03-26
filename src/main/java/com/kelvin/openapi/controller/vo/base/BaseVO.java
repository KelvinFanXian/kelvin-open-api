package com.kelvin.openapi.controller.vo.base;

/**
 * 请求体公共字段
 * @author: 范显
 * @Date: 2024/3/19 14:18
 **/

public class BaseVO {
    private String acs_appid;
    private Long acs_timestamp;
    private String acs_sign;

    public String getAcs_appid() {
        return acs_appid;
    }

    public void setAcs_appid(String acs_appid) {
        this.acs_appid = acs_appid;
    }

    public Long getAcs_timestamp() {
        return acs_timestamp;
    }

    public void setAcs_timestamp(Long acs_timestamp) {
        this.acs_timestamp = acs_timestamp;
    }

    public String getAcs_sign() {
        return acs_sign;
    }

    public void setAcs_sign(String acs_sign) {
        this.acs_sign = acs_sign;
    }
}
