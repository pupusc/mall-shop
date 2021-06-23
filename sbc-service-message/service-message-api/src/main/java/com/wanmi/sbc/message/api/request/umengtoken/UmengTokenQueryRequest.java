package com.wanmi.sbc.message.api.request.umengtoken;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>友盟推送设备与会员关系通用查询请求参数</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengTokenQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	private String customerId;

	/**
	 * 友盟推送会员设备token
	 */
	@ApiModelProperty(value = "友盟推送会员设备token")
	private String devlceToken;

	/**
	 * 友盟推送会员设备token平台类型
	 */
	@ApiModelProperty(value = "token平台类型")
	private PushPlatform platform;

	/**
	 * 搜索条件:绑定时间开始
	 */
	@ApiModelProperty(value = "搜索条件:绑定时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bindingTimeBegin;
	/**
	 * 搜索条件:绑定时间截止
	 */
	@ApiModelProperty(value = "搜索条件:绑定时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bindingTimeEnd;

}