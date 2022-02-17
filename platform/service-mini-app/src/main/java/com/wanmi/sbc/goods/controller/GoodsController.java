package com.wanmi.sbc.goods.controller;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import com.wanmi.sbc.wx.mini.controller.WxMiniApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private WxMiniGoodsProvider miniGoodsProvider;
    @Autowired
    private WxMiniApiController wxMiniApiController;

    @PostMapping("/wx/add")
    public BaseResponse addWxGoods(WxGoodsCreateRequest wxGoodsCreateRequest){
        return miniGoodsProvider.add(wxGoodsCreateRequest);
    }

    @RequestMapping("/wx/callback")
    public String goodsAuditCallback(HttpServletRequest request){
        log.info("微信商品审核回调");
        wxMiniApiController.auditCallback(request);
        return "success";
    }
}
