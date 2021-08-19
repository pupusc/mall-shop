package com.wanmi.sbc.customer.bean.vo;

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
 * <p>直播商品VO</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@ApiModel
@Data
public class LiveGoodsByWeChatVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long goodsId;

	/**
	 * 商品标题
	 */
	@ApiModelProperty(value = "商品标题")
	private String name;

	/**
	 * 填入mediaID
	 */
	@ApiModelProperty(value = "填入mediaID")
	private String coverImgUrl;

	/**
	 * 价格类型，1：一口价，2：价格区间，3：显示折扣价
	 */
	@ApiModelProperty(value = "价格类型，1：一口价，2：价格区间，3：显示折扣价")
	private Integer priceType;
	/**
	 * 商品详情id
	 */
	@ApiModelProperty(value = "商品详情id")
	private String goodsInfoId;

	/**
	 * 直播商品价格左边界
	 */
	@ApiModelProperty(value = "直播商品价格左边界")
	private BigDecimal price;

	/**
	 * 直播商品价格右边界
	 */
	@ApiModelProperty(value = "直播商品价格右边界")
	private BigDecimal price2;

	/**
	 * 商品详情页的小程序路径
	 */
	@ApiModelProperty(value = "商品详情页的小程序路径")
	private String url;

	/**
	 * 库存
	 */
	@ApiModelProperty(value = "库存")
	private Long stock;



	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

	/**
	 * 提交审核时间
	 */
	@ApiModelProperty(value = "提交审核时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime submitTime;

	/**
	 * 审核单ID
	 */
	@ApiModelProperty(value = "审核单ID")
	private Long auditId;

	/**
	 * 审核状态,0:未审核1 审核通过2审核失败3禁用中
	 */
	@ApiModelProperty(value = "审核状态,0:未审核1 审核通过2审核失败3禁用中")
	private Integer auditStatus;

	/**
	 * 审核原因
	 */
	@ApiModelProperty(value = "审核原因")
	private String auditReason;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * 删除人
	 */
	@ApiModelProperty(value = "删除人")
	private String deletePerson;

	/**
	 * 1,2：表示是为api添加商品，否则是在MP添加商品
	 */
	@ApiModelProperty(value = "1,2：表示是为api添加商品，否则是在MP添加商品")
	private Integer thirdPartyTag;
}