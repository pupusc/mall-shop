package com.wanmi.sbc.erp.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.QuerySaleAfterOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCancelReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmDeliverReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmPaymentReq;
import com.wanmi.sbc.erp.api.req.SaleAfterExamineReq;
import com.wanmi.sbc.erp.api.req.SaleAfterReq;
import com.wanmi.sbc.erp.api.resp.SaleAfterFillDeliverInfoReq;
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
	BaseResponse<List<SaleAfterResp>> createSaleAfter(@RequestBody SaleAfterReq request);

	/**
	 * TODO 售后列表
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/saleAfterList")
	BaseResponse<List<SaleAfterResp>> saleAfterList(@RequestBody SaleAfterReq request);

	/**
	 * TODO 售后列表
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/saleAfterList")
	BaseResponse<List<SaleAfterOrderResp>> querySaleAfterOrder(@RequestBody QuerySaleAfterOrderReq request);

	/**
	 * TODO 审核售后单
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/examineSaleAfter")
	BaseResponse<Boolean> examineSaleAfter(@RequestBody SaleAfterExamineReq request);

	/**
	 * TODO 取消售后
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/cancelSaleAfter")
	BaseResponse<Boolean> cancelSaleAfter(@RequestBody SaleAfterCancelReq request);

	/**
	 * TODO 填写物流信息
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/fillDeliverInfo")
	BaseResponse<Boolean> fillDeliverInfo(@RequestBody SaleAfterFillDeliverInfoReq request);

	/**
	 * TODO 确认收货
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/confirmDeliver")
	BaseResponse<Boolean> confirmDeliver(@RequestBody SaleAfterConfirmDeliverReq request);

	/**
	 * TODO 确认打款
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/confirmPayment")
	BaseResponse<Boolean> confirmPayment(@RequestBody SaleAfterConfirmPaymentReq request);

}
