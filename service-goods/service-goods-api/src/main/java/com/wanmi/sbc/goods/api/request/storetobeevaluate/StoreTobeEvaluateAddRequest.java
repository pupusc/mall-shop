package com.wanmi.sbc.goods.api.request.storetobeevaluate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺服务待评价新增参数</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreTobeEvaluateAddRequest implements Serializable {

	private static final long serialVersionUID = 845227681922304633L;

	/**
	 * 店铺Id
	 */
	@ApiModelProperty(value = "店铺Id")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 店铺logo
	 */
	@ApiModelProperty(value = "店铺logo")
	private String storeLogo;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	@Length(max=150)
	private String storeName;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	@NotBlank
	@Length(max=255)
	private String orderNo;

	/**
	 * 购买时间
	 */
	@ApiModelProperty(value = "购买时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime buyTime;

	/**
	 * 购买商品数量
	 */
	@NotNull
	@ApiModelProperty(value = "店铺名称")
	private Integer goodsNum;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称")
	@Length(max=128)
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	@ApiModelProperty(value = "会员登录账号|手机号")
	@NotBlank
	@Length(max=20)
	private String customerAccount;

	/**
	 * 店铺自动评价日期
	 */
	@ApiModelProperty(value = "店铺自动评价日期")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate autoStoreEvaluateDate;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=32)
	private String createPerson;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	@Length(max=32)
	private String updatePerson;

}