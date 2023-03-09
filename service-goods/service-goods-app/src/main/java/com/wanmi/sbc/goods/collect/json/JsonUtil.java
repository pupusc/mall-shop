package com.wanmi.sbc.goods.collect.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * JsonUtil
 * @author chenzhen
 */
@Slf4j
public class JsonUtil {

    //递归查询,查询所有为key的value
    public static List<String> findJsonGetKey(String fullResponseJson, String key) {

        List<String> list = new ArrayList<>();

        findValueObjectGetKey(JSONObject.parseObject(fullResponseJson), key, list);

        return list;
    }

    //递归查询,查询所有为key的value
    public static List<Map> findJsonGetList(JSONObject jsonObject, String key) {

        List<Map> list = new ArrayList<>();

        findValueObjectGetList(jsonObject, key, list);

        return list;
    }


    //丰富
    public static void richJson(JSONObject jsonObject, String key, Map richMap) {

        List list = findJsonGetList(jsonObject,key);
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            String value = String.valueOf(map.get(key));

            Map findMap = (Map)richMap.get(value);
            if(findMap != null){

                map.put("sale_price",findMap.get("sale_price"));
                map.put("line_price",findMap.get("line_price"));
                map.put("goods_img",findMap.get("goods_img"));

            }
        }

    }

    /**
     * 从json中查找对象
     *
     * @param fullResponse json对象
     * @param key          json key
     */
    private static void findValueObjectGetKey(JSONObject fullResponse, String key,List list) {

        if (fullResponse == null) {
            return;
        }
        fullResponse.keySet().forEach(keyStr -> {
            Object keyvalue = fullResponse.get((String) keyStr);
            if (keyvalue instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) keyvalue).size(); i++) {
                    Object obj = ((JSONArray) keyvalue).get(i);
                    if (obj instanceof JSONObject) {
                        findValueObjectGetKey(((JSONObject) obj), key,list);
                    }
                }
            } else if (keyvalue instanceof JSONObject) {
                findValueObjectGetKey((JSONObject) keyvalue,key, list);
            } else {
                if (key.equals(keyStr)) {
                    list.add(keyvalue);
                }
            }
        });
    }


    /**
     * 从json中查找对象
     *
     * @param fullResponse json对象
     * @param key          json key
     */
    private static void findValueObjectGetList(JSONObject fullResponse, String key,List list) {

        if (fullResponse == null) {
            return;
        }
        fullResponse.keySet().forEach(keyStr -> {
            Object keyvalue = fullResponse.get((String) keyStr);
            if (keyvalue instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) keyvalue).size(); i++) {
                    Object obj = ((JSONArray) keyvalue).get(i);
                    if (obj instanceof JSONObject) {
                        findValueObjectGetList(((JSONObject) obj), key,list);
                    }
                }
            } else if (keyvalue instanceof JSONObject) {
                findValueObjectGetList((JSONObject) keyvalue,key, list);
            } else {
                if (key.equals(keyStr)) {
                    list.add(fullResponse);
                }
            }
        });
    }

    public static void main(String[] args) {
        String json = "{    \"isBook\": \"yes\", \"spu_id\": \"333\",   \"list\": {        \"tags\": [            {                \"spu_id\": \"111\"            }        ]    },    \"tags\": [        {            \"spu_id\": \"222\",            \"order_type\": 10        }    ]}";
        JSONObject jsonObject = JSONObject.parseObject(json);

        //输出测试
        String jsonString1 = JSONObject.toJSONString(jsonObject);
        System.out.println(jsonString1);

        List list = JsonUtil.findJsonGetKey(json,"spu_id");
        System.out.println(list);

        /**begin 封装goods**/
        Map goodMap1 = new HashMap();
        goodMap1.put("sale_price","100");
        goodMap1.put("line_price","50");
        goodMap1.put("goods_img","http://xxx1.img");


        Map goodMap2 = new HashMap();
        goodMap2.put("sale_price","100");
        goodMap2.put("line_price","50");
        goodMap2.put("goods_img","http://xxx2.img");

        Map goodMap3 = new HashMap();
        goodMap3.put("sale_price","100");
        goodMap3.put("line_price","50");
        goodMap3.put("goods_img","http://xxx3.img");


        Map map = new HashMap();
        map.put("111",goodMap1);
        map.put("222",goodMap2);
        map.put("333",goodMap2);
        /**end   封装goods**/
        System.out.println(map);

        JsonUtil.richJson(jsonObject,"spu_id",map);

        String jsonString2 = JSONObject.toJSONString(jsonObject);
        System.out.println(jsonString2);
    }

}

