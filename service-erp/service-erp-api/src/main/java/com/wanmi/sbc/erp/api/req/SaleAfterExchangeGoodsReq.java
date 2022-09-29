package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaleAfterExchangeGoodsReq implements Serializable {

	private static final long serialVersionUID = 1379280547965326438L;
	
	/**
	 * 快递公司编码
	 */
	private String expressCode;

	/**
	 * 快递单号
	 */
	private String expressNo;
	
	/**
	 * 换货商品list
	 */
	private List<SaleAfterGoodsReq> goodsVOList;

}
