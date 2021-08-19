package com.wanmi.sbc.setting.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
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
 * <p>onlineerviceItemVO</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@Data
public class OnlineServiceItemVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 在线客服座席id
	 */
	@ApiModelProperty(value = "在线客服座席id")
	private Integer serviceItemId;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Long storeId;

	/**
	 * 在线客服主键
	 */
	@ApiModelProperty(value = "在线客服主键")
	private Integer onlineServiceId;

	/**
	 * 客服昵称
	 */
	@ApiModelProperty(value = "客服昵称")
	private String customerServiceName;

	/**
	 * 客服账号
	 */
	@ApiModelProperty(value = "客服账号")
	private String customerServiceAccount;

	/**
	 * 删除标志 默认0：未删除 1：删除
	 */
	@ApiModelProperty(value = "删除标志 默认0：未删除 1：删除")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String operatePerson;

}