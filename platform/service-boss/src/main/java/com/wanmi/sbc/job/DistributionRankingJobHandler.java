package com.wanmi.sbc.job;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerRankingSaveProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingInitRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingInitResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 定时任务Handler
 * 生成排行榜数据
 * 12点 2点 4点生成
 * 4点生成数据主要是确保社交分销邀新记录有效邀新处理完毕
 */
@JobHandler(value = "distributionRankingJobHandler")
@Component
@Slf4j
public class DistributionRankingJobHandler extends IJobHandler {

    /**
     * 排行榜
     */
    @Autowired
    private DistributionCustomerRankingSaveProvider distributionCustomerRankingSaveProvider;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        DistributionCustomerRankingInitRequest request = new DistributionCustomerRankingInitRequest();
        request.setTargetDate(LocalDate.now());
        if (StringUtils.isNotBlank(param)) {
            request.setTargetDate(LocalDate.parse(param));
        }
        XxlJobLogger.log("生成排行榜数据开始参数:" + JSONObject.toJSONString(request));
        BaseResponse<DistributionCustomerRankingInitResponse> response = distributionCustomerRankingSaveProvider.initRankingData(request);
        XxlJobLogger.log("生成排行榜数据结束:" + JSONObject.toJSONString(response));
        return SUCCESS;
    }


}
