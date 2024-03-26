package com.kelvin.openapi.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.time.Instant;
import java.util.Arrays;


/**
 * 
 * ApiHelper
 * 
 * @author Kelvin范显
 * @createDate 2017年1月20日
 */
final public class ApiHelper {

    public static String sign(JSONObject params, String key) {
        JSONObject jsonObject = ApiJSONUtil.sortJsonObject(params);

        String[] names = jsonObject.keySet().toArray(new String[jsonObject.size()]);
        Arrays.sort(names);
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            sb.append(name);
            sb.append("=");
            sb.append(jsonObject.get(name));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        final String toSingStr = sb.toString().concat("#").concat(key);
//        System.out.println("toSingStr: "+ toSingStr);
        return SecureUtil.md5(toSingStr);
    }


    /**
     * 验证请求是否过期： true:有效；false:过期
     *
     * @param Unix_timeStamp Unix时间戳
     * @param limitMin  限制时间（分钟）
     * @return
     * @createDate： 2017年2月10日
     * @2017, by Kelvin范显.
     */
    public static boolean validTimeStamp(String Unix_timeStamp,long limitMin){
        long currentTime = Instant.now().toEpochMilli();
        long timeStampL = 0;
        try {
            timeStampL = Long.valueOf(Unix_timeStamp);//时间戳
        } catch (NumberFormatException e) {
            throw new RuntimeException("Not a valid Unix timestamp!");
        }

        Long diff = Math.abs((currentTime - timeStampL) / (1000 * 60));
        if(diff<limitMin){
            return true;
        }
        return  false;
    }

    public static void main(String[] args) {
        String s = "{\n" +
                "    \"a1\": [\n" +
                "        {\n" +
                "            \"A4\": 6,\n" +
                "            \"a\": 1,\n" +
                "            \"a2\": 3,\n" +
                "            \"b\": 4,\n" +
                "            \"a1\": 6,\n" +
                "            \"A\": 8,\n" +
                "            \"B\": 4\n" +
                "        },\n" +
                "        {\n" +
                "            \"a\": 1,\n" +
                "            \"a2\": 3,\n" +
                "            \"b\": 4,\n" +
                "            \"a1\": 6,\n" +
                "            \"A\": 8,\n" +
                "            \"A4\": 6,\n" +
                "            \"B\": 4\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONObject jsonObject = JSONUtil.parseObj(s);
        sign(jsonObject, "secret");
    }

}
