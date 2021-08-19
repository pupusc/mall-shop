package com.wanmi.sbc.marketing.distributionrecord.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerListRequest;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordModifyRequest;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordQueryRequest;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordUpdateInfo;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordByInviteeIdResponse;
import com.wanmi.sbc.marketing.bean.dto.QueryConditionTime;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import com.wanmi.sbc.marketing.distributionrecord.model.root.DistributionRecord;
import com.wanmi.sbc.marketing.distributionrecord.repository.DistributionRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>DistributionRecord业务逻辑</p>
 *
 * @author baijz
 * @date 2019-02-25 10:28:48
 */
@Slf4j
@Service("DistributionRecordService")
public class DistributionRecordService {
    @Autowired
    private DistributionRecordRepository distributionRecordRepository;

    /**
     * 新增DistributionRecord
     *
     * @author baijz
     */
    @Transactional
    public List<DistributionRecord> add(List<DistributionRecord> entity) {
        return distributionRecordRepository.saveAll(entity);
    }

    /**
     * 修改DistributionRecord
     *
     * @author baijz
     */
    @Transactional
    public int modify(DistributionRecordModifyRequest request) {
        AtomicInteger backStatu = new AtomicInteger(1);
        List<DistributionRecordUpdateInfo> recordUpdateInfos = request.getUpdateInfos();
        if (!CollectionUtils.isEmpty(recordUpdateInfos) && recordUpdateInfos.size() > 0) {
            recordUpdateInfos.forEach(record -> {
                String tradeId = record.getTradeId();
                String goodsInfoId = record.getGoodsInfoId();
                if (!ObjectUtils.isEmpty(record.getGoodsCount())&&record.getGoodsCount() > 0) {
                    //更新商品的数量
                    backStatu.set(this.updateOrderGoodsCountByTradeId(tradeId, goodsInfoId, record.getGoodsCount()));
                }
                if (!ObjectUtils.isEmpty(record.getFinishTime())) {
                    //更新订单完成时间
                    backStatu.set(this.updateFinishTimeByTradeId(tradeId, record.getFinishTime()));
                }
                if (!ObjectUtils.isEmpty(record.getMissionReceivedTime())) {
                    //更新佣金入账时间
                    backStatu.set(this.updateCommissionReceivedTimeByTradeId(tradeId, goodsInfoId, record
                            .getMissionReceivedTime()));
                }
                if (!ObjectUtils.isEmpty(record.getGoodsCount())&&record.getGoodsCount() == 0) {
                    //分校记录软删除
                    this.deleteByTradeIdAndGoodsInfoId(tradeId, goodsInfoId);
                }
            });
        }
        return backStatu.get();
    }

    /**
     * 单个软删除DistributionRecord
     *
     * @author baijz
     */
    @Transactional
    public void deleteByTradeIdAndGoodsInfoId(String tradeId, String goodsInfoId) {
        DistributionRecord distributionRecord = this.getByTradIdAndGoodsInfoId(tradeId, goodsInfoId);
        if(Objects.isNull(distributionRecord)){
            log.info("分销记录软删除：没有查询到记录tradeId:".concat(tradeId).concat("goodsInfoId:").concat(goodsInfoId));
            return;
        }
        distributionRecord.setDeleteFlag(DeleteFlag.YES);
        distributionRecordRepository.save(distributionRecord);
    }

    /**
     * 批量删除DistributionRecord
     *
     * @author baijz
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        distributionRecordRepository.deleteAll(ids.stream().map(id -> {
            DistributionRecord entity = new DistributionRecord();
            entity.setRecordId(id);
            return entity;
        }).collect(Collectors.toList()));
    }

    /**
     * 单个查询DistributionRecord
     *
     * @author baijz
     */
    public DistributionRecord getById(String id) {
        return distributionRecordRepository.findById(id).get();
    }

    /**
     * 分页查询DistributionRecord
     *
     * @author baijz
     */
    public Page<DistributionRecord> page(DistributionRecordQueryRequest queryReq) {
        return distributionRecordRepository.findAll(
                DistributionRecordWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询DistributionRecord
     *
     * @author baijz
     */
    public List<DistributionRecord> list(DistributionRecordQueryRequest queryReq) {
        return distributionRecordRepository.findAll(DistributionRecordWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author baijz
     */
    public DistributionRecordVO wrapperVo(DistributionRecord distributionRecord) {
        if (distributionRecord != null) {
            DistributionRecordVO distributionRecordVO = new DistributionRecordVO();
            KsBeanUtil.copyPropertiesThird(distributionRecord, distributionRecordVO);
            return distributionRecordVO;
        }
        return null;
    }

    /**
     * 根据货品id和订单交易号查询分销记录
     *
     * @param tradeId
     * @param goodsInfoId
     * @return
     */
    public DistributionRecord getByTradIdAndGoodsInfoId(String tradeId, String goodsInfoId) {
        return distributionRecordRepository.findDistributionRecordByGoodsInfoIdAndTradeId(goodsInfoId,tradeId);
    }

    /**
     * 根据订单交易号查询分销记录
     *
     * @param tradeId
     * @return
     */
    public List<DistributionRecord> findDistributionRecordsByTradeId(String tradeId) {
        return distributionRecordRepository.findDistributionRecordsByTradeId(tradeId);
    }

    /**
     * 根据订单号更新分销记录的完成时间
     *
     * @param tradeId
     * @return
     */
    public int updateFinishTimeByTradeId(String tradeId, LocalDateTime finishTime) {
        return distributionRecordRepository.updateFinishTimeByTradeId(tradeId, finishTime);
    }

    /**
     * 根据订单号和货品Id更新分销记录的入账时间和入账状态
     *
     * @param tradeId
     * @return
     */
    public int updateCommissionReceivedTimeByTradeId(String tradeId, String goodsInfoId, LocalDateTime receivedTime) {
        return distributionRecordRepository.updateCommissionReceivedTimeByTradeIdAndGoodsInfoId(tradeId, receivedTime,
                goodsInfoId);
    }

    /**
     * 根据订单号和货品Id更新分销记录的货品数量
     *
     * @param tradeId
     * @return
     */
    public int updateOrderGoodsCountByTradeId(String tradeId, String goodsInfoId, Long orderGoodsCount) {
        return distributionRecordRepository.updateOrderGoodsCountByTradeIdAndGoodsInfoId(tradeId, orderGoodsCount,
                goodsInfoId);
    }

    /**
     * 根据订单号软删除
     *
     * @param tradeId
     * @return
     */
    @Transactional
    public int deleteByTradeId(String tradeId) {
        return distributionRecordRepository.updateDeleteFlagByTradeId(tradeId);
    }


    /**
     * 获取分销员的业绩信息
     *
     * @param distributorId
     * @return
     */
    public DistributionRecordByInviteeIdResponse getPerformanceByInviteeId(String distributorId) {
        QueryConditionTime conditionTime = new QueryConditionTime();
        DistributionRecordQueryRequest request = new DistributionRecordQueryRequest();
        BigDecimal yesterdaySales = BigDecimal.ZERO;
        BigDecimal yesterdayEstimatedReturn = BigDecimal.ZERO;
        BigDecimal monthSales = BigDecimal.ZERO;
        BigDecimal monthEstimatedReturn = BigDecimal.ZERO;
        request.setDeleteFlag(DeleteFlag.NO);
        request.setDistributorId(distributorId);
        request.setPayTimeBegin(conditionTime.getYestodayTime());
        request.setPayTimeEnd(conditionTime.getYesterdayEndTime());
        //昨日的分销记录
        List<DistributionRecord> distributionRecords = this.list(request);
        if (!CollectionUtils.isEmpty(distributionRecords)) {
            //根据分销员的Ids查询分销员的信息，并插入
            List<String> distributorIds = distributionRecords.stream().filter(d -> d.getDistributorId() != null).map(v -> v.getDistributorId()).distinct().collect(toList());
            DistributionCustomerListRequest customerListRequest = new DistributionCustomerListRequest();
            customerListRequest.setDistributionIdList(distributorIds);
            for (DistributionRecord distributionRecord : distributionRecords) {
                if (distributionRecord.getCustomerId().equals(distributionRecord.getDistributorCustomerId())) {
                    continue;
                }
                yesterdaySales = yesterdaySales.add(BigDecimal.valueOf(distributionRecord.getOrderGoodsCount()).multiply(distributionRecord.getOrderGoodsPrice()));
                yesterdayEstimatedReturn = yesterdayEstimatedReturn.add(distributionRecord.getCommissionGoods().multiply(BigDecimal.valueOf(distributionRecord.getOrderGoodsCount())));
            }

        }
        request.setPayTimeBegin(conditionTime.getThisMonthTime());
        request.setPayTimeEnd(LocalDateTime.now());
        //本月分销记录
        List<DistributionRecord> distributionRecordList = this.list(request);
        if (!CollectionUtils.isEmpty(distributionRecordList)) {
            for (DistributionRecord distributionRecord : distributionRecordList) {
                if (distributionRecord.getCustomerId().equals(distributionRecord.getDistributorCustomerId())) {
                    continue;
                }
                monthEstimatedReturn = monthEstimatedReturn.add(distributionRecord.getCommissionGoods().multiply(BigDecimal.valueOf(distributionRecord.getOrderGoodsCount())));
                monthSales = monthSales.add(BigDecimal.valueOf(distributionRecord.getOrderGoodsCount()).multiply(distributionRecord.getOrderGoodsPrice()));
            }

        }
        DistributionRecordByInviteeIdResponse distributionRecordByInviteeIdResponse = new DistributionRecordByInviteeIdResponse();
        distributionRecordByInviteeIdResponse.setYesterdaySales(yesterdaySales);
        distributionRecordByInviteeIdResponse.setYesterdayEstimatedReturn(yesterdayEstimatedReturn);
        distributionRecordByInviteeIdResponse.setMonthSales(monthSales);
        distributionRecordByInviteeIdResponse.setMonthEstimatedReturn(monthEstimatedReturn);
        return distributionRecordByInviteeIdResponse;
    }


}
