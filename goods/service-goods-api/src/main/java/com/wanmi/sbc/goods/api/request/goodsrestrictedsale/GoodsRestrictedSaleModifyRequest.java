package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.List;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import com.wanmi.sbc.goods.bean.enums.AssignPersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.PersonRestrictedCycle;
import com.wanmi.sbc.goods.bean.enums.PersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.RestrictedType;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedCustomerRelaVO;
import io.swagger.annotations.Api;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售配置修改参数</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleModifyRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

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
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 删除标识
	 */
	@ApiModelProperty(value = "删除标识")
	private DeleteFlag delFlag;

	/**
	 * 客户会员的IDS
	 */
	@ApiModelProperty("会员的Id")
	private List<String> customerIds;

	/**
	 * 客户等级IDS
	 */
	@ApiModelProperty("客户等级IDS")
	private List<Long> customerLevelIds;

	/**
	 * 限售配置会员关系表
	 */
	@ApiModelProperty("限售配置会员关系表")
	private List<GoodsRestrictedCustomerRelaVO> goodsRestrictedCustomerRelas;
	/**
	 * 是否打开限售方式开关
	 */
	@ApiModelProperty(value = "是否打开限售方式开关 ")
	private DefaultFlag restrictedWay;
	/**
	 * 是否打开起售数量开关
	 */
	@ApiModelProperty(value = "是否打开起售数量开关 ")
	private DefaultFlag restrictedStartNum;
}