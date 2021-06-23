package com.wanmi.sbc.crm.api.request.customerplansendcount;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>权益礼包优惠券发放统计表列表查询请求参数</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanSendCountListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-礼包优惠券发放统计idList
	 */
	@ApiModelProperty(value = "批量查询-礼包优惠券发放统计idList")
	private List<Long> idList;

	/**
	 * 礼包优惠券发放统计id
	 */
	@ApiModelProperty(value = "礼包优惠券发放统计id")
	private Long id;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	private Long planId;

	/**
	 * 发放礼包人数
	 */
	@ApiModelProperty(value = "发放礼包人数")
	private Long giftPersonCount;

	/**
	 * 发放礼包次数
	 */
	@ApiModelProperty(value = "发放礼包次数")
	private Long giftCount;

	/**
	 * 发放优惠券人数
	 */
	@ApiModelProperty(value = "发放优惠券人数")
	private Long couponPersonCount;

	/**
	 * 发放优惠券张数
	 */
	@ApiModelProperty(value = "发放优惠券张数")
	private Long couponCount;

	/**
	 * 优惠券使用人数
	 */
	@ApiModelProperty(value = "优惠券使用人数")
	private Long couponPersonUseCount;

	/**
	 * 优惠券使用张数
	 */
	@ApiModelProperty(value = "优惠券使用张数")
	private Long couponUseCount;

	/**
	 * 优惠券使用率
	 */
	@ApiModelProperty(value = "优惠券使用率")
	private Double couponUseRate;

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

}