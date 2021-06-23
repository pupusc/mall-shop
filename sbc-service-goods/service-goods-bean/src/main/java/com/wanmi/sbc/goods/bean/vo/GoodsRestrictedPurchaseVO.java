package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>购物车限售配置实体类</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@Data
public class GoodsRestrictedPurchaseVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 是否有权限购买
	 */
	private DefaultFlag defaultFlag;

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


}