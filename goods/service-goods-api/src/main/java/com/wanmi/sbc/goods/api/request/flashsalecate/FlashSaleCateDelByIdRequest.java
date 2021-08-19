package com.wanmi.sbc.goods.api.request.flashsalecate;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除秒杀分类请求参数</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleCateDelByIdRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 秒杀分类主键
	 */
	@ApiModelProperty(value = "秒杀分类主键")
	@NotNull
	private Long cateId;
}