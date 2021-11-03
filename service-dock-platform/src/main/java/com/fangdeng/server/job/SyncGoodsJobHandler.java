package com.fangdeng.server.job;


import com.fangdeng.server.dto.SyncGoodsQueryDTO;
import com.fangdeng.server.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@JobHandler(value = "syncGoodsJobHandler")
@Component
@Slf4j
public class SyncGoodsJobHandler extends IJobHandler {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("=====同步博库商品start======");
        //商品同步，第一次全部同步，之后每天凌晨同步一次，同步昨天的数据
        SyncGoodsQueryDTO queryDTO = objectMapper.readValue(params, SyncGoodsQueryDTO.class);
        LocalDateTime startTime = LocalDate.now().minusDays(1).atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(0, 0, 0);
        String sTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(startTime);
        String eTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(endTime);
        try {
            if (!queryDTO.getIsAllSync()) {
                queryDTO.setStime(sTime);
                queryDTO.setEtime(eTime);
            }
        } catch (RuntimeException e) {
            log.error("同步博库商品参数错误,{}", e);
        }

        //查询博库商品
        goodsService.syncGoodsInfo(queryDTO);
        log.info("=====同步博库商品end======");
        return SUCCESS;
    }
}

