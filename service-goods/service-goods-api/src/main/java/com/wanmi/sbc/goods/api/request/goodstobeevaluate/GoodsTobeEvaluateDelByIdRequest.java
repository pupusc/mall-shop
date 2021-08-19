package com.wanmi.sbc.goods.api.request.goodstobeevaluate;

import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>单个删除订单商品待评价请求参数</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsTobeEvaluateDelByIdRequest implements Serializable {

	private static final long serialVersionUID = -1328464478583720887L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@NotNull
	private String id;
}