package com.wanmi.sbc.goods.api.provider.goods;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;


@FeignClient(value = "${application.goods.name}", contextId = "GoodsAuditProvider")
public interface GoodsAuditProvider {
    /**
     * 商品标签
     */
    @PostMapping("/goods/${application.goods.version}/audit/list")
    BaseResponse<MicroServicePage<GoodsSyncVO>> list(@RequestBody GoodsAuditQueryRequest request);

    @PostMapping("/goods/${application.goods.version}/audit/image")
    BaseResponse auditGoods(@RequestBody GoodsAuditQueryRequest request);

    @PostMapping("/goods/${application.goods.version}/audit/admanual/approve")
    BaseResponse approveAdManual(@RequestBody GoodsAuditQueryRequest request);

    @PostMapping("/goods/${application.goods.version}/audit/admanual/reject")
    BaseResponse rejectAdManual(@RequestBody GoodsAuditQueryRequest request);

    @PostMapping("/goods/${application.goods.version}/audit/launch/approve")
    BaseResponse launchApprove(@RequestBody GoodsAuditQueryRequest request);

    @PostMapping("/goods/${application.goods.version}/audit/launch/reject")
    BaseResponse launchReject(@RequestBody GoodsAuditQueryRequest request);

    @PostMapping("/goods/${application.goods.version}/audit/publish")
    BaseResponse publish(@RequestBody GoodsAuditQueryRequest request);

    @GetMapping("/goods/${application.goods.version}/audit/detail/{goodsNo}")
    BaseResponse<GoodsViewByIdResponse> detail(@PathVariable("goodsNo") String goodsNo);
}
