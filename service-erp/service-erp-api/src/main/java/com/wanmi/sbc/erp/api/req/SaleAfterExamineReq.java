package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterExamineReq implements Serializable {

	private static final long serialVersionUID = 2811686927599639571L;
	
	/**
	 * 售后单tid
	 */
	private Long saOrderTid;
	
	/**
	 * 审核状态 1：审核通过 2：审核不通过
	 */
	private Integer examinStatus;

}
