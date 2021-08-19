package com.wanmi.sbc.goods.api.request.livegoods;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播商品修改参数</p>
 * @author zwb
 * @date 2020-06-10 11:05:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@Max(9223372036854775807L)
	private Long id;


	/**
	 * 微信id
	 */
	@ApiModelProperty(value = "微信id")
	@Max(9223372036854775807L)
	private Long goodsId;
	/**
	 * 商品标题
	 */
	@ApiModelProperty(value = "商品标题")
	@Length(max=255)
	private String name;

	/**
	 * 填入mediaID
	 */
	@ApiModelProperty(value = "填入mediaID")
	@Length(max=255)
	private String coverImgUrl;

	/**
	 * 价格类型，1：一口价，2：价格区间，3：显示折扣价
	 */
	@ApiModelProperty(value = "价格类型，1：一口价，2：价格区间，3：显示折扣价")
	@Max(127)
	private Integer priceType;

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
	@Length(max=255)
	private String url;

	/**
	 * 库存
	 */
	@ApiModelProperty(value = "库存")
	@Max(9223372036854775807L)
	private Long stock;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;


	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 商品详情id
	 */
	@ApiModelProperty(value = "商品详情id")
	@Max(9223372036854775807L)
	private String goodsInfoId;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	@NotNull
	@Max(9223372036854775807L)
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
	@Max(9223372036854775807L)
	private Long auditId;

	/**
	 * 审核状态,0:未审核1 审核通过2审核失败3禁用中
	 */
	@ApiModelProperty(value = "审核状态,0:未审核1 审核通过2审核失败3禁用中")
	@NotNull
	@Max(127)
	private Integer auditStatus;

	/**
	 * 审核原因
	 */
	@ApiModelProperty(value = "审核原因")
	@Length(max=255)
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
	@Length(max=255)
	private String deletePerson;


	/**
	 * 1, 2：表示是为api添加商品，否则是在MP添加商品
	 */
	@ApiModelProperty(value = "1, 2：表示是为api添加商品，否则是在MP添加商品")
	@NotNull
	@Max(127)
	private Integer thirdPartyTag;
}