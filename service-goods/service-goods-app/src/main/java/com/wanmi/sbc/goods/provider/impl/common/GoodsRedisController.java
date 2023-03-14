package com.wanmi.sbc.goods.provider.impl.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.provider.common.GoodsRedisProvider;
import com.wanmi.sbc.goods.api.provider.common.RiskVerifyProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SpuRequest;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import com.wanmi.sbc.goods.collect.*;
import com.wanmi.sbc.goods.common.RiskVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GoodsRedisController implements GoodsRedisProvider {

    @Autowired
    BookTags bookTags;

    @Autowired
    GoodTags goodTags;

    @Autowired
    MarketLabel marketLabel;

    @Autowired
    CacheService cacheService;

    @Autowired
    GoodsTestCacheService goodsCacheService;

    //单个
    @Override
    public BaseResponse refreshBook(SpuRequest spuRequest) {

        String isbn = spuRequest.getIsbn();
        System.out.println("isbn"+ isbn);

        return BaseResponse.SUCCESSFUL();
    }

    //所有
    @Override
    public BaseResponse refreshRedis() {

        new Thread(new Runnable() {
            public void run() {
                try {

                    bookTags.doGoods();         //图书商品

                    goodTags.doGoods();         //非书商品

                    marketLabel.doMarket();     //营销标签

                    cacheService.clear();       //释放内存

                    goodsCacheService.clear();
                    cacheService.clear();


                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();


        return BaseResponse.SUCCESSFUL();

    }

}
