package com.wanmi.sbc.setting.api.request.qqloginset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>qq登录信息新增参数</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * mobileServerStatus
	 */
	@ApiModelProperty(value = "mobileServerStatus")
	@Max(127)
	private Integer mobileServerStatus;

	/**
	 * mobileAppId
	 */
	@ApiModelProperty(value = "mobileAppId")
	@Length(max=60)
	private String mobileAppId;

	/**
	 * mobileAppSecret
	 */
	@ApiModelProperty(value = "mobileAppSecret")
	@Length(max=60)
	private String mobileAppSecret;

	/**
	 * pcServerStatus
	 */
	@ApiModelProperty(value = "pcServerStatus")
	@Max(127)
	private Integer pcServerStatus;

	/**
	 * pcAppId
	 */
	@ApiModelProperty(value = "pcAppId")
	@Length(max=60)
	private String pcAppId;

	/**
	 * pcAppSecret
	 */
	@ApiModelProperty(value = "pcAppSecret")
	@Length(max=60)
	private String pcAppSecret;

	/**
	 * appServerStatus
	 */
	@ApiModelProperty(value = "appServerStatus")
	@Max(127)
	private Integer appServerStatus;

	/**
	 * createTime
	 */
	@ApiModelProperty(value = "createTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * updateTime
	 */
	@ApiModelProperty(value = "updateTime")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * operatePerson
	 */
	@ApiModelProperty(value = "operatePerson")
	@Length(max=45)
	private String operatePerson;

}