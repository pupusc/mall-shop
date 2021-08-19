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
 * <p>微信登录配置VO</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@ApiModel
@Data
public class WeiboLoginSetVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * weiboSetId
	 */
	@ApiModelProperty(value = "weiboSetId")
	private String weiboSetId;

	/**
	 * mobileServerStatus
	 */
	@ApiModelProperty(value = "mobileServerStatus")
	private Integer mobileServerStatus;

	/**
	 * mobileAppId
	 */
	@ApiModelProperty(value = "mobileAppId")
	private String mobileAppId;

	/**
	 * mobileAppSecret
	 */
	@ApiModelProperty(value = "mobileAppSecret")
	private String mobileAppSecret;

	/**
	 * pcServerStatus
	 */
	@ApiModelProperty(value = "pcServerStatus")
	private Integer pcServerStatus;

	/**
	 * pcAppId
	 */
	@ApiModelProperty(value = "pcAppId")
	private String pcAppId;

	/**
	 * pcAppSecret
	 */
	@ApiModelProperty(value = "pcAppSecret")
	private String pcAppSecret;

	/**
	 * appServerStatus
	 */
	@ApiModelProperty(value = "appServerStatus")
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
	private String operatePerson;

}