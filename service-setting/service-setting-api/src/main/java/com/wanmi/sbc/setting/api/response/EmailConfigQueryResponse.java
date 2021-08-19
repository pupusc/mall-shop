package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.enums.EmailStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户财务邮箱response
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfigQueryResponse {
    /**
     * 邮箱配置Id
     */
    @ApiModelProperty(value = "邮箱配置Id")
    private String emailConfigId;

    /**
     * 发信人邮箱地址
     */
    @ApiModelProperty(value = "发信人邮箱地址")
    private String fromEmailAddress;

    /**
     * 发信人
     */
    @ApiModelProperty(value = "发信人")
    private String fromPerson;

    /**
     * SMTP服务器主机名
     */
    @ApiModelProperty(value = "SMTP服务器主机名")
    private String emailSmtpHost;

    /**
     * SMTP服务器端口号
     */
    @ApiModelProperty(value = "SMTP服务器端口号")
    private String emailSmtpPort;

    /**
     * SMTP服务器授权码
     */
    @ApiModelProperty(value = "SMTP服务器授权码")
    private String authCode;

    /**
     * 邮箱启用状态（0：未启用 1：已启用）
     */
    @ApiModelProperty(value = "邮箱启用状态")
    private EmailStatus status;
}
