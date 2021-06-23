package com.wanmi.sbc.goods.api.request.goodsevaluateimage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>根据评价id删除晒单图片</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateImageDelByEvaluateIdRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 评论id
	 */
	@NotNull
	private String evaluateId;
}