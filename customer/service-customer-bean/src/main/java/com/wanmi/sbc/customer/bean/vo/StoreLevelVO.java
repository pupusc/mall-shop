package com.wanmi.sbc.customer.bean.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>商户客户等级表VO</p>
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@Data
public class StoreLevelVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long storeLevelId;

	/**
	 * 店铺编号
	 */
	private Long storeId;

	/**
	 * 等级名称
	 */
	private String levelName;

	/**
	 * 折扣率
	 */
	private BigDecimal discountRate;

	/**
	 * 客户升级所需累积支付金额
	 */
	private BigDecimal amountConditions;

	/**
	 * 客户升级所需累积支付订单笔数
	 */
	private Integer orderConditions;

	/**
	 * 删除标记 0:未删除 1:已删除
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
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 更新人
	 */
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * 删除人
	 */
	private String deletePerson;

	@ApiModelProperty(name = "企业购 折扣")
	private Integer discount;

	@ApiModelProperty(name = "企业购 企业价")
	private BigDecimal price;

}