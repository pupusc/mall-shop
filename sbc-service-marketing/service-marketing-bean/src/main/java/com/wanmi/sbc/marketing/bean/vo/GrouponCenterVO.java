package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>H5-拼团活动首页列表VO</p>
 * @author chenli
 * @date 2019-05-21 14:02:38
 */
@ApiModel
@Data
public class GrouponCenterVO implements Serializable {

	private static final long serialVersionUID = 270892065421007859L;

	/**
	 * 活动ID
	 */
    @ApiModelProperty(value = "活动ID")
	private String grouponActivityId;

	/**
	 * spu编号
	 */
	@ApiModelProperty(value = "spu编号")
	private String goodsId;

	/**
	 * sku编号
	 */
	@ApiModelProperty(value = "sku编号")
	private String goodsInfoId;

	/**
	 * spu商品名称
	 */
	@ApiModelProperty(value = "spu商品名称")
	private String goodsName;

	/**
	 * 商品图片
	 */
	@ApiModelProperty(value = "商品图片")
	private String goodsImg;

	/**
	 * 商品市场价
	 */
	@ApiModelProperty(value = "商品市场价")
	private BigDecimal marketPrice;

	/**
	 * 拼团价格
	 */
	@ApiModelProperty(value = "拼团价格")
	private BigDecimal grouponPrice;

	/**
	 * 拼团人数
	 */
    @ApiModelProperty(value = "拼团人数")
	private Integer grouponNum;

	/**
	 * 已成团人数
	 */
	@ApiModelProperty(value = "已成团人数")
	private Integer alreadyGrouponNum;

}