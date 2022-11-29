package com.wanmi.sbc.erp.api.provider;


import com.wanmi.sbc.erp.api.req.ShopCenterGoodsStockOrCostPriceReq;
import com.wanmi.sbc.erp.api.resp.NewGoodsInfoResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.SalePlatformQueryReq;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import com.wanmi.sbc.erp.api.resp.ShopCenterGoodsCostPriceResp;
import com.wanmi.sbc.erp.api.resp.ShopCenterGoodsStockResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
     * 查询商品信息
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/search-goods-info")
    BaseResponse<List<NewGoodsInfoResp>> searchGoodsInfo(@RequestBody NewGoodsInfoRequest request);

//    /**
//     * goods
//     */
//    @PostMapping("/erp/${application.erp.version}/shopcenter/searchPackByGoodsCodes")
//    BaseResponse<List<GoodsPackRsp>> searchPackByGoodsCodes(@RequestBody List<String> request);

    /**
     * 获取渠道信息
     *
     * @param salePlatformQueryReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/product/getSalePlatform")
    BaseResponse<SalePlatformResp> getSalePlatform(@RequestBody SalePlatformQueryReq salePlatformQueryReq);


    /**
     * 获取库存
     * @param shopCenterGoodsStockReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/product/searchGoodsStock")
    BaseResponse<List<ShopCenterGoodsStockResp>> searchGoodsStock(@RequestBody ShopCenterGoodsStockOrCostPriceReq shopCenterGoodsStockReq);

    /**
     * 获取成本价
     * @param shopCenterGoodsStockReq
     * @return
     */
    @PostMapping("/erp/${application.erp.version}/shopcenter/product/searchGoodsCostPrice")
    BaseResponse<List<ShopCenterGoodsCostPriceResp>> searchGoodsCostPrice(@RequestBody ShopCenterGoodsStockOrCostPriceReq shopCenterGoodsStockReq);
}
