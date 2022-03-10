package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.*;

@Data
public class ConsigneeReqVO implements Serializable {

    /**
     * 省
     */
    @NotNull
    private Long provinceId;

    /**
     * 市
     */
    @NotNull
    private Long cityId;

    /**
     * 区
     */
    @NotNull
    private Long areaId;
    /**
     * 街道
     */
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
    private String streetName;
}
