package com.soybean.mall.goods.controller;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import com.soybean.mall.wx.mini.controller.WxMiniApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/wx")
public class GoodsController {

    @Autowired
    private WxMiniGoodsProvider miniGoodsProvider;
    @Autowired
    private WxMiniApiController wxMiniApiController;

    @PostMapping("/goods/add")
    public BaseResponse addWxGoods(WxGoodsCreateRequest wxGoodsCreateRequest){
        return miniGoodsProvider.add(wxGoodsCreateRequest);
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public String goodsAuditCallback(HttpServletRequest request){
        if(request.getMethod().equals("GET")){
            BaseResponse baseResponse = wxMiniApiController.verifyCallback(request);
            return (String) baseResponse.getContext();
        }else{
            log.info("微信商品审核回调");
            wxMiniApiController.auditCallback(request);
            return "success";
        }
    }

}
