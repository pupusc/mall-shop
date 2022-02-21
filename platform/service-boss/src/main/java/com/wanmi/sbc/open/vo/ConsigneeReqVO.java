package com.wanmi.sbc.open.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

/**
 * 收货人信息
 * Created by jinwei on 19/03/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class ConsigneeReqVO implements Serializable {

    /**
     * 省
     */
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 区
     */
    private Long areaId;
    /**
     * 街道
     */
    private String streetId;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 收货人名称
     */
    private String name;
    /**
     * 收货人电话
     */
    private String phone;

    //-------------------------以下部分前端没有传输入参---------------------------------
    /***
     * 详细地址(包含省市区）
     */
    private String detailAddress;

    /**
     * 省名称（老订单使用）
     */
    private String provinceName;

    /**
     * 市名称（老订单使用）
     */
    private String cityName;

    /**
     * 区名称（老订单使用）
     */
    private String areaName;
    /**
     * 街道
     */
    private String streetName;
}
