package com.wanmi.sbc.common.util;

import java.math.BigDecimal;

/**
 * @author: Geek Wang
 * @createDate: 2019/6/27 14:08
 * @version: 1.0
 */
public class DistributionCommissionUtils {

	public static BigDecimal calDistributionCommission(BigDecimal x, BigDecimal y, CustomizeFunction<BigDecimal, BigDecimal> customizeFunction) {
		return customizeFunction.handler(x, y);
	}

	/**
	 * 计算分销分销佣金
	 * @param goodsInfoCommission 商品预估佣金
	 * @param commissionRate 佣金比例-分销员等级
	 * @return
	 */
	public static BigDecimal calDistributionCommission(BigDecimal goodsInfoCommission, BigDecimal commissionRate){
		return calDistributionCommission(goodsInfoCommission, commissionRate, (x, y) -> commissionRate.multiply(goodsInfoCommission).setScale(2,BigDecimal.ROUND_DOWN));
	}

}
