package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterConfirmDeliverReq implements Serializable {

	private static final long serialVersionUID = -8793759934079758004L;
	
	/**
	 * 售后单tid
	 */
	private Long saOrderTid;
	
}
