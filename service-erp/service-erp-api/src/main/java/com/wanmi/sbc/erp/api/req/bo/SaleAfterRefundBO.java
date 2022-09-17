package com.wanmi.sbc.erp.api.req.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SaleAfterRefundBO implements Serializable {

	private static final long serialVersionUID = 3496590571505980112L;
	
	/**
	 * 退款主表tid
	 */
	private Long tid;
	
	/**
	 * 售后主单ID
	 */
	private Long saOrderId;
	
	/**
	 * 退款单号
	 */
	private String refundNo;
	
	/**
	 * 退款流水号
	 */
	private String refundTradeNo;
	
	/**
	 * 退款网关
	 */
	private String refundGateway;
	
	/**
	 * 退款金额
	 */
	private Integer amount;
	
	/**
	 * 支付类型
	 */
	private String payType;
	
	/**
	 * 退款状态
	 */
	private Integer status;
	
	/**
	 * 退款时间
	 */
	private Date refundTime;
	
	/**
	 * 退款商户号
	 */
	private String refundMchid;
	
	/**
	 * 操作人Id
	 */
	private String operatorId;
	
	/**
	 * 操作人名
	 */
	private String operatorName;

}
