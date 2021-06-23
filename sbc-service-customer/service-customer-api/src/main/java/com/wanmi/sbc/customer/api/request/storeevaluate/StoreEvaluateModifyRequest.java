package com.wanmi.sbc.customer.api.request.storeevaluate;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;

/**
 * <p>店铺评价修改参数</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreEvaluateModifyRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 评价id
	 */
	@Length(max=32)
	private String evaluateId;

	/**
	 * 店铺Id
	 */
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 店铺名称
	 */
	@Length(max=150)
	private String storeName;

	/**
	 * 订单号
	 */
	@NotBlank
	@Length(max=255)
	private String orderNo;

	/**
	 * 会员Id
	 */
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 会员名称
	 */
	@Length(max=128)
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	@NotBlank
	@Length(max=20)
	private String customerAccount;

	/**
	 * 商品评分
	 */
	@NotNull
	@Max(127)
	private Integer goodsScore;

	/**
	 * 服务评分
	 */
	@NotNull
	@Max(127)
	private Integer serverScore;

	/**
	 * 物流评分
	 */
	@NotNull
	@Max(127)
	private Integer logisticsScore;

	/**
	 * 综合评分（冗余字段看后面怎么做）
	 */
	private BigDecimal compositeScore;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@NotNull
	@Max(127)
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
	@Length(max=32)
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
	@Length(max=32)
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
	@Length(max=32)
	private String delPerson;

}