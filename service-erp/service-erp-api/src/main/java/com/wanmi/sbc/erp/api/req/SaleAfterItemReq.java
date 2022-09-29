package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterItemReq implements Serializable {

	private static final long serialVersionUID = 3098474496632851480L;

	/**
	 * 子订单ID
	 */
	private Long itemOrderTid;

	/**
	 * 可退数量 【】
	 */
	private Integer returnableNum;

	/**
	 * 实付金额
	 */
	private Integer oughtFee;

	/**
	 * 购买件数
	 */
	private Integer num;

	/**
	 * 可退金额
	 */
	private Integer refundableAmount;

	/**
	 * 退货退款VO
	 */
	private SaleAfterReturnAllReq saleAfterReturnAllVO;

	/**
	 * 仅退款VO
	 */
	private SaleAfterOnlyRefundReq saleAfterOnlyRefundVO;

	/**
	 * 换货VO
	 */
	private SaleAfterExchangeGoodsReq saleAfterExchangeGoodsVO;

}
