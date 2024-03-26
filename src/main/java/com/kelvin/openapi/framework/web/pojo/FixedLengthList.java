package com.kelvin.openapi.framework.web.pojo;

/**
 * 定长数组，自动移除较老数据
 * @author: 范显
 * @Date: 2024/3/25 10:36
 **/

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FixedLengthList<E> {
    private final int maxSize;
    private final LinkedList<E> list;

    public FixedLengthList(int size) {
        this.maxSize = size;
        this.list = new LinkedList<>();
    }

    public void add(E element) {
        if (list.size() >= maxSize) {
            list.poll(); // 移除队列头部元素
        }
        list.offer(element); // 在队列尾部添加元素
    }

    public E get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }


    public E maxOccurrenceElement() {
        E maxOccurrenceElement = null;
        int maxOccurrenceCount = 0;
        Map<E, Integer> eOccurrenceMap = new HashMap<>();
        for (E element : list) {
            int occurrence = eOccurrenceMap.getOrDefault(element, 0);
            eOccurrenceMap.put(element, ++occurrence);
            if (occurrence > maxOccurrenceCount) {
                maxOccurrenceCount = occurrence;
                maxOccurrenceElement = element;
            }
        }
        return maxOccurrenceElement;
    }
}
