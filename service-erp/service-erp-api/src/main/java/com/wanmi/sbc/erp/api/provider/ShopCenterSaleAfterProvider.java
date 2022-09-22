package com.wanmi.sbc.erp.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.QuerySaleAfterOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCancelReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmDeliverReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmPaymentReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateReq;
import com.wanmi.sbc.erp.api.req.SaleAfterExamineReq;
import com.wanmi.sbc.erp.api.req.SaleAfterFillDeliverInfoReq;
import com.wanmi.sbc.erp.api.req.SaleAfterReq;
import com.wanmi.sbc.erp.api.resp.SaleAfterOrderResp;
import com.wanmi.sbc.erp.api.resp.SaleAfterResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 售后单
 */
@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterSaleAfterProvider")
public interface ShopCenterSaleAfterProvider {

	/**
	 * TODO 创建售后
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/createSaleAfter")
	BaseResponse<Long> createSaleAfter(@RequestBody SaleAfterCreateNewReq request);
}
