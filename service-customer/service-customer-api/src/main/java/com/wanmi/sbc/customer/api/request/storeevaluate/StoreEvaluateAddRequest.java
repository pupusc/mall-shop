package com.wanmi.sbc.customer.api.request.storeevaluate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>店铺评价新增参数</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreEvaluateAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

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
	@Length(max=255)
	private String orderNo;

	/**
	 * 购买时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime buyTime;

	/**
	 * 会员Id
	 */
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
	@Length(max=20)
	private String customerAccount;

	/**
	 * 商品评分
	 */
	@Max(127)
	private Integer goodsScore;

	/**
	 * 服务评分
	 */
	@Max(127)
	private Integer serverScore;

	/**
	 * 物流评分
	 */
	@Max(127)
	private Integer logisticsScore;

	/**
	 * 综合评分（冗余字段看后面怎么做）
	 */
	private BigDecimal compositeScore;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Max(127)
	private Integer delFlag = 0;

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

    /**
     * 终端来源
     */
    private TerminalSource terminalSource;

}