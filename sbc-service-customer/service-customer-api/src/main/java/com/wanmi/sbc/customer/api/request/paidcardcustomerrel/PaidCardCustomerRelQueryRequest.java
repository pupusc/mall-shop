package com.wanmi.sbc.customer.api.request.paidcardcustomerrel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员通用查询请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardCustomerRelQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> idList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

	/**
	 * 会员idList
	 */
	@ApiModelProperty(value = "会员id")
	private List<String> customerIdList;

	/**
	 * 付费会员类型ID
	 */
	@ApiModelProperty(value = "付费会员类型ID")
	private String paidCardId;

	/**
	 * 搜索条件:开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:开始时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime beginTimeBegin;
	/**
	 * 搜索条件:开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:开始时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime beginTimeEnd;

	/**
	 * 搜索条件:结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeBegin;
	/**
	 * 搜索条件:结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeEnd;

	/**
	 * 是否过期时间
	 */
	@ApiModelProperty(value = "是否过期时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTimeFlag;

	/**
	 * 状态： 0：未删除 1：删除
	 */
	@ApiModelProperty(value = "状态： 0：未删除 1：删除")
	private DeleteFlag delFlag;

	/**
	 * 卡号
	 */
	@ApiModelProperty(value = "卡号")
	private String cardNo;

	/**
	 * 付费卡id集合
	 */
	@ApiModelProperty(value = "付费卡id集合")
	private List<String>paidCardIdList;


	/**
	 * 是否发送了过期提醒短信
	 */
	@ApiModelProperty(value = "是否发送了过期提醒短信")
	private Boolean sendMsgFlag;

	/**
	 * 是否发送了已经过期提醒短信
	 */
	@ApiModelProperty(value = "是否发送了已经过期提醒短信")
	private Boolean sendExpireMsgFlag;

}