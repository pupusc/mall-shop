package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺服务待评价VO</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@ApiModel
@Data
public class StoreTobeEvaluateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private String id;

	/**
	 * 店铺Id
	 */
	@ApiModelProperty(value = "店铺Id")
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
	private String storeName;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
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
	@ApiModelProperty(value = "店铺名称")
	private Integer goodsNum;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	private String customerId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称")
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	@ApiModelProperty(value = "会员登录账号|手机号")
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
	private String updatePerson;

}