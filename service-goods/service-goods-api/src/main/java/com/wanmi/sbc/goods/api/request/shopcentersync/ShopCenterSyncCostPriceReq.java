package com.wanmi.sbc.goods.api.request.shopcentersync;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopCenterSyncCostPriceReq implements Serializable {
	/**
	 * 商品码
	 */
	private String goodsCode;
	/**
	 * 商品成本价
	 */
	private Integer goodsPrice;
}
