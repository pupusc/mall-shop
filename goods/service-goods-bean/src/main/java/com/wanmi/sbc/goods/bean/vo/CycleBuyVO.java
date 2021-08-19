package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.enums.DeliveryPlan;
import com.wanmi.sbc.goods.bean.enums.GiftGiveMethod;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>周期购活动VO</p>
 * @author weiwenhao
 * @date 2021-01-21 20:01:50
 */
@ApiModel
@Data
public class CycleBuyVO implements Serializable {


	private static final long serialVersionUID = 7510781250640011298L;
	/**
	 * 周期购Id
	 */
	@ApiModelProperty(value = "周期购Id")
	private Long id;

	/**
	 * 关联商品Id
	 */
	@ApiModelProperty(value = "关联商品Id")
	private String originGoodsId;

	/**
	 * 创建商品ID
	 */
	@ApiModelProperty(value = "创建商品ID")
	private String goodsId;

	/**
	 * 周期购活动名称
	 */
	@ApiModelProperty(value = "周期购活动名称")
	private String activityName;

	/**
	 * 活动文案
	 */
	@ApiModelProperty(value = "活动文案")
	private String recordActivities;

	/**
	 * 配送方案 0:商家主导配送 1:客户主导配送
	 */
	@ApiModelProperty(value = "配送方案 0:商家主导配送 1:客户主导配送")
	private DeliveryPlan deliveryPlan;

	/**
	 * 赠送方式：0：默认全送 1:可选一种
	 */
	@ApiModelProperty(value = "赠送方式：0：默认全送 1:可选一种")
	private GiftGiveMethod giftGiveMethod;

	/**
	 * 配送周期 0:每日一期 1:每周一期 2:每月一期
	 */
	@ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
	private DeliveryCycle deliveryCycle;

	/**
	 * 发货日期:1.使用逗号拼接发送日期
	 */
	@ApiModelProperty(value = "发货日期:1.使用逗号拼接发送日期")
	private List<String> sendDateRule;

	/**
	 * 周期购运费 =0:每期运费x期数， >0:满X期包邮
	 */
	@ApiModelProperty(value = "周期购运费 =0:每期运费x期数， >0:满X期包邮")
	private Integer cycleFreightType;

	/**
	 * 上下架 0：上架 1:下架
	 */
	@ApiModelProperty(value = "上下架 0：上架 1:下架")
	private AddedFlag addedFlag;

	/**
	 * 商铺Id
	 */
	@ApiModelProperty(value = "商铺Id")
	private Long storeId;

	/**
	 * 删除标记  0：正常，1：删除
	 */
	@ApiModelProperty(value = "删除标记  0：正常，1：删除")
	private DeleteFlag delFlag;

    /**
     * 商品实体信息
     */
    @ApiModelProperty(value = "商品实体信息")
    private GoodsVO goodsVO;

	/**
	 * 商品sku实体信息列表
	 */
	@ApiModelProperty(value = "商品sku实体信息列表")
	private  List<GoodsInfoVO> goodsInfoVOS;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * createPerson
	 */
	@ApiModelProperty(value = "createPerson")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * updatePerson
	 */
	@ApiModelProperty(value = "updatePerson")
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * deletePerson
	 */
	@ApiModelProperty(value = "deletePerson")
	private String deletePerson;

	/**
	 * 赠品列表
	 */
	@ApiModelProperty(value = "赠品列表")
	private List<CycleBuyGiftVO> cycleBuyGiftVOList;

	/**
	 * 发货日期列表(用于用户端展示)
	 */
	@ApiModelProperty(value = "发货日期列表")
	private List<CycleBuySendDateRuleVO> cycleBuySendDateRuleVOList;

	/**
	 * erp商品编码
	 */
	@ApiModelProperty(value = "erp商品编码")
	private String erpGoodsNo;

}