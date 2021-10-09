package com.fangdeng.server.job;

import com.fangdeng.server.dto.SyncGoodsQueryDTO;
import com.fangdeng.server.service.GoodsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 同步库存
 */
@JobHandler(value = "syncGoodsStockJobHandler")
@Component
@Slf4j
public class SyncGoodsStockJobHandler extends IJobHandler {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        log.info("全量同步博库商品库存任务执行开始");
        try {
            SyncGoodsQueryDTO queryDTO = objectMapper.readValue(params, SyncGoodsQueryDTO.class);
            goodsService.syncGoodsStock(queryDTO);
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("同步ERP库存定时任务,参数错误", e);
            return FAIL;
        } finally {

        }
    }
}