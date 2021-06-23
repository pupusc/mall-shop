package com.wanmi.sbc.order.thirdplatformtrade.service;

import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.thirdplatformtrade.model.entity.LinkedMallLogisticsDetail;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.LinkedMallTradeLogistics;
import com.wanmi.sbc.order.thirdplatformtrade.repository.LinkedMallTradeLogisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 第三方渠道-linkedMall订单物流处理服务层
 * @Description: 第三方渠道-linkedMall订单物流处理服务层
 * @Autho yuhuiyu
 * @Date： 2020-8-22 13:07:07
 */
@Service
@Slf4j
public class LinkedMallTradeLogisticsService {

    @Autowired
    private LinkedMallTradeLogisticsRepository logisticsRepository;

    @Autowired
    private GeneratorService generatorService;

    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param logistics
     */
    public void add(LinkedMallTradeLogistics logistics) {
        logisticsRepository.save(logistics);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param logistics
     */
    public void update(LinkedMallTradeLogistics logistics) {
        logisticsRepository.save(logistics);
    }

    /**
     * 删除文档
     *
     * @param id
     */
    public void delete(String id) {
        logisticsRepository.deleteById(id);
    }

    /**
     * 根据linkedmall订单号和物流单号查询 linkedmall订单信息
     *
     * @param lmOrderId linkedmall订单号
     * @param mailNo linkedmall物流单号
     * @return
     */
    public LinkedMallTradeLogistics findTopByLmOrderIdAndMailNo(String lmOrderId, String mailNo) {
        return logisticsRepository.findTopByLmOrderIdAndMailNo(lmOrderId, mailNo).orElse(null);
    }

    /**
     * 修改文档
     * 专门用于数据修改服务,不允许数据新增的时候调用
     *
     * @param logistics
     */
    public void updateLinkedMallLogistics(LinkedMallTradeLogistics logistics) {
        LinkedMallTradeLogistics tradeLogistics =
                this.findTopByLmOrderIdAndMailNo(logistics.getLmOrderId(), logistics.getMailNo());
        if(Objects.nonNull(tradeLogistics)) {

            // 数据库已存在的情况，根据 物流信息最新一条时间 判断是否更新
            List<LinkedMallLogisticsDetail> oldLogistics = tradeLogistics.getLogisticsDetailList();
            List<LinkedMallLogisticsDetail> newLogistics = logistics.getLogisticsDetailList();
            if(CollectionUtils.isNotEmpty(oldLogistics) && CollectionUtils.isNotEmpty(newLogistics)) {
                String oldTimeStr = oldLogistics.get(0).getOcurrTimeStr();
                String newTimeStr = newLogistics.get(0).getOcurrTimeStr();
                if(oldTimeStr.equals(newTimeStr)) { return; }
            }

            logistics.setId(tradeLogistics.getId());
            logistics.setUpdateTime(LocalDateTime.now());
            this.update(logistics);
        } else {
            logistics.setId(generatorService.generate("LM"));
            logistics.setCreateTime(LocalDateTime.now());
            logistics.setUpdateTime(LocalDateTime.now());
            this.add(logistics);
        }
    }
}
