package com.kelvin.openapi.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kelvin.openapi.dal.dataobject.OpenApiAccountInfo;
import com.kelvin.openapi.dal.postgre.OpenApiAccountInfoMapper;
import com.kelvin.openapi.service.OpenApiAccountInfoService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: 范显
 * @Date: 2024/3/18 16:07
 **/
@Service
public class OpenApiAccountInfoServiceImpl implements OpenApiAccountInfoService {

    @Resource
    OpenApiAccountInfoMapper mapper;

    private LoadingCache<String, OpenApiAccountInfo> accountMemo;

    @PostConstruct
    void init() {
        accountMemo = CacheBuilder.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .build(CacheLoader.from(mapper::selectByAppId));
    }

    @Override
    public OpenApiAccountInfo findAccountByAppId(String appId) {
        return accountMemo.getUnchecked(appId);
    }
}
