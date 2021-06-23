package com.wanmi.sbc.goods.api.request.goodslabel;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量删除商品标签请求参数</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLabelDelByIdListRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-标签idList
	 */
	@ApiModelProperty(value = "批量删除-标签idList")
	@NotEmpty
	private List<Long> goodsLabelIdList;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id", hidden = true)
	private Long storeId;
}