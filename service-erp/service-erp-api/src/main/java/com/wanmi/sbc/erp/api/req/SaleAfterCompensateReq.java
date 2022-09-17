package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaleAfterCompensateReq implements Serializable {

	private static final long serialVersionUID = 306027861435968137L;
	
	/**
	 * 可退数量
	 */
	private Integer returnableNum;
	
	/**
	 * 补偿商品list
	 */
	private List<SaleAfterGoodsReq> goodsVOList;
	
}
