package com.fangdeng.server.job;

import com.fangdeng.server.dto.SyncGoodsQueryDTO;
import com.fangdeng.server.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====同步博库价格start======");
        String sTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        String eTime = sTime;
        SyncGoodsQueryDTO queryDTO = objectMapper.readValue(params, SyncGoodsQueryDTO.class);
        if(StringUtils.isEmpty(queryDTO.getStime())) {
             queryDTO.setStime(sTime);
        }
        if(StringUtils.isEmpty(queryDTO.getEtime())) {
            queryDTO.setEtime(eTime);
        }
        goodsService.syncGoodsPrice(queryDTO);
        log.info("=====同步博库价格end======");
        return SUCCESS;
    }
}