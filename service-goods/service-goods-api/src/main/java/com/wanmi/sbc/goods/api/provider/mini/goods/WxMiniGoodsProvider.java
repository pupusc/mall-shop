package com.wanmi.sbc.goods.api.provider.mini.goods;

import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.WxGoodsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "${application.goods.name}", contextId = "WxMiniGoodsProvider")
public interface WxMiniGoodsProvider {

    @PostMapping("/goods/${application.goods.version}/wx/add")
    BaseResponse add(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest);

    @PostMapping("/goods/${application.goods.version}/wx/to-audit")
    BaseResponse toAudit(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest);

    @PostMapping("/goods/${application.goods.version}/wx/cancel-audit")
    BaseResponse cancelAudit(@RequestParam("goodsId") String goodsId);

    @PostMapping("/goods/${application.goods.version}/wx/update")
    BaseResponse update(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest);

    @PostMapping("/goods/${application.goods.version}/wx/list")
    BaseResponse<MicroServicePage<WxGoodsVo>> list(@RequestBody WxGoodsSearchRequest wxGoodsSearchRequest);

    @PostMapping("/goods/${application.goods.version}/wx/delete")
    BaseResponse delete(@RequestBody WxDeleteProductRequest wxDeleteProductRequest);

    @PostMapping("/goods/${application.goods.version}/wx/find-all-id")
    BaseResponse<List<String>> findAllId(@RequestBody WxGoodsSearchRequest wxGoodsSearchRequest);

    @PostMapping("/goods/${application.goods.version}/wx/find-by-goods-ids")
    BaseResponse<List<String>> findByGoodsIds(@RequestBody List<String> goodsIds);

    @PostMapping("/goods/${application.goods.version}/wx/audit-sync")
    BaseResponse goodsAuditSync(@RequestParam(value = "goodsId", required = false) String goodsId);

    @PostMapping("/${application.goods.version}/wx/audit/callback")
    BaseResponse<Boolean> auditCallback(@RequestBody Map<String, Object> paramMap);
}
