package com.wanmi.sbc.setting.api.request.syssms;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统短信配置新增参数</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 接口地址
	 */
	@ApiModelProperty(value = "接口地址")
	@Length(max=45)
	private String smsUrl;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	@Length(max=45)
	private String smsName;

	/**
	 * SMTP密码
	 */
	@ApiModelProperty(value = "SMTP密码")
	@Length(max=45)
	private String smsPass;

	/**
	 * 网关
	 */
	@ApiModelProperty(value = "网关")
	@Length(max=45)
	private String smsGateway;

	/**
	 * 是否开启(0未开启 1已开启)
	 */
	@ApiModelProperty(value = "是否开启(0未开启 1已开启)")
	@Max(127)
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
	@Length(max=45)
	private String smsAddress;

	/**
	 * smsProvider
	 */
	@ApiModelProperty(value = "smsProvider")
	@Length(max=45)
	private String smsProvider;

	/**
	 * smsContent
	 */
	@ApiModelProperty(value = "smsContent")
	@Length(max=45)
	private String smsContent;

}