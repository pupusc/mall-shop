package com.wanmi.sbc.order.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopCenterSyncDeliveryReq implements Serializable {
	/**
	 * 第三方主订单号
	 */
	private String platformOrderId;
	/**
	 * 第三方子订单号（目前推送就1条）
	 */
	private List<String> platformItemIds;
	/**
	 * 快递单号
	 */
	private String expressNo;
	/**
	 * 物流公司编号
	 */
	private String expressCode;
	/**
	 * SkuId
	 */
	private String platformSkuId;
	/**
	 * shopCenter站内订单号
	 */
	private String orderItemId;
}
