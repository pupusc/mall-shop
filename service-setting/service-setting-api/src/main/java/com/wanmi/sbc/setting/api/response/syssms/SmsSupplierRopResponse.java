package com.wanmi.sbc.setting.api.response.syssms;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by feitingting on 2019/11/11.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSupplierRopResponse implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String account;

    /**
     * 提供商
     */
    @ApiModelProperty(value = "提供商")
    private String name;

    /**
     * 服务商站点地址(申请地址)
     */
    @ApiModelProperty(value = "服务商站点地址(申请地址)")
    private String siteAddress;

    /**
     * 短信接口地址
     */
    @ApiModelProperty(value = "短信接口地址")
    private String interfaceUrl;

    /**
     * 接口密码
     */
    @ApiModelProperty(value = "接口密码")
    private String password;

    /**
     * 报备和签名
     */
    @ApiModelProperty(value = "报备和签名")
    private String template;

    /**
     * 短信内容
     */
    @ApiModelProperty(value = "短信内容")
    private String smsContent;

    /**
     * 是否开启
     */
    @ApiModelProperty(value = "是否开启")
    private String status;


    @ApiModelProperty(value = "createTime")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
