package com.fangdeng.server.job;

import com.fangdeng.server.dto.SyncGoodsQueryDTO;
import com.fangdeng.server.service.GoodsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * 同步价格，每天一次
 */
@JobHandler(value = "syncGoodsPriceJobHandler")
@Component
@Slf4j
public class SyncGoodsPriceJobHandler extends IJobHandler {

    @Autowired
    private GoodsService goodsService;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====同步博库价格start======");
        LocalDate startTime = LocalDate.now();
        String sTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startTime);
        String eTime = sTime;
        if(StringUtils.isNotBlank(params)) {
            String[] paramterArray = params.split(",");
             sTime = paramterArray[0];
             eTime = paramterArray[1];
        }
        goodsService.syncGoodsPrice(sTime,eTime);
        log.info("=====同步博库价格end======");
        return SUCCESS;
    }
}