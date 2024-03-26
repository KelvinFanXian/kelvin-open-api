package com.kelvin.openapi.framework.web.core.interceptor;


import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.kelvin.openapi.common.exception.ServiceException;
import com.kelvin.openapi.framework.web.pojo.FixedLengthList;
import com.kelvin.openapi.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 全局接口请求频率限制
 * API接口访问上限,当访问频率或者并发量超过阈值应该禁止
 * Guava提供了限流工具类RateLimiter,该类基于令牌桶算法(Token Bucket)来完成限流
 * 创建一个稳定输出令牌的RateLimiter，保证了每个接口平均每秒不超过permitsPerSecond个请求
 *
 * @author fanxian
 * @Date 2021-5-20 10:54:30
 */
public class GlobalApiLimiterInterceptor implements HandlerInterceptor {
    private static final Logger log = getLogger(GlobalApiLimiterInterceptor.class);
    private static RateLimiter rateLimiter;
    private static Map<String, RateLimiter> apiRateLimiterMap;

    /**
     * api 访问ip记录
     */
    private Map<String, FixedLengthList<String>> apiAccessIpListMap = new HashMap<>();

    public GlobalApiLimiterInterceptor() {
        final double permitsPerSecond = 1;

        log.info("全局接口请求频率");
        rateLimiter = RateLimiter.create(permitsPerSecond);
        apiRateLimiterMap = new ConcurrentHashMap(8) {
            @Override
            public Object getOrDefault(Object key, Object defaultValue) {
                return super.getOrDefault(key, rateLimiter);
            }
        };
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String path = request.getRequestURI();
        final String remoteHost = HttpUtil.getIpAddr(request);
        final String KEY = String.join(":", remoteHost, path);

        if (!apiRateLimiterMap.containsKey(KEY)) { // API limit setting
            RateLimiter rateLimiterDefault = RateLimiter.create(Double.valueOf(1));
            apiRateLimiterMap.put(KEY, rateLimiterDefault);
        }

        // API访问IP记录
        FixedLengthList<String> apiAccessIpList = apiAccessIpListMap.getOrDefault(path, new FixedLengthList<>(200));

        RateLimiter rateLimiter = apiRateLimiterMap.get(KEY);
        if (!rateLimiter.tryAcquire(1, 3, TimeUnit.SECONDS)) {
            String doubtfulIp = apiAccessIpList.maxOccurrenceElement(); // 访问次数最多为可疑IP
            log.error("[IP:{}]URL:[{}]请求超过频率限制,建议根据系统负载和数据库压力进行适当调节", remoteHost, path);
            throw new ServiceException(400, StrUtil.format("[IP:{}]请求URL:[{}]超过频率限制", remoteHost, path));
        }

        apiAccessIpList.add(remoteHost);//添加IP访问记录
        return true;
    }


}
