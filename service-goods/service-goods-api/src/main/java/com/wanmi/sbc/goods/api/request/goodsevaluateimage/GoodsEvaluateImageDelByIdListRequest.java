package com.wanmi.sbc.goods.api.request.goodsevaluateimage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>批量删除商品评价图片图片请求参数</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateImageDelByIdListRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-图片IdList
	 */
	@NotEmpty
	private List<String> imageIdList;
}