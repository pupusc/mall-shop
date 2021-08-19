package com.wanmi.sbc.goods.api.request.goodsevaluate;

import lombok.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>单个删除商品评价请求参数</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateDelByIdRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 评价id
	 */
	@NotNull
	private String evaluateId;
}