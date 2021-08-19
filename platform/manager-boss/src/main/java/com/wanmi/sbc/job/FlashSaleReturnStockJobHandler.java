package com.wanmi.sbc.job;

import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.provider.flashsaleactivity.FlashSaleActivityQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityPageRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.bean.vo.FlashSaleActivityVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 定时任务Handler
 * 秒杀活动结束后商品还库存
 *
 * @author minchen
 */
@JobHandler(value = "flashSaleReturnStockJobHandler")
@Component
@Slf4j
public class FlashSaleReturnStockJobHandler extends IJobHandler {

    @Autowired
    private FlashSaleActivityQueryProvider flashSaleActivityQueryProvider;

    @Autowired
    private FlashSaleGoodsSaveProvider flashSaleGoodsSaveProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        // 每小时第5分钟执行 0 5 * * * ?
        // 过去10分钟内是否有刚过期的秒杀活动
        LocalDateTime startTime = LocalDateTime.now().minus(10, ChronoUnit.MINUTES).minus(Constants.FLASH_SALE_LAST_HOUR, ChronoUnit.HOURS);
        LocalDateTime endTime = LocalDateTime.now().minus(Constants.FLASH_SALE_LAST_HOUR, ChronoUnit.HOURS);
        FlashSaleActivityPageRequest activityPageRequest = FlashSaleActivityPageRequest.builder()
                .fullTimeBegin(startTime)
                .fullTimeEnd(endTime)
                .build();
        List<FlashSaleActivityVO> activityVOList = flashSaleActivityQueryProvider.page(activityPageRequest).getContext()
                .getFlashSaleActivityVOPage()
                .getContent();

        // 根据活动信息查询参与该场次的秒杀商品
        activityVOList.forEach(activityVO -> {
            FlashSaleGoodsQueryRequest goodsQueryRequest = FlashSaleGoodsQueryRequest.builder()
                    .activityDate(activityVO.getActivityDate())
                    .activityTime(activityVO.getActivityTime())
                    .build();
            flashSaleGoodsSaveProvider.activityEndReturnStock(goodsQueryRequest);
        });
        return SUCCESS;
    }

}
