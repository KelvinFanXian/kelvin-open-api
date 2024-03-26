package com.kelvin.openapi.dal.postgre;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kelvin.openapi.dal.dataobject.OpenApiAccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: 范显
 * @Date: 2024/3/18 15:22
 **/
@Mapper
public interface OpenApiAccountInfoMapper extends BaseMapper<OpenApiAccountInfo> {
    OpenApiAccountInfo selectByAppId(@Param("appId") String appId);

    String test();
}
