package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterFillDeliverInfoReq implements Serializable {

	private static final long serialVersionUID = -1564115545319307632L;
	
	/**
	 * 售后单tid
	 */
	private Long saOrderTid;
	
	/**
	 * 快递编码
	 */
	private String expressCode;
	
	/**
	 * 快递号
	 */
	private String expressNo;

}
