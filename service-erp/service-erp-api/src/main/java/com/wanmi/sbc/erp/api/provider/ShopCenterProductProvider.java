package com.wanmi.sbc.erp.api.provider;

import com.wanmi.sbc.erp.api.req.SalePlatformQueryReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/11 11:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterProductProvider")
public interface ShopCenterProductProvider {

    /**
     * 电商中台创建订单
     * @param salePlatformQueryReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/product/getSalePlatform")
    BaseResponse<Void> createOrder(@RequestBody SalePlatformQueryReq salePlatformQueryReq);
}
