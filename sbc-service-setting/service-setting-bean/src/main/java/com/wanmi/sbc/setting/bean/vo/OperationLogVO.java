package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志信息
 * Created by daiyitian on 2017/4/26.
 */
@Data
public class OperationLogVO implements Serializable {

    private static final long serialVersionUID = -4545620276656668361L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private String employeeId;

    /**
     * 门店Id
     */
    @ApiModelProperty(value = "门店Id")
    private Long storeId;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    private Long companyInfoId;

    /**
     * 操作人账号
     */
    @ApiModelProperty(value = "操作人账号")
    private String opAccount;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String opName;

    /**
     * 操作人角色
     */
    @ApiModelProperty(value = "操作人角色")
    private String opRoleName;

    /**
     * 操作模块
     */
    @ApiModelProperty(value = "操作模块")
    private String opModule;

    /**
     * 操作关键字
     */
    @ApiModelProperty(value = "操作关键字")
    private String opCode;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String opContext;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime opTime;

    /**
     * 操作IP
     */
    @ApiModelProperty(value = "操作IP")
    private String opIp;

    /**
     * 操作MAC地址
     */
    @ApiModelProperty(value = "操作MAC地址")
    private String opMac;

    /**
     * 运营商
     */
    @ApiModelProperty(value = "运营商")
    private String opIsp;

    /**
     * 所在国家
     */
    @ApiModelProperty(value = "所在国家")
    private String opCountry;

    /**
     * 所在省份
     */
    @ApiModelProperty(value = "所在省份")
    private String opProvince;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private String opCity;
}
