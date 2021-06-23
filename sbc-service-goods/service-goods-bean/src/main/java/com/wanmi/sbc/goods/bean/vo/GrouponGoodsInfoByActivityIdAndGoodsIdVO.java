package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>根据拼团活动ID、SPU编号查询拼团价格最小的拼团SKU信息</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoByActivityIdAndGoodsIdVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团活动ID
	 */
	private String grouponActivityId;

	/**
	 * SPU编号
	 */
	private String goodsId;

	/**
	 * 拼团价格（spu下sku最低价）
	 */
	private BigDecimal grouponPrice;

	/**
	 * sku编号
	 */
	private String goosInfoId;
}