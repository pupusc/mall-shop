package com.wanmi.sbc.task;

import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsProvider;
import com.wanmi.sbc.order.api.provider.exceptionoftradepoints.ExceptionOfTradePointsQueryProvider;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * @ClassName HomeIndexGoodsJobHandler
 * @Description 积分抵扣订单补偿定时任务
 * @Author caofang
 * @Date 2021/3/17 11:21
 **/
@JobHandler(value = "homeIndexGoodsJobHandler")
@Component
@Slf4j
public class HomeIndexGoodsJobHandler extends IJobHandler {

    private static final List<HandleStatus> HANDLE_STATUSES = Arrays.asList(HandleStatus.PROCESSING_FAILED, HandleStatus.PENDING);

    private static final List<Integer> errorTimes = Arrays.asList(1, 2);

    @Autowired
    private ExternalProvider externalProvider;

    @Autowired
    private ExceptionOfTradePointsQueryProvider exceptionOfTradePointsQueryProvider;

    @Autowired
    private ExceptionOfTradePointsProvider exceptionOfTradePointsProvider;

    @Override
    public ReturnT<String> execute(String paramStr) {
        //查询错误次数小于3

        return SUCCESS;
    }


}
