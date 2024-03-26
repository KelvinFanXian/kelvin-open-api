package com.kelvin.openapi.service;

import com.kelvin.openapi.dal.dataobject.OpenApiAccountInfo;

/**
 * @author: 范显
 * @Date: 2024/3/18 16:03
 **/
public interface OpenApiAccountInfoService {

    OpenApiAccountInfo findAccountByAppId(String appId);
}
