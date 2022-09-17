package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaleAfterCreateReq implements Serializable {

	private static final long serialVersionUID = -6908461813843355828L;

	/**
	 * 主订单ID
	 */
	private Long orderTid;

	/**
	 * 说明
	 */
	private String reason;

	/**
	 * 附件，为文件系统地址
	 */
	private List<String> attachmentList;

	/**
	 * 子订单售后list
	 */
	private List<SaleAfterItemReq> saleAfterItemVOList;

	/**
	 * 图书订阅VO
	 */
	private SaleAfterBookSubscribeReq saleAfterBookSubscribeVO;

	/**
	 * 补偿VO
	 */
	private SaleAfterCompensateReq saleAfterCompensateVO;

	/**
	 * 主订单-仅退款VO
	 */
	private List<SaleAfterOnlyRefundMasterReq> saleAfterOnlyRefundMasterVOList;


}
