package com.wanmi.sbc.job;

import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "wxGoodsAuditSyncJobHandler")
@Component
@Slf4j
public class WxGoodsAuditSyncJobHandler extends IJobHandler {

    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;

    @Override
    public ReturnT<String> execute(String goodsId) throws Exception {
        log.info("开始同步视频号商品审核结果,参数: {}", goodsId);
        wxMiniGoodsProvider.goodsAuditSync(goodsId);
        log.info("同步视频号商品审核结果结束");
        return SUCCESS;
    }
}
