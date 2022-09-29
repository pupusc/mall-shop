package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterOnlyRefundReq implements Serializable {

	private static final long serialVersionUID = -4499857468330784338L;
	
	/**
	 * 退款金额
	 */
	private Integer refundAmount;

}
