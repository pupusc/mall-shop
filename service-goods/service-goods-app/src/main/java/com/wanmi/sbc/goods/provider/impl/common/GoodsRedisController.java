package com.wanmi.sbc.goods.provider.impl.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.provider.common.GoodsRedisProvider;
import com.wanmi.sbc.goods.api.provider.common.RiskVerifyProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SpuRequest;
import com.wanmi.sbc.goods.api.request.common.ImageVerifyRequest;
import com.wanmi.sbc.goods.collect.*;
import com.wanmi.sbc.goods.collect.respository.GoodRepository;
import com.wanmi.sbc.goods.common.RiskVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
    GoodRepository goodRepository;

    @Autowired
    CacheService cacheService;

    @Autowired
    GoodsTestCacheService goodsCacheService;

    //通过isbn刷新 卖点标签&&商品详细
    //ISBN_C_T003
    @Override
    public BaseResponse refreshBook(SpuRequest spuRequest) {

        String isbn = spuRequest.getIsbn();

        bookTags.doData(isbn);
        System.out.println("isbn"+ isbn);
        return BaseResponse.SUCCESSFUL();

    }

    //通过sku_id刷新
    //sku_id 图书 2c9a009b86a5b1850186a6ae64c80004  spu_id 2c9a00ca86299cda01862a0163e60000
    //sku_id 非书 2c90c8647c3481e7017c35c181310002  spu_id 2c90c8647c3481e7017c35c181140001
    @Override
    public BaseResponse refreshGoods(SpuRequest spuRequest) {

        String sku_id = spuRequest.getSku_id();

        String spu_id = goodRepository.getSpuIdBySku(sku_id);

        String isbn = goodRepository.getIsbnBySpuId(spu_id);

        if(DitaUtil.isNotBlank(isbn)){
            bookTags.doData(isbn);          //图书
        }else{
            goodTags.doGoods(spu_id);       //非书
        }

        if(DitaUtil.isNotBlank(spu_id)){
            Map skuMap = new HashMap();
            skuMap.put("spu_id",spu_id);
            skuMap.put("sku_id",sku_id);

            marketLabel.doData(skuMap);
        }

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
