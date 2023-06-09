package com.wanmi.sbc.order.api.req;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
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
	 * 发货时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deliveryTime;

	private List<Long> orderItemIds;
}
