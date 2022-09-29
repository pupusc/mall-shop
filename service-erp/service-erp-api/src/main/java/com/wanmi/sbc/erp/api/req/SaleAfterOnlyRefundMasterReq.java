package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterOnlyRefundMasterReq implements Serializable {

	private static final long serialVersionUID = -2370846067627698943L;
	
	/**
	 * 仅退款类型 1：现金 2：站内积分 3：知豆
	 */
	private Integer refundAmountType;

	/**
	 * 退款金额
	 */
	private Integer refundAmount;
	
	/**
	 * 退款原因 1：退运费 2：补偿
	 */
	private Integer refundReason;
}
