package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.mini.goods.MiniGoodsProvider;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiniGoodsProvider miniGoodsProvider;

    @PostMapping("/wx/add")
    public BaseResponse addWxGoods(WxGoodsCreateRequest wxGoodsCreateRequest){
        return miniGoodsProvider.add(wxGoodsCreateRequest);
    }
}
