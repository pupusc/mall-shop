package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统短信配置VO</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@Data
public class SysSmsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String smsId;

	/**
	 * 接口地址
	 */
	@ApiModelProperty(value = "接口地址")
	private String smsUrl;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String smsName;

	/**
	 * SMTP密码
	 */
	@ApiModelProperty(value = "SMTP密码")
	private String smsPass;

	/**
	 * 网关
	 */
	@ApiModelProperty(value = "网关")
	private String smsGateway;

	/**
	 * 是否开启(0未开启 1已开启)
	 */
	@ApiModelProperty(value = "是否开启(0未开启 1已开启)")
	private Integer isOpen;

	/**
	 * createTime
	 */
	@ApiModelProperty(value = "createTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * modifyTime
	 */
	@ApiModelProperty(value = "modifyTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime modifyTime;

	/**
	 * smsAddress
	 */
	@ApiModelProperty(value = "smsAddress")
	private String smsAddress;

	/**
	 * smsProvider
	 */
	@ApiModelProperty(value = "smsProvider")
	private String smsProvider;

	/**
	 * smsContent
	 */
	@ApiModelProperty(value = "smsContent")
	private String smsContent;
}