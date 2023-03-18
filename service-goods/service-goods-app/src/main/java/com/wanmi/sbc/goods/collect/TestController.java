package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试redis同步类
 */
@RestController
@Slf4j
public class TestController {

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
      @Autowired
      BookCacheService bookCacheService;

    @GetMapping("/test")
    public BaseResponse test(){

        //启动setting,goods服务
        //goodTags.getRedis("P735546359");         //图书
        //goodTags.getRedis("P989359460");        //非书

        //1小时37分
        System.out.println("begin:" + DitaUtil.getCurrentAllDate());

         bookTags.doGoods();         //图书商品

        goodTags.doGoods();         //非书商品

        marketLabel.doMarket();     //营销标签

        bookCacheService.clear();  //释放内存
        goodsCacheService.clear();
        cacheService.clear();
  //19:01:01~19:16:41    15分钟

        System.out.println("end:" + DitaUtil.getCurrentAllDate());

        return BaseResponse.SUCCESSFUL();
    }


}
