package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询限售配置请求参数</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleByIdRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 限售主键
	 */
	@ApiModelProperty(value = "限售主键")
	@NotNull
	private Long restrictedId;
}