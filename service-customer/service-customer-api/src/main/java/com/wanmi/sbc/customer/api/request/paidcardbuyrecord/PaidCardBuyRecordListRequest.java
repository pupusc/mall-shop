package com.wanmi.sbc.customer.api.request.paidcardbuyrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.enums.BuyTypeEnum;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员列表查询请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardBuyRecordListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-流水号List
	 */
	@ApiModelProperty(value = "批量查询-流水号List")
	private List<String> payCodeList;

	/**
	 * 流水号
	 */
	@ApiModelProperty(value = "流水号")
	private String payCode;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String customerId;

	/**
	 * 用户姓名
	 */
	@ApiModelProperty(value = "用户姓名")
	private String customerName;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 卡号
	 */
	@ApiModelProperty(value = "卡号")
	private String cardNo;

	/**
	 * 付费会员类型id
	 */
	@ApiModelProperty(value = "付费会员类型id")
	private String paidCardId;

	/**
	 * 付费会员类型名称
	 */
	@ApiModelProperty(value = "付费会员类型名称")
	private String paidCardName;

	/**
	 * 付费周期id
	 */
	@ApiModelProperty(value = "付费周期id")
	private String ruleId;

	/**
	 * 付费周期名称
	 */
	@ApiModelProperty(value = "付费周期名称")
	private String ruleName;

	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	/**
	 * 购买类型 0：购买 1：续费
	 */
	@ApiModelProperty(value = "购买类型 0：购买 1：续费")
	private BuyTypeEnum type;

	/**
	 * 用户账号
	 */
	@ApiModelProperty(value = "用户账号")
	private String customerAccount;

	/**
	 * 搜索条件:失效时间开始
	 */
	@ApiModelProperty(value = "搜索条件:失效时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime invalidTimeBegin;
	/**
	 * 搜索条件:失效时间截止
	 */
	@ApiModelProperty(value = "搜索条件:失效时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime invalidTimeEnd;

	/**
	 * 付费卡实例ID
	 */
	@ApiModelProperty(value = "付费卡实例ID")
	private String customerPaidcardId;

}