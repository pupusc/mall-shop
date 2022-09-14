package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterReturnAllReq implements Serializable {

	private static final long serialVersionUID = -1414856657313024114L;
	
	/**
	 * 退货数量
	 */
	private Integer refundNumber;

	/**
	 * 退款金额
	 */
	private Integer refundAmount;
	
	/**
	 * 快递公司编码
	 */
	private String expressCode;

	/**
	 * 快递单号
	 */
	private String expressNo;

}
