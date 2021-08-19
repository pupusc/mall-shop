package com.wanmi.sbc.setting.api.request.qqloginset;

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
 * <p>qq登录信息修改参数</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * qqSetId
	 */
	@ApiModelProperty(value = "qqSetId")
	@Length(max=32)
	private String qqSetId;

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