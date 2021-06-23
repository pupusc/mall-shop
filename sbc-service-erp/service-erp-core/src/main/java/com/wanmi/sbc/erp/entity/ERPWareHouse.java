package com.wanmi.sbc.erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: ERP仓库列表
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 22:04
 **/
@Data
public class ERPWareHouse implements Serializable {

    /**
     * 仓库代码
     */
    @JsonProperty("code")
    private String code;

    /**
     * 仓库名称
     */
    @JsonProperty("name")
    private String name;

    /**
     * 地址
     */
    @JsonProperty("address")
    private String address;

    /**
     * 备注
     */
    @JsonProperty("note")
    private String note;

    /**
     * 仓库类型
     */
    @JsonProperty("type_name")
    private String typeName;


    /**
     * 地区信息
     */
    @JsonProperty("area_name")
    private String areaName;
}
