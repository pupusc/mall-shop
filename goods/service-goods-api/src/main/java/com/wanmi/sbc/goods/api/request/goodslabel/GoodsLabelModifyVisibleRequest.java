package com.wanmi.sbc.goods.api.request.goodslabel;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>商品标签修改参数</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLabelModifyVisibleRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@NotNull
	@ApiModelProperty(value = "标签id")
	private Long goodsLabelId;

	/**
	 * 前端是否展示 false: 关闭 true:开启
	 */
	@NotNull
	@ApiModelProperty(value = "前端是否展示 false: 关闭 true:开启")
	private Boolean labelVisible;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id", hidden = true)
	private Long storeId;
}