package com.wanmi.sbc.order.api.constant;

/**
 * 退款原因常量
 * @author: Geek Wang
 * @createDate: 2019/5/27 16:32
 * @version: 1.0
 */
public interface RefundReasonConstants {

	/**
	 * 拼团订单自动退款
	 */
	String Q_ORDER_SERVICE_GROUPON_AUTO_REFUND = "拼团订单自动退款";

	/**
	 * 拼团订单自动退款
	 */
	String Q_ORDER_SERVICE_GROUPON_AUTO_REFUND_USER = "支付成功，订单异常，自动退款";

	/**
	 * 第三方平台调用订单支付失败，自动退款
	 */
	String Q_ORDER_SERVICE_THIRD_AUTO_REFUND_USER = "供应商商品采购失败，已自动退款";

}
