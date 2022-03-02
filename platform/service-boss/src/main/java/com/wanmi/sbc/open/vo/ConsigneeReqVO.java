package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.*;

@Data
public class ConsigneeReqVO implements Serializable {

    /**
     * 省
     */
    @NotBlank
    private Long provinceId;

    /**
     * 市
     */
    @NotBlank
    private Long cityId;

    /**
     * 区
     */
    @NotBlank
    private Long areaId;
    /**
     * 街道
     */
    @NotBlank
    private String streetId;

    /**
     * 详细地址
     */
    @NotBlank
    private String address;

    /**
     * 收货人名称
     */
    @NotBlank
    private String name;
    /**
     * 收货人电话
     */
    @NotBlank
    private String phone;

    //-------------------------以下部分前端没有传输入参---------------------------------
    /***
     * 详细地址(包含省市区）
     */
    @NotBlank
    private String detailAddress;

    /**
     * 省名称
     */
    @NotBlank
    private String provinceName;

    /**
     * 市名称
     */
    @NotBlank
    private String cityName;

    /**
     * 区名称
     */
    @NotBlank
    private String areaName;
    /**
     * 街道
     */
    @NotBlank
    private String streetName;
}
