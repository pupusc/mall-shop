package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterBookSubscribeReq implements Serializable {

	private static final long serialVersionUID = -7457061817020291956L;

	/**
	 * 子订单ID
	 */
	private Long itemOrderTid;

	/**
	 * 剩余期数
	 */
	private Integer returnableNum;

	/**
	 * 退订期数
	 */
	private Integer refundNumber;

	/**
	 * 退款金额
	 */
	private Integer refundAmount;

}
