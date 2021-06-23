package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
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
public class GoodsRestrictedBatchValidateRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量校验
	 */
	List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS;

    /**
     * 会员信息
	 */
	private CustomerVO customerVO;

	/**
	 * 是否开团购买
	 */
	private Boolean openGroupon;

	/**
	 * 秒杀类型
	 */
	private String snapshotType;


	/**
	 * 是否开店礼包
	 */
	private Boolean storeBagsFlag;



}