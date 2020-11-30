package com.geominfo.mlsql.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 版本号排序工具类
 * @author: ryan(丁帅波)
 * @create: 2020-11-26 10:38
 * @version: 1.0.0
 */
public class VersionSort implements Comparator<Map<String, Object>> {
    /**
     * 排序用到的key
     */
    private String key;

    public VersionSort(String key) {
        this.key = key;
    }

    @Override
    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        // 获取比较的字符串
        String compareValue1 = (String) o1.get(key);
        String compareValue2 = (String) o2.get(key);
        String[] valueSplit1 = compareValue1.split("[.]");
        String[] valueSplit2 = compareValue2.split("[.]");
        int minLength = valueSplit1.length;
        if (minLength > valueSplit2.length) {
            minLength = valueSplit2.length;
        }
        for (int i = 0; i < minLength; i++) {
            int value1 = Integer.parseInt(valueSplit1[i]);
            int value2 = Integer.parseInt(valueSplit2[i]);
            if (value1 > value2) {
                return 1;
            } else if (value1 < value2) {
                return -1;
            }
        }
        return valueSplit1.length - valueSplit2.length;
    }
}
