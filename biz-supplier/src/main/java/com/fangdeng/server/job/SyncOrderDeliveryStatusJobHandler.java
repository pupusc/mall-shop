package com.fangdeng.server.job;

import com.fangdeng.server.service.GoodsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;




/**
 * 同步订单发货状态
 */
@JobHandler(value = "syncOrderDeliveryStatusJobHandler")
@Component
@Slf4j
public class SyncOrderDeliveryStatusJobHandler extends IJobHandler {


    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====订单发货状态同步开始======");
        String[] paramterArray = params.split(",");
        int size = 0;
        String ptid = StringUtils.EMPTY;
        try {
            size = Integer.parseInt(paramterArray[0]);
            if (paramterArray.length>1){
                ptid = paramterArray[1];
            }
        } catch (RuntimeException e) {
            log.error("调用接口更新订单发货状态,参数错误,采用默认 200,{}", e);
        }

        log.info("=====订单发货状态同步结束======");
        return SUCCESS;
    }

}