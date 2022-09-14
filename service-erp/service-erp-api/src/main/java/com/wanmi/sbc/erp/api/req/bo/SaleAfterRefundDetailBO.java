package com.wanmi.sbc.erp.api.req.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterRefundDetailBO implements Serializable {

	private static final long serialVersionUID = 5983425043444766469L;
	
	/**
	 * 售后子单ID
	 */
	private Long saItemId;
	
	/**
	 * 支付类型
	 */
	private Integer payType;
	
	/**
	 * 退款金额
	 */
	private Integer amount;
	
	/**
	 * 退款理由
	 */
	private String refundReason;

}
