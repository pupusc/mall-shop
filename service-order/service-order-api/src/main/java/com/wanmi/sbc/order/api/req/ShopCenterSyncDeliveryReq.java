package com.wanmi.sbc.order.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ShopCenterSyncDeliveryReq implements Serializable {
	/**
	 * 第三方主订单号
	 */
	private String platformOrderId;

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
	 * 站内子订单号
	 */
	private Long orderItemId;
	/**
	 * 子单发货时间
	 */
	private Date deliveryTime;

	private List<Long> orderItemIds;
}
