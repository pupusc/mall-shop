package com.wanmi.sbc.goods.api.request.shopcentersync;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopCenterSyncStockReq implements Serializable {
	/**
	 * 商品码
	 */
	private String goodsCode;
	/**
	 * 数量
	 */
	private Integer quantity;

	/**
	 * 商品成本价
	 */
	private Integer goodsPrice;

	/**
	 * 1001=库存变动，1004=成本价变动
	 */
	private Integer tag;
}
