package com.soybean.mall.goods.controller;

import com.soybean.mall.wx.callback.parser.WxAuditCallbackParser;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/wx")
public class GoodsController {

    @Autowired
    private WxMiniGoodsProvider miniGoodsProvider;
    @Autowired
    private WxGoodsApiController wxGoodsApiController;
    @Autowired
    private WxAuditCallbackParser wxAuditCallbackParser;

    @PostMapping("/goods/add")
    public BaseResponse addGoods(WxGoodsCreateRequest wxGoodsCreateRequest){
        return miniGoodsProvider.add(wxGoodsCreateRequest);
    }

    @PostMapping("/goods/delete")
    public BaseResponse deleteGoods(WxDeleteProductRequest wxDeleteProductRequest){
        return miniGoodsProvider.delete(wxDeleteProductRequest);
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public String goodsAuditCallback(HttpServletRequest request){
        if(request.getMethod().equals("GET")){
            BaseResponse baseResponse = wxGoodsApiController.verifyCallback(request);
            return (String) baseResponse.getContext();
        }else{
            log.info("微信回调start");
            try {
                wxAuditCallbackParser.dealCallback(request.getInputStream());
            } catch (IOException e) {
                log.error("微信回调失败");
            }
            return "success";
        }
    }

}
