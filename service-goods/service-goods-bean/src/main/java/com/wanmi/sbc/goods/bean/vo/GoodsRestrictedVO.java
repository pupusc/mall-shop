package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.AssignPersonRestrictedType;
import com.wanmi.sbc.goods.bean.enums.RestrictedCycleType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>限售配置实体类</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@Data
public class GoodsRestrictedVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 限售周期的类型  :  0-天  1-周  2-月  3-年  4-终生  5-订单
	 */
	private RestrictedCycleType restrictedCycleType;

	/**
	 * 限售数量
	 */
	private Long restrictedNum;

	/**
	 * 起售数量
	 */
	private Long startSaleNum;

	/**
	 * 货品Id
	 */
	private String goodsInfoId;

	/**
	 * 商品的信息
	 */
	private GoodsInfoVO goodsInfo;

	/**
	 * 特定会员的限售类型 0: 会员等级  1：指定会员
	 */
	private AssignPersonRestrictedType assignPersonRestrictedType;

	/**
	 * 限售配置会员关系表
	 */
	private List<GoodsRestrictedCustomerRelaVO> goodsRestrictedCustomerRelas;

	/**
	 * 是否打开限售方式开关
	 */
	private DefaultFlag restrictedWay;

	/**
	 * 是否打开起售数量开关
	 */
	private DefaultFlag restrictedStartNum;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

}