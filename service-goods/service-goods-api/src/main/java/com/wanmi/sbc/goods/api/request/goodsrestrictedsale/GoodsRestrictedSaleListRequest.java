package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AssignPersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.PersonRestrictedCycle;
import com.wanmi.sbc.goods.bean.enums.PersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.RestrictedType;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售配置列表查询请求参数</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-限售主键List
	 */
	@ApiModelProperty(value = "批量查询-限售主键List")
	private List<Long> restrictedIdList;

	/**
	 * 限售主键
	 */
	@ApiModelProperty(value = "限售主键")
	private Long restrictedId;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Long storeId;

	/**
	 * 货品的skuId
	 */
	@ApiModelProperty(value = "货品的skuId")
	private String goodsInfoId;

	/**
	 * 限售方式 0: 按订单 1：按会员
	 */
	@ApiModelProperty(value = "限售方式 0: 按订单 1：按会员")
	private RestrictedType restrictedType;

	/**
	 * 是否每人限售标识 
	 */
	@ApiModelProperty(value = "是否每人限售标识 ")
	private DefaultFlag restrictedPrePersonFlag;

	/**
	 * 是否每单限售的标识
	 */
	@ApiModelProperty(value = "是否每单限售的标识")
	private DefaultFlag restrictedPreOrderFlag;

	/**
	 * 是否指定会员限售的标识
	 */
	@ApiModelProperty(value = "是否指定会员限售的标识")
	private DefaultFlag restrictedAssignFlag;

	/**
	 * 个人限售的方式(  0:终生限售  1:周期限售)
	 */
	@ApiModelProperty(value = "个人限售的方式(  0:终生限售  1:周期限售)")
	private PersonRestrictedType personRestrictedType;

	/**
	 * 个人限售的周期 (0:周   1:月  2:年)
	 */
	@ApiModelProperty(value = "个人限售的周期 (0:周   1:月  2:年)")
	private PersonRestrictedCycle personRestrictedCycle;


	/**
	 * 特定会员的限售类型 0: 会员等级  1：指定会员
	 */
	@ApiModelProperty(value = "特定会员的限售类型 0: 会员等级  1：指定会员")
	private AssignPersonRestrictedType assignPersonRestrictedType;

	/**
	 * 限售数量
	 */
	@ApiModelProperty(value = "限售数量")
	private Long restrictedNum;

	/**
	 * 起售数量
	 */
	@ApiModelProperty(value = "起售数量")
	private Long startSaleNum;

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
	 * 删除标识
	 */
	@ApiModelProperty(value = "删除标识")
	private DeleteFlag delFlag;

}