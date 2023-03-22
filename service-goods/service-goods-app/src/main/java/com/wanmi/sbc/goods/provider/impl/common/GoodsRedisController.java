package com.wanmi.sbc.goods.provider.impl.common;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.DateUtil;
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

import java.util.*;

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

        new Thread(new Runnable() {
            public void run() {
                try {

                    String isbn = spuRequest.getIsbn();

                    bookTags.doData(isbn);
                    System.out.println("isbn"+ isbn);

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                            DateUtil.format(new Date(),DateUtil.FMT_TIME_1),
                            "testLog",
                            Objects.isNull(ex.getMessage())?"": JSON.toJSONString(ex.getMessage()),
                            ex);
                }
            }
        }).start();


        return BaseResponse.SUCCESSFUL();

    }

    //通过sku_id刷新
    //sku_id 图书 2c9a009b86a5b1850186a6ae64c80004  spu_id 2c9a00ca86299cda01862a0163e60000
    //sku_id 非书 2c90c8647c3481e7017c35c181310002  spu_id 2c90c8647c3481e7017c35c181140001
    @Override
    public BaseResponse refreshGoods(SpuRequest spuRequest) {


        new Thread(new Runnable() {
            public void run() {
                try {

                    String sku_id = spuRequest.getSku_id();
                    boolean isExistSku = goodRepository.isExistSku(sku_id);

                    if (isExistSku){
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

                    }


                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                            DateUtil.format(new Date(),DateUtil.FMT_TIME_1),
                            "testLog",
                            Objects.isNull(ex.getMessage())?"": JSON.toJSONString(ex.getMessage()),
                            ex);
                }
            }
        }).start();


        return BaseResponse.SUCCESSFUL();
    }

    //所有
    @Override
    public BaseResponse refreshRedis() {

        new Thread(new Runnable() {
            public void run() {
                try {

                    marketLabel.doMarket();     //营销标签

                    bookTags.doGoods();         //图书商品

                    goodTags.doGoods();         //非书商品

                    cacheService.clear();       //释放内存

                    goodsCacheService.clear();
                    cacheService.clear();
                    System.out.println("finish:" + DitaUtil.getCurrentAllDate());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                            DateUtil.format(new Date(),DateUtil.FMT_TIME_1),
                            "testLog",
                            Objects.isNull(ex.getMessage())?"": JSON.toJSONString(ex.getMessage()),
                            ex);
                }
            }
        }).start();


        return BaseResponse.SUCCESSFUL();

    }

}
