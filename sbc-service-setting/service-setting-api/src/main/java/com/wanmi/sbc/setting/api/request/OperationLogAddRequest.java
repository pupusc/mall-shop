package com.wanmi.sbc.setting.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志信息
 * Created by daiyitian on 2017/4/26.
 */
@ApiModel
@Data
public class OperationLogAddRequest extends SettingBaseRequest {

    private static final long serialVersionUID = -6532723368017943964L;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private String employeeId;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 公司Id
     */
    @ApiModelProperty(value = "公司Id")
    private Long companyInfoId;

    /**
     * 操作人账号
     */
    @ApiModelProperty(value = "操作人账号")
    private String opAccount;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
     * 操作关类型
     */
    @ApiModelProperty(value = "操作关类型")
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
    @ApiModelProperty(value = "操作MAC地址")
    private String opIsp;

    /**
     * 第三方ID
     */
    @ApiModelProperty(value = "第三方ID")
    private String thirdId;

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
