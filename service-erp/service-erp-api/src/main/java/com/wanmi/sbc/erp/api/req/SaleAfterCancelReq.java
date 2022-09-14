package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterCancelReq implements Serializable {

	private static final long serialVersionUID = -4469251233509763733L;
	
	/**
	 * 售后单tid
	 */
	private Long saOrderTid;
	
}
