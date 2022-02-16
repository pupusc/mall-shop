package com.wanmi.sbc.job;

import com.wanmi.sbc.goods.api.provider.goods.GoodsVoteProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "voteNumberSyncJobHandler")
@Component
@Slf4j
public class VoteNumberSyncJobHandler extends IJobHandler {

    @Autowired
    private GoodsVoteProvider goodsVoteProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("同步投票次数开始");
        try {
            goodsVoteProvider.syncVoteNumber();
        }catch (Exception e) {
            log.error("投票数据同步失败");
            return FAIL;
        }
        log.info("同步投票次数成功");
        return SUCCESS;
    }


}
