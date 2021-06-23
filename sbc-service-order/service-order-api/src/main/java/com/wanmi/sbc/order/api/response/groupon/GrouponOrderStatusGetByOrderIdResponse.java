package com.wanmi.sbc.order.api.response.groupon;

import com.wanmi.sbc.order.bean.enums.GrouponOrderCheckStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>团订单状态</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponOrderStatusGetByOrderIdResponse implements Serializable {
	private static final long serialVersionUID = -4493594277885985685L;


	/**
	 * 团订单验证状态
	 */
	@ApiModelProperty(value = "团订单验证状态")
	private GrouponOrderCheckStatus status;

}