package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询卡券请求参数</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 电子卡券ID
	 */
	@ApiModelProperty(value = "电子卡券ID")
	@NotNull
	private Long id;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识", hidden = true)
	private Long storeId;

}