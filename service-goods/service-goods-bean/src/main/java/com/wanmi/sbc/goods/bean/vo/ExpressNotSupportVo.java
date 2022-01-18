package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ExpressNotSupportVo {

    private Long id;

    //二级供应商id
    private Long supplierId;

    private Map<String, List<String>> areas;

//    //不支持配送地区
//    private String provinceId;
//
//    private String provinceName;
//
//    //不支持配送地区
//    private String cityId;
//
//    //不支持配送地区
//    private String cityName;
}
