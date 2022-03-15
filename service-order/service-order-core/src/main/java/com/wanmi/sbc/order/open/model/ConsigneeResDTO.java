package com.wanmi.sbc.open.vo;

import lombok.Data;

import java.io.*;
import java.time.LocalDateTime;

/**
 * 收货人信息
 */
@Data
public class ConsigneeResDTO implements Serializable {
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
     * 详细地址
     */
    private String address;

    /***
     * 详细地址(包含省市区）
     */
    private String detailAddress;

    /**
     * 收货人名称
     */
    private String name;

    /**
     * 收货人电话
     */
    private String phone;

    /**
     * 期望收货时间
     */
    private LocalDateTime expectTime;

    /**
     * 收货地址修改时间
     */
    private String updateTime;

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
}
