package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author: Geek Wang
 * @createDate: 2019/5/21 11:14
 * @version: 1.0
 */
public enum  GrouponOrderCheckStatus {

	/**
	 * 验证通过
	 */
	 PASS,

	/**
	 * 参数不正确
	 */
	ORDER_MARKETING_GROUPON_PARAMS_ERROR,

	/**
	 * 不是拼团订单（普通订单）
	 */
	 NOT_ORDER_MARKETING_GROUPON,

	/**
	 * 拼团订单-支付状态不正确
	 */
	ORDER_MARKETING_GROUPON_PAY_STATUS_ERROR,

	/**
	 * 拼团活动不存在
	 */
	 ORDER_MARKETING_GROUPON_ACTIVITY_NOT_EXIST,

	/**
	 * 拼团活动未开始
	 */
	 ORDER_MARKETING_GROUPON_ACTIVITY_NOT_START,

	/**
	 * 拼团活动已失效，订单提交或者支付
	 */
	  ORDER_MARKETING_GROUPON_ACTIVITY_DISABLE,

	/**
	 * 存在同一个拼团营销活动处于待成团状态，不可开团(团长)
	 */
	  ORDER_MARKETING_GROUPON_WAIT,

	/**
	 * 已参同一团活动，不可再参团
	 */
	  ORDER_MARKETING_GROUPON_ALREADY_JOINED,

	/**
	 * 团长订单不存在
	 */
	 ORDER_MARKETING_GROUPON_ORDER_INST_NOT_EXIST,

	/**
	 * 团发起已超过24小时(倒计时结束)，不可参团
	 */
	  ORDER_MARKETING_GROUPON_COUNT_DOWN_END,
	/**
	 * 未达起购数
	 */
	ORDER_MARKETING_GROUPON_START_NUM,
	/**
	 * 超出限购数
	 */
	ORDER_MARKETING_GROUPON_LIMIT_NUM,

	/**
	 * 团长订单-已成团/已作废
	 */
	 ORDER_MARKETING_GROUPON_ORDER_INST_ALREADY_OR_FAIL;



	@JsonCreator
	public GrouponOrderCheckStatus fromValue(int value){
		return values()[value];
	}

	@JsonValue
	public int toValue() {
		return this.ordinal();
	}
}
