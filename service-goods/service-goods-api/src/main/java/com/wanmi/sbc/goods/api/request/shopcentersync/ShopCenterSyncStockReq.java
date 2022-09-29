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
}
