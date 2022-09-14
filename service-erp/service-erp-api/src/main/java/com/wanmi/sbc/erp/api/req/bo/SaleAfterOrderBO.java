package com.wanmi.sbc.erp.api.req.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SaleAfterOrderBO implements Serializable {

	private static final long serialVersionUID = 388340642804163275L;
	
	/**
	 * 平台退款ID
	 */
	private String platformRefundId;
	
	/**
	 * 售后类型
	 */
	private Integer refundType;
	
	/**
	 * 售后状态
	 */
	private Integer status;
	
	/**
	 * 执行时间
	 */
	private Date applyTime;
	
	/**
	 * 关闭时间
	 */
	private Date closeTime;
	
	/**
	 * 退款金额
	 */
	private Integer refundFee;
	
	/**
	 * 退款邮费
	 */
	private Integer refundPostFee;
	
	/**
	 * 备注
	 */
	private String memo;
	
	/**
	 * 主订单ID
	 */
	private Long orderId;
	

}
