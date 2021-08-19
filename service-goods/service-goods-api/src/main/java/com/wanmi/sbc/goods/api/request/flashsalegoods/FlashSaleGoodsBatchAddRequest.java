package com.wanmi.sbc.goods.api.request.flashsalegoods;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>抢购商品表新增参数</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsBatchAddRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	@NotEmpty
	private List<FlashSaleGoodsVO> flashSaleGoodsVOList;

}