package com.wanmi.sbc.goods.collect.json;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    public static void main(String args[]) {
        HashMap<Integer, String> hmap= new HashMap<Integer, String>();
        hmap.put(2, "Anil");
        hmap.put(44, "Ajit");
        hmap.put(1, "Brad");
        hmap.put(4, "Sachin");
        hmap.put(88, "XYZ");

        Map map= Collections.synchronizedMap(hmap);
        Set set = map.entrySet();
        synchronized(map){
            Iterator i = set.iterator();
            // Display elements
            while(i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
                System.out.print(me.getKey() + ": ");
                System.out.println(me.getValue());
                if(me.getKey().equals(1)){
                    map.put(3,"3333");
                }
            }
        }
        System.out.println(map);
    }

    public static void main2(String[] args) {
        Map<String, Object> synchronizedHashMap =
                Collections.synchronizedMap(new HashMap<String, Object>());

        synchronizedHashMap.put("name","lisi");
        synchronizedHashMap.put("age",18);
        synchronizedHashMap.put("xx","xx");
        Iterator iterator1=  synchronizedHashMap.entrySet().iterator();
        while(iterator1.hasNext()){
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iterator1.next();
            if(entry.getKey().equals("age")){
                synchronizedHashMap.put("yy","yy");
            }

        }

        System.out.println(synchronizedHashMap);
    }

    public static void main3(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("Java入门教程", "http://c.biancheng.net/java/");
        map.put("C语言入门教程", "http://c.biancheng.net/c/");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + "：" + mapValue);
            if(mapKey.equals("Java入门教程")){
                map.put("xxx人们","www.baidu.com");
            }
        }
        System.out.println(map);
    }



}
