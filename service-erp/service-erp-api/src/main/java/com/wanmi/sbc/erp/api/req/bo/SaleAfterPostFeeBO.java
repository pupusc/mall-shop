package com.wanmi.sbc.erp.api.req.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaleAfterPostFeeBO implements Serializable {

	private static final long serialVersionUID = 702651613733545140L;
	
	private List<SaleAfterRefundDetailBO> saleAfterRefundDetailBOList;
}
