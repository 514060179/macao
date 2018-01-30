package com.yinghai.macao.common.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MapUtil {

    /**
     * 閺嶈宓�?柨顔猴拷閸婂吋鏆熼幑顔跨箻鐞涘矁绻冨锟�?     * */
    public static Map<String, Object> filterMapByKeyValue(Map<String, Object> map, String filterKey, String filterValue) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            if (!filterKey.equals(key)) {
                if (!filterValue.equals(val)) {
                    resultMap.put(key, val);
                }
            }
        }
        return resultMap;
    }

    /**Map排序，按照Key排序*/
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) { return null; }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}