package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterRefundDetailResp implements Serializable {

	private static final long serialVersionUID = -2847188648496008220L;
	
	/**
	 * 售后子单id
	 */
	private Long saItemId;
	
	/**
	 * 支付类型，1 现金，2 知豆，3 积分，4 智慧币
	 */
	private String payType;
	
	/**
	 * 金额
	 */
	private Integer amount;
	
	/**
	 * 退款理由
	 */
	private String reason;

}
