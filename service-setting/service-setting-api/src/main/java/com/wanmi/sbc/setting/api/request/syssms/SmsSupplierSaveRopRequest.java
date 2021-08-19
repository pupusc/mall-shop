package com.wanmi.sbc.setting.api.request.syssms;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.SmsSupplierStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * Created by feitingting on 2019/11/11.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSupplierSaveRopRequest extends BaseQueryRequest {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @Length(max=45)
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
    @Length(max=45)
    private String siteAddress;

    /**
     * 短信接口地址
     */
    @Length(max=45)
    @ApiModelProperty(value = "短信接口地址")
    private String interfaceUrl;

    /**
     * 接口密码
     */
    @Length(max=45)
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
    @Length(max=45)
    @ApiModelProperty(value = "短信内容")
    private String smsContent;

    /**
     * 是否开启
     */
    @ApiModelProperty(value = "是否开启")
    private SmsSupplierStatus status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime modifyTime;
}
