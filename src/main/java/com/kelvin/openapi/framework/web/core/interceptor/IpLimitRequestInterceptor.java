package com.kelvin.openapi.framework.web.core.interceptor;

import com.kelvin.openapi.common.exception.ServiceException;
import com.kelvin.openapi.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * IP白名单，黑名单
 *
 * @author fanxian
 * @date 2021-5-20 15:14:59
 */
@Slf4j
public class IpLimitRequestInterceptor implements HandlerInterceptor {

    private String blacklist="192.168.98.118 ";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = HttpUtil.getIpAddr(request);
        Boolean isLimit = checkIpIsLimit(ip);
        if (isLimit) {
            log.info(String.format("ip:[%s]请求URL:[%s]，为非法请求，请检查IP限制名单", ip, request.getPathInfo()));
            throw new ServiceException(400, "拒绝访问");
        }
        return true;
    }


    /**
     * @param ipAddr
     * @return
     */
    private Boolean checkIpIsLimit(String ipAddr) {
        if (blacklist.contains(ipAddr)) {
            return true;
        }
        return false;
    }

}
