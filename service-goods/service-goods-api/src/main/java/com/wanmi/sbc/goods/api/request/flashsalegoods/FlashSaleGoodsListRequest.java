package com.wanmi.sbc.goods.api.request.flashsalegoods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>抢购商品表列表查询请求参数</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

	@ApiModelProperty(value = "批量查询-goodsinfoIds")
	private List<String> goodsinfoIds;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 活动日期：2019-06-11
	 */
	@ApiModelProperty(value = "活动日期：2019-06-11")
	private String activityDate;

	/**
	 * 活动时间：13:00
	 */
	@ApiModelProperty(value = "活动时间：13:00")
	private String activityTime;

	/**
	 * skuID
	 */
	@ApiModelProperty(value = "skuID")
	private String goodsInfoId;

	/**
	 * spuID
	 */
	@ApiModelProperty(value = "spuID")
	private String goodsId;

	/**
	 * 抢购价
	 */
	@ApiModelProperty(value = "抢购价")
	private BigDecimal price;

	/**
	 * 抢购库存
	 */
	@ApiModelProperty(value = "抢购库存")
	private Integer stock;

	/**
	 * 抢购销量
	 */
	@ApiModelProperty(value = "抢购销量")
	private Long salesVolume;

	/**
	 * 分类ID
	 */
	@ApiModelProperty(value = "分类ID")
	private Long cateId;

	/**
	 * 限购数量
	 */
	@ApiModelProperty(value = "限购数量")
	private Integer maxNum;

	/**
	 * 起售数量
	 */
	@ApiModelProperty(value = "起售数量")
	private Integer minNum;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Long storeId;

	/**
	 * 包邮标志，0：不包邮 1:包邮
	 */
	@ApiModelProperty(value = "包邮标志，0：不包邮 1:包邮")
	private Integer postage;

	/**
	 * 删除标志，0:未删除 1:已删除
	 */
	@ApiModelProperty(value = "删除标志，0:未删除 1:已删除")
	private DeleteFlag delFlag;

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
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

	/**
	 * 查询时间段 0：未开始 1：正在进行 2：已结束 3：未开始与正在进行
	 */
	@ApiModelProperty(value = "查询时间段", notes="0：未开始 1：正在进行 2：已结束 3：未开始与正在进行")
	private Integer queryDataType;
}