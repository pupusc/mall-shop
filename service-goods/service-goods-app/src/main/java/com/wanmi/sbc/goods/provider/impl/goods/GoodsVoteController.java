package com.wanmi.sbc.goods.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsVoteProvider;
import com.wanmi.sbc.goods.info.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsVoteController implements GoodsVoteProvider {

    @Autowired
    private GoodsService goodsService;

    @Override
    public BaseResponse syncVoteNumber() {
        goodsService.syncGoodsVoteNumber();
        return BaseResponse.SUCCESSFUL();
    }


}
