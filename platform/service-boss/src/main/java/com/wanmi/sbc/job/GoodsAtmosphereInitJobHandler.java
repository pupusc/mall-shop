package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSyncVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@JobHandler(value = "GoodsAtmosphereInitJobHandler")
@Component
@Slf4j
public class GoodsAtmosphereInitJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====氛围同步start======");


        return SUCCESS;
    }
}
