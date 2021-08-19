package com.wanmi.sbc.goods.api.request.goodslabel;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除商品标签请求参数</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLabelDelByIdRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	@NotNull
	private Long goodsLabelId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id", hidden = true)
    private Long storeId;
}