package com.wanmi.sbc.erp.api.provider;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.SalePlatformQueryReq;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description: 商品对象信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/11 11:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterProductProvider")
public interface ShopCenterProductProvider {

    /**
     * 获取渠道信息
     * @param salePlatformQueryReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/product/getSalePlatform")
    BaseResponse<SalePlatformResp> getSalePlatform(@RequestBody SalePlatformQueryReq salePlatformQueryReq);
}
