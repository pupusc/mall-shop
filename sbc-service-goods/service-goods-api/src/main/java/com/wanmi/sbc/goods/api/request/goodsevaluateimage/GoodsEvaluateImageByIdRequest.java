package com.wanmi.sbc.goods.api.request.goodsevaluateimage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>单个查询商品评价图片请求参数</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateImageByIdRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片Id
	 */
	@NotNull
	private String imageId;
}