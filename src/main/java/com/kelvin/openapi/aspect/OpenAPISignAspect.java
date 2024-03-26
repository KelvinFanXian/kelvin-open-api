package com.kelvin.openapi.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kelvin.openapi.annotation.OpenAPISign;
import com.kelvin.openapi.common.exception.Assert;
import com.kelvin.openapi.common.exception.ServiceException;
import com.kelvin.openapi.controller.vo.base.BaseVO;
import com.kelvin.openapi.dal.dataobject.OpenApiAccountInfo;
import com.kelvin.openapi.service.OpenApiAccountInfoService;
import com.kelvin.openapi.util.ApiHelper;
import com.kelvin.openapi.util.HttpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * TODO:校验MD5码
 *
 * @author Kelvin范显
 * @createDate 2017年1月19日
 */
@Aspect
@Component
@Slf4j
public class OpenAPISignAspect {

    @Resource
    OpenApiAccountInfoService openApiAccountInfoService;
    @Resource
    RedisTemplate redisTemplate;


    /**
     * 验证MD5校验码是否一致
     */
    @Around("execution(* *.*(..)) && @annotation(com.kelvin.openapi.annotation.OpenAPISign)")
    public Object checkSign(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String apiPath = request.getRequestURI();
        String ipAddr = HttpUtil.getIpAddr(request);
        ipLimitCheck(ipAddr);

        // Method annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OpenAPISign sign = method.getAnnotation(OpenAPISign.class);

        // 获得传入的参数
        Object[] args = joinPoint.getArgs();
        Assert.isTrue(args[0] instanceof BaseVO, "API编写错误，1必须Post方法 2参数需继承BaseVO对象");
        JSONObject requestBody = JSONUtil.parseObj(args[0]);

        // 方法对象
        final String acs_sign = requestBody.getStr("acs_sign");
        final String acs_appid = requestBody.getStr("acs_appid");
        final String acs_timestamp = requestBody.getStr("acs_timestamp");

        // 必填
        Assert.notBlank(acs_sign, "acs_sign 不能为空");
        Assert.notBlank(acs_appid, "acs_appid 不能为空");
        Assert.notBlank(acs_timestamp, "acs_timestamp 不能为空");

        // 验证appid是否存在
        OpenApiAccountInfo appAccountInfo = openApiAccountInfoService.findAccountByAppId(acs_appid);
        Assert.notNull(appAccountInfo, "系统未找到acs_appid={}", acs_appid);

        // app调用API检查许可
        appPermitCheck(apiPath, ipAddr, appAccountInfo);
        appCallApiLimit(apiPath, sign, appAccountInfo, ipAddr);

        //2.验证时间戳，判断请求是否过期
        Assert.isTrue(ApiHelper.validTimeStamp(acs_timestamp,5),"请求已过期");

        // 生成MD5校验码
        requestBody.remove("acs_sign");
        final String md5Sign = ApiHelper.sign(requestBody, appAccountInfo.getAppSecret());
        //3.验证签名， 判断MD5校验码是否一致
        if (!acs_sign.equals(md5Sign)) {
            log.error("\n\n sign不匹配：\n\t client_ip--{}\n\t call_method--{}\n\t input_params--{}\n\t client_sign--{}\n\t server_sign--{}\n\n",
                    new Object[]{HttpUtil.getIpAddr(request),joinPoint.getSignature().getName(),requestBody,acs_sign,md5Sign});
            throw new ServiceException(400, "参数签名错误");
        }
        return joinPoint.proceed(args);
    }


    /**
     * ip限制检查
     * @param ipAddr
     */
    final private String FORBIDDEN_IP_CALL_OPEN_API = "FORBIDDEN_IP_CALL_OPEN_API#";
    private void ipLimitCheck(String ipAddr) {
        Object reason = redisTemplate.opsForValue().get(FORBIDDEN_IP_CALL_OPEN_API.concat(ipAddr));
        if (reason != null) {
            throw new ServiceException(400, "ip已被禁止调用API，原因：" + reason);
        }
    }
    private void limitIp(String ipAddr, String reason) {
        redisTemplate.opsForValue().set(FORBIDDEN_IP_CALL_OPEN_API.concat(ipAddr), reason, 1, TimeUnit.MINUTES);
    }

    /**
     * 调用权限检查(接口授权，IP白名单)
     *
     * @param apiPath
     * @param ipAddr
     * @param appAccountInfo
     */
    private void appPermitCheck(String apiPath, String ipAddr, OpenApiAccountInfo appAccountInfo) {
        Assert.isTrue(appAccountInfo.getApiPermit()!=null&&appAccountInfo.getApiPermit().contains(apiPath),
                "当前app[{}]无权访问API[{}]", appAccountInfo.getAppId(), apiPath);
        Assert.isTrue(appAccountInfo.getIpWhite()!=null&&appAccountInfo.getIpWhite().contains(ipAddr),
                "当前app[{}]的IP白名单不包含[{}]", appAccountInfo.getAppId(), ipAddr);
    }



    /**
     * app访问频率限制
     * @param sign
     * @param appAccountInfo
     */
    private Map<String, Long> requestCountMap = new ConcurrentHashMap<>();
    private Map<String, Long> requestTimeMap = new ConcurrentHashMap<>();
    private void appCallApiLimit(String apiPath, OpenAPISign sign, OpenApiAccountInfo appAccountInfo, String ipAddr) {
        final String appId = appAccountInfo.getAppId();
        final String key = StrUtil.join("#", appId, ipAddr, apiPath);

        long now = System.currentTimeMillis();
        if(requestCountMap.containsKey(key)){
            long last = requestTimeMap.get(key); // 上次访问时间
            if(now - last < sign.rateLimitTimeWindow()){
                if(requestCountMap.get(key) >= sign.rateLimitCount()){
                    String errMsg = StrUtil.format("[appId:{}][IP:{}]请求URL:[{}]超过频率限制", appId, ipAddr, apiPath);
                    limitIp(ipAddr, errMsg);
                    throw new ServiceException(400, errMsg);
                }
                requestCountMap.put(key, requestCountMap.get(key) + 1);
            }else{
                requestTimeMap.put(key, now);
            }
        }else{
            requestTimeMap.put(key, now);
            requestCountMap.put(key, 1L);
        }
    }
}
