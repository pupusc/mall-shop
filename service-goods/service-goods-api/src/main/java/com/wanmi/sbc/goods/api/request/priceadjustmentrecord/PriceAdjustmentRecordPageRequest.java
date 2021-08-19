package com.wanmi.sbc.goods.api.request.priceadjustmentrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>调价记录表分页查询请求参数</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-调价单号List
	 */
	@ApiModelProperty(value = "批量查询-调价单号List")
	private List<String> idList;

	/**
	 * 调价单号
	 */
	@ApiModelProperty(value = "调价单号")
	private String id;

	/**
	 * 调价类型：0 市场价、 1 等级价、2 阶梯价、3 供货价
	 */
	@ApiModelProperty(value = "调价类型：0 市场价、 1 等级价、 2 阶梯价、 3 供货价")
	private PriceAdjustmentType priceAdjustmentType;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id")
	private Long storeId;

	/**
	 * 调价商品数
	 */
	@ApiModelProperty(value = "调价商品数")
	private Long goodsNum;

	/**
	 * 搜索条件:生效时间开始
	 */
	@ApiModelProperty(value = "搜索条件:生效时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime effectiveTimeBegin;
	/**
	 * 搜索条件:生效时间截止
	 */
	@ApiModelProperty(value = "搜索条件:生效时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime effectiveTimeEnd;

	/**
	 * 制单人名称
	 */
	@ApiModelProperty(value = "制单人名称")
	private String creatorName;

	/**
	 * 制单人账户
	 */
	@ApiModelProperty(value = "制单人账户")
	private String creatorAccount;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 是否确认：0 未确认、1 已确认
	 */
	@ApiModelProperty(value = "是否确认：0 未确认、1 已确认")
	private Integer confirmFlag;

	/**
	 * SKU编码
	 */
	@ApiModelProperty(value = "SKU编码")
	private String goodsInfoNo;

	/**
	 * 商品SKU名称
	 */
	@ApiModelProperty(value = "商品SKU名称")
	private String goodsInfoName;

}