package com.wanmi.sbc.goods.bean.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.enums.PointsGoodsStatus;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>积分商品表VO</p>
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@ApiModel
@Data
public class PointsGoodsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 积分商品id
	 */
	@ApiModelProperty(value = "积分商品id")
	private String pointsGoodsId;

	/**
	 * SpuId
	 */
	@ApiModelProperty(value = "SpuId")
	private String goodsId;

	/**
	 * SkuId
	 */
	@ApiModelProperty(value = "SkuId")
	private String goodsInfoId;

	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "分类id")
	private Integer cateId;

	/**
	 * 字段更改，不代表积分商品库存，改为积分商品可兑换上限
	 */
	@ApiModelProperty(value = "字段更改，不代表积分商品库存，改为积分商品可兑换上限")
	private Long stock;

	/**
	 * 可兑换的最大库存
	 */
	@ApiModelProperty(value = "可兑换的最大库存")
	private Long maxStock;

	/**
	 * 销量
	 */
	@ApiModelProperty(value = "销量")
	private Long sales;

	/**
	 * 结算价格
	 */
	@ApiModelProperty(value = "结算价格")
	private BigDecimal settlementPrice;

	/**
	 * 兑换积分
	 */
	@ApiModelProperty(value = "兑换积分")
	private Long points;

	/**
	 * 是否启用 0：停用，1：启用
	 */
	@ApiModelProperty(value = "是否启用 0：停用，1：启用")
	private EnableStatus status;

	/**
	 * 兑换状态 1: 进行中, 2: 暂停中,3: 未开始,4: 已结束
	 */
	@ApiModelProperty(value = "兑换状态 1: 进行中, 2: 暂停中,3: 未开始,4: 已结束")
	private PointsGoodsStatus pointsGoodsStatus;

	/**
	 * 推荐标价, 0: 未推荐 1: 已推荐
	 */
	@ApiModelProperty(value = "推荐标价, 0: 未推荐 1: 已推荐")
	private BoolFlag recommendFlag;

	/**
	 * 分类信息
	 */
	@ApiModelProperty(value = "分类信息")
	private PointsGoodsCateVO pointsGoodsCate;

	/**
	 * SPU信息
	 */
	@ApiModelProperty(value = "SPU信息")
	private GoodsVO goods;

	/**
	 * SKU信息
	 */
	@ApiModelProperty(value = "SKU信息")
	private GoodsInfoVO goodsInfo;

	/**
	 * 规格信息
	 */
	@ApiModelProperty(value = "规格信息")
	private String specText;

	/**
	 * 兑换开始时间
	 */
	@ApiModelProperty(value = "兑换开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime beginTime;

	/**
	 * 兑换结束时间
	 */
	@ApiModelProperty(value = "兑换结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;

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

	/**
	 * 删除标识,0: 未删除 1: 已删除
	 */
	@ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
	private DeleteFlag delFlag;

}