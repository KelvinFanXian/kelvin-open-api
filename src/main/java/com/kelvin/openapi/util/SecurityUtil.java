package com.kelvin.openapi.util;

import cn.hutool.core.util.StrUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author: 范显
 * @Date: 2024/3/18 13:58
 **/
public class SecurityUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 将body转换为Map
     *
     * @param body
     * @return
     * @throws Exception
     */
    public static Map<String, String> bodyToMap(String body) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }

//    /**
//     * 判断签名是否正确
//     *
//     * @param xmlStr XML格式数据
//     * @param key    API密钥
//     * @return 签名是否正确
//     * @throws Exception
//     */
//    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
//        return false;
//    }


    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String md5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] array = md5.digest(data.getBytes(DEFAULT_CHARSET));
        return new String(array, DEFAULT_CHARSET);
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes(DEFAULT_CHARSET));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 构造签名
     *
     * @param params
     * @param appId
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static String createSign(Map<String, String> params, String appId, String secretKey) throws Exception {
        Map<String, String> sortParams = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        sortParams.forEach((key, value) -> {
            if (StrUtil.isNotBlank(value)) {
                sb.append("&").append(key).append("=").append(value);
            }
        });
        String stringA = sb.toString().replaceFirst("&", "");
        String stringSignTemp = appId + stringA + "&" + "secretKey=" + secretKey;
        return SecurityUtil.md5(stringSignTemp);
    }

    /**
     * 获取当前时间戳，单位秒
     *
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     *
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 判断是否为基本类型
     *
     * @param obj
     * @return
     */
    private boolean isPrimitive(Object obj) {
        try {
            return obj.getClass().getDeclaredField("name").getType().isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}
