package com.wanmi.sbc.goods.api.request.livegoods;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询直播商品请求参数</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 微信id
	 */
	@ApiModelProperty(value = "微信id")
	@NotNull
	private Long goodsId;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识", hidden = true)
	private Long storeId;

}