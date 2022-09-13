package com.wanmi.sbc.erp.api.provider;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 售后单
 */
@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterSaleAfterProvider")
public interface ShopCenterSaleAfterProvider {

}
