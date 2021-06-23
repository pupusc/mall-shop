package com.wanmi.sbc.goods.api.request.goodstobeevaluate;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>订单商品待评价修改参数</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@ApiModel
@Data
public class GoodsTobeEvaluateModifyRequest implements Serializable {

	private static final long serialVersionUID = 2595074438931737035L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Length(max=32)
	private String id;

	/**
	 * 店铺Id
	 */
	@ApiModelProperty(value = "店铺Id")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 店铺名称
	 */
	@ApiModelProperty(value = "店铺名称")
	@Length(max=150)
	private String storeName;

	/**
	 * 商品id(spuId)
	 */
	@ApiModelProperty(value = "商品id(spuId)")
	@NotBlank
	@Length(max=32)
	private String goodsId;

	/**
	 * 商品图片
	 */
	@ApiModelProperty(value = "商品图片")
	@Length(max=255)
	private String goodsImg;

	/**
	 * 货品id(skuId)
	 */
	@ApiModelProperty(value = "货品id(skuId)")
	@NotBlank
	@Length(max=32)
	private String goodsInfoId;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	@NotBlank
	@Length(max=255)
	private String goodsInfoName;

	/**
	 * 规格值名称
	 */
	@ApiModelProperty(value = "规格值名称")
	@NotBlank
	@Length(max=45)
	private String goodsSpecDetail;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	@NotBlank
	@Length(max=255)
	private String orderNo;

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
	 * 是否评价 0：未评价，1：已评价
	 */
	@ApiModelProperty(value = "是否评价 0：未评价，1：已评价")
	@Max(127)
	private Integer evaluateStatus;

	/**
	 * 是否晒单 0：未晒单，1：已晒单
	 */
	@ApiModelProperty(value = "是否晒单 0：未晒单，1：已晒单")
	@Max(127)
	private Integer evaluateImgStatus;

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