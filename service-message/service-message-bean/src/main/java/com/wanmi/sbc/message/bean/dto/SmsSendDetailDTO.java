package com.wanmi.sbc.message.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.SendDetailStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>短信发送实体类</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@Data
public class SmsSendDetailDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
    @ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 发送任务id
	 */
    @ApiModelProperty(value = "发送任务id")
	private Long sendId;

	/**
	 * 接收短信的号码
	 */
    @ApiModelProperty(value = "接收短信的号码")
	private String phoneNumbers;

	/**
	 * 回执id
	 */
    @ApiModelProperty(value = "回执id")
	private String bizId;

	/**
	 * 状态（0-失败，1-成功）
	 */
    @ApiModelProperty(value = "状态（0-失败，1-成功）")
	private SendDetailStatus status;

	/**
	 * 请求状态码。
	 */
    @ApiModelProperty(value = "请求状态码")
	private String code;

	/**
	 * 任务执行信息
	 */
    @ApiModelProperty(value = "任务执行信息")
	private String message;

	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * sendTime
	 */
    @ApiModelProperty(value = "发送时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTime;

    @ApiModelProperty(value = "签名名称")
	private String signName;

    @ApiModelProperty(value = "模板code")
	private String templateCode;

    /**
     * 模板内容JSON
     */
    @ApiModelProperty(value = "模板内容JSON")
	private String templateParam;

    /**
     * 业务类型  参照com.wanmi.sbc.customer.bean.enums.SmsTemplate
     */
    @ApiModelProperty(value = "业务类型")
	private String businessType;

    @ApiModelProperty(value = "签名id")
	private Long signId;

}