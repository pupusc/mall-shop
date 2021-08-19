package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>友盟push接口配置VO</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@Data
public class UmengPushConfigVO implements Serializable {
	private static final long serialVersionUID = 6735384789161102483L;
	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Integer id;

	/**
	 * androidKeyId
	 */
	@ApiModelProperty(value = "androidKeyId")
	private String androidKeyId;

	/**
	 * androidMsgSecret
	 */
	@ApiModelProperty(value = "androidMsgSecret")
	private String androidMsgSecret;

	/**
	 * androidKeySecret
	 */
	@ApiModelProperty(value = "androidKeySecret")
	private String androidKeySecret;

	/**
	 * iosKeyId
	 */
	@ApiModelProperty(value = "iosKeyId")
	private String iosKeyId;

	/**
	 * iosKeySecret
	 */
	@ApiModelProperty(value = "iosKeySecret")
	private String iosKeySecret;

}