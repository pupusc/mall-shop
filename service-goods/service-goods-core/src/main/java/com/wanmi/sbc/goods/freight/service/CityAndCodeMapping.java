package com.wanmi.sbc.goods.freight.service;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CityAndCodeMapping {

    //key:省  value：下属的市名字及code
    public static Map<String, List<AreaAndCode>> levelMap;
    //key: 名字 value：code
    public static Map<String, String> mapping;
    //key：市名  value：省名
    public static Map<String, String> cityProvince;
    static {
        try {
            levelMap = new HashMap<>();
            mapping = new HashMap<>();
            cityProvince = new HashMap<>();
            Map<String, String> pcodeName = new HashMap<>();
            InputStream p = CityAndCodeMapping.class.getResourceAsStream("/provinces.json");
            String pstr = IOUtils.toString(p, "utf-8");
            List<AreaAndCode> pList = JSONArray.parseArray(pstr, AreaAndCode.class);
            pList.forEach(i -> pcodeName.put(i.getCode(), i.getName()));

            InputStream c = CityAndCodeMapping.class.getResourceAsStream("/cities.json");
            String cstr = IOUtils.toString(c, "utf-8");
            List<AreaAndCode> cList = JSONArray.parseArray(cstr, AreaAndCode.class);
            cList.forEach(i -> {
                mapping.put(i.getName(), i.getCode());
                cityProvince.put(i.getName(), pcodeName.get(i.getParent_code()));
            });
            pList.forEach(i -> mapping.put(i.getName(), i.getCode()));

            Map<String, List<AreaAndCode>> collect = cList.stream().collect(Collectors.groupingBy(AreaAndCode::getParent_code));

            for (AreaAndCode areaAndCode : pList) {
                List<AreaAndCode> areaAndCodes = collect.get(areaAndCode.getCode());
                levelMap.put(areaAndCode.getName(), areaAndCodes);
            }

        } catch (IOException e) {
            log.error("导入不配送地区错误", e);
        }
    }

    public static Integer getCityCount(String provinceName) {
        List<AreaAndCode> areaAndCodes = levelMap.get(provinceName);
        if(areaAndCodes == null) return null;
        return areaAndCodes.size();
    }

    public static List<AreaAndCode> getCitys(String provinceName) {
        return levelMap.get(provinceName);
    }

    public static String getProvince(String cityName){
        return cityProvince.get(cityName);
    }

    public static String getCode(String areaName) {
        return mapping.get(areaName);
    }

    @Data
    public static class AreaAndCode {

        public String name;
        public String code;
        public String parent_code;

    }
}