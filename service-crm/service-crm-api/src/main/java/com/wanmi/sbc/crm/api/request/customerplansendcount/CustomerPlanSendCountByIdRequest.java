package com.wanmi.sbc.crm.api.request.customerplansendcount;

import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询权益礼包优惠券发放统计表请求参数</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanSendCountByIdRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 礼包优惠券发放统计id
	 */
	@ApiModelProperty(value = "运营计划id")
	@NotNull
	private Long planId;
}