package com.wanmi.sbc.erp.api.req.bo;

import com.wanmi.sbc.erp.api.enums.SaleAfterCreateEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class SaleAfterBO implements Serializable {

	private static final long serialVersionUID = 9051256162494055575L;

	/**
	 * 售后类型：1退货退款 2仅退款 3换货 4补偿 5主订单仅退款 6赠品
	 */
	@NotEmpty
	private List<Integer> refundTypeList;

	/**
	 * 售后主单
	 */
	private SaleAfterOrderBO saleAfterOrderBO;

	/**
	 * 快递费
	 */
	private SaleAfterPostFeeBO saleAfterPostFee;

	/**
	 * 售后子单
	 */
	private List<SaleAfterItemBO> saleAfterItemBOList;

	/**
	 * 退款主单
	 */
	private List<SaleAfterRefundBO> saleAfterRefundBOList;

	/**
	 * 业务发起
	 */
	private SaleAfterCreateEnum saleAfterCreateEnum;
}
