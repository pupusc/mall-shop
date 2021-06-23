package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询拼团活动商品信息表请求参数</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoByIdRequest{
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团商品ID
	 */
	@NotNull
	private String grouponGoodsId;
}