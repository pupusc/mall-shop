package com.wanmi.sbc.goods.api.request.priceadjustmentrecord;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询调价记录表请求参数</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 调价单号
	 */
	@ApiModelProperty(value = "调价单号")
	@NotNull
	private String id;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id", hidden = true)
	private Long storeId;

}