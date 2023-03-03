package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public BaseResponse test(){

        //goodTags.getRedis("P735546359");        //图书
        //goodTags.getRedis("P989359460");        //非书

        System.out.println("begin:" + DitaUtil.getCurrentAllDate());

        bookTags.doGoods();         //图书商品

        goodTags.doGoods();         //非书商品

        System.out.println("end:" + DitaUtil.getCurrentAllDate());

        return BaseResponse.SUCCESSFUL();
    }

}
