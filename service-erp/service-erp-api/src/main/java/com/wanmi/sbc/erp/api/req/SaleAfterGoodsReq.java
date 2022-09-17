package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaleAfterGoodsReq implements Serializable {

	private static final long serialVersionUID = 8831817501183013287L;
	
	/**
	 * 补偿原因，补偿时生效 1：赠品 2：补偿-商品
	 *
	 * @see com.soybean.unified.order.api.enums.SaleAfterCompensationTypeEnum
	 */
	private Integer compensationType;

	/**
	 * 购买件数
	 */
	private Integer count;

	/**
	 * 业务类型
	 */
	private Integer metaGoodsType;

	/**
	 * 销售码编码
	 */
	private String metaGoodsId;

	/**
	 * 销售码名称
	 */
	private String metaGoodsName;

	/**
	 * 是否组合规则
	 */
	private Boolean isPack;

}
