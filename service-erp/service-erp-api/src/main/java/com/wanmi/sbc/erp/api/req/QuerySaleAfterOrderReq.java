package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuerySaleAfterOrderReq implements Serializable {

	private static final long serialVersionUID = 203778006357061411L;
	
	/**
	 * 售后主单号（有saId按saId,再按orderId）
	 */
	private Long saId;

	/**
	 * 主订单ID
	 */
	private Long orderId;

}
