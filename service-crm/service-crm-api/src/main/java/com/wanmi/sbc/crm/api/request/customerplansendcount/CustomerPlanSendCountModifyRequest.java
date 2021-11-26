package com.wanmi.sbc.crm.api.request.customerplansendcount;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>权益礼包优惠券发放统计表修改参数</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanSendCountModifyRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 礼包优惠券发放统计id
	 */
	@ApiModelProperty(value = "礼包优惠券发放统计id")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	@Max(9223372036854775807L)
	private Long planId;

	/**
	 * 发放礼包人数
	 */
	@ApiModelProperty(value = "发放礼包人数")
	@Max(9223372036854775807L)
	private Long giftPersonCount;

	/**
	 * 发放礼包次数
	 */
	@ApiModelProperty(value = "发放礼包次数")
	@Max(9223372036854775807L)
	private Long giftCount;

	/**
	 * 发放优惠券人数
	 */
	@ApiModelProperty(value = "发放优惠券人数")
	@Max(9223372036854775807L)
	private Long couponPersonCount;

	/**
	 * 发放优惠券张数
	 */
	@ApiModelProperty(value = "发放优惠券张数")
	@Max(9223372036854775807L)
	private Long couponCount;

	/**
	 * 优惠券使用人数
	 */
	@ApiModelProperty(value = "优惠券使用人数")
	@Max(9223372036854775807L)
	private Long couponPersonUseCount;

	/**
	 * 优惠券使用张数
	 */
	@ApiModelProperty(value = "优惠券使用张数")
	@Max(9223372036854775807L)
	private Long couponUseCount;

	/**
	 * 优惠券使用率
	 */
	@ApiModelProperty(value = "优惠券使用率")
	private Double couponUseRate;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}