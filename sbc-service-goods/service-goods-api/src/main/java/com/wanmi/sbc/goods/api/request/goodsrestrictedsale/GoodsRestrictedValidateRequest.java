package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>限售配置校验请求</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedValidateRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 货品的Id
	 */
	@NotNull
	private String goodsInfoId;

	/**
	 * 购买的数量
	 */
	@NotNull
	private Long buyNum;

	/**
	 * 会员信息
	 */
	private CustomerVO customerVO;
}