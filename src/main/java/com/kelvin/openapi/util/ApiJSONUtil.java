package com.kelvin.openapi.util;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.*;

/**
 * 对Json的操作
 * 
 * @author Kelvin范显
 * @createDate 2017年2月7日
 */
public class ApiJSONUtil {
	/**
     * JSONObject排序
     * @param obj
     * @return
     * @createDate： 2017年2月7日
     * @2017, by Kelvin范显.
     */
    @SuppressWarnings("all")
    public static JSONObject sortJsonObject(JSONObject obj) {  
		Map map = new TreeMap();  
        Set<String> keys = obj.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {  
            String key = it.next();  
            Object value = obj.get(key);  
            if (value instanceof JSONObject) {
                map.put(key, sortJsonObject((JSONObject) JSONUtil.parse(value)));
            } else if (value instanceof JSONArray) {  
                map.put(key, sortJsonArray((JSONArray) JSONUtil.parse(value)));
            } else {  
                map.put(key, value);  
            }  
        }  
        return (JSONObject) JSONUtil.parse(map);
    } 
    
    
    /**
     *  JSONArray排序
     * @param array
     * @return
     * @createDate： 2017年2月7日
     * @2017, by Kelvin范显.
     */
    @SuppressWarnings("all")  
    public static JSONArray sortJsonArray(JSONArray array) {  
        List list = new ArrayList();  
        int size = array.size();  
        for (int i = 0; i < size; i++) {
            Object obj = array.get(i);  
            if (obj instanceof JSONObject) {  
                list.add(sortJsonObject((JSONObject) JSONUtil.parse(obj)));
            } else if (obj instanceof JSONArray) {  
                list.add(sortJsonArray((JSONArray) JSONUtil.parse(obj)));
            } else {  
                list.add(obj);  
            }  
        }  
  
        list.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));  
        return (JSONArray) JSONUtil.parse(list);
    } 
    
}
