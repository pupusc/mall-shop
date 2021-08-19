package com.wanmi.sbc.customer.bean.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>店铺评价VO</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@Data
public class StoreEvaluateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 评价id
	 */
	private String evaluateId;

	/**
	 * 店铺Id
	 */
	private Long storeId;

	/**
	 * 店铺名称
	 */
	private String storeName;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 会员Id
	 */
	private String customerId;

	/**
	 * 会员名称
	 */
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	private String customerAccount;

	/**
	 * 商品评分
	 */
	private Integer goodsScore;

	/**
	 * 服务评分
	 */
	private Integer serverScore;

	/**
	 * 物流评分
	 */
	private Integer logisticsScore;

	/**
	 * 综合评分（冗余字段看后面怎么做）
	 */
	private BigDecimal compositeScore;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	private String createPerson;

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTime;

	/**
	 * 删除人
	 */
	private String delPerson;

}