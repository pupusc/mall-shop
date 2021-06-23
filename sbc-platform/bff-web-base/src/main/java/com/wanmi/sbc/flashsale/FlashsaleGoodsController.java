package com.wanmi.sbc.flashsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.flashsalegoods.IsInProgressReq;
import com.wanmi.sbc.goods.api.response.flashsalegoods.IsInProgressResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @program: sbc-micro-service
 * @description: 秒杀商品
 * @create: 2019-06-17 10:30
 **/
@RestController
@RequestMapping("/flashsalebase")
@Api(tags = "FlashsaleGoodsController", description = "秒杀商品")
public class FlashsaleGoodsController {
    @Autowired
    FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    /**
     * @Description: 商品是否正在抢购活动中
     * @param goodsId
     * @Author: Bob
     * @Date: 2019-06-17 10:41
     */
    @ApiOperation(value = "商品是否正在抢购活动中")
    @GetMapping("/{goodsId}/isInProgress")
    public BaseResponse<IsInProgressResp> isInProgress(@PathVariable String goodsId){
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = begin.minus(Constants.FLASH_SALE_LAST_HOUR, ChronoUnit.HOURS);
        return flashSaleGoodsQueryProvider.isInProgress(IsInProgressReq.builder().goodsId(goodsId).begin(begin)
                .end(end).build());
    }
}