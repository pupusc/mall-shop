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

import javax.persistence.Column;
import javax.persistence.Convert;

/**
 * <p>订单商品待评价VO</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@ApiModel
@Data
public class GoodsTobeEvaluateVO implements Serializable {
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
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	private String storeName;

	/**
	 * 商品id(spuId)
	 */
	@ApiModelProperty(value = "商品id(spuId)")
	private String goodsId;

	/**
	 * 商品图片
	 */
	@ApiModelProperty(value = "商品图片")
	private String goodsImg;

	/**
	 * 货品id(skuId)
	 */
	@ApiModelProperty(value = "货品id(skuId)")
	private String goodsInfoId;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsInfoName;

	/**
	 * 规格值名称
	 */
	@ApiModelProperty(value = "规格值名称")
	private String goodsSpecDetail;

	/**
	 * 购买时间
	 */
	@ApiModelProperty(value = "购买时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime buyTime;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNo;

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
	 * 是否评价 0：未评价，1：已评价
	 */
	@ApiModelProperty(value = "是否评价 0：未评价，1：已评价")
	private Integer evaluateStatus;

	/**
	 * 是否晒单 0：未晒单，1：已晒单
	 */
	@ApiModelProperty(value = "是否晒单 0：未晒单，1：已晒单")
	private Integer evaluateImgStatus;

	/**
	 * 商品自动评价日期
	 */
	@ApiModelProperty(value = "商品自动评价日期")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate autoGoodsEvaluateDate;

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