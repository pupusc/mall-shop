package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyVO;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

/**
 * <p>限售配置校验请求</p>
 * @author baijz
 * @date 2020-04-08 11:20:28  这里拼团和秒杀只校验上线（限售）
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedBatchValidateOrderCommitRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量校验
	 */
	private List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS;

    /**
     * 会员信息
	 */
	private CustomerSimplifyVO customerVO;


}