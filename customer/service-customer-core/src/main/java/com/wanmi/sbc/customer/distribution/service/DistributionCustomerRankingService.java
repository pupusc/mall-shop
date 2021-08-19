package com.wanmi.sbc.customer.distribution.service;


import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingInitRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingQueryRequest;
import com.wanmi.sbc.customer.bean.enums.RankingType;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerRankingVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRanking;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomerRankingBase;
import com.wanmi.sbc.customer.distribution.repository.DistributionCustomerRankingRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * <p>用户分销排行榜业务逻辑</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@Service("DistributionCustomerRankingService")
public class DistributionCustomerRankingService {
    @Autowired
    private DistributionCustomerRankingRepository distributionCustomerRankingRepository;

    /**
     * 新增用户分销排行榜
     * @author lq
     */
    @Transactional
    public DistributionCustomerRanking add(DistributionCustomerRanking entity) {
        distributionCustomerRankingRepository.save(entity);
        return entity;
    }

    /**
     * 修改用户分销排行榜
     * @author lq
     */
    @Transactional
    public DistributionCustomerRanking modify(DistributionCustomerRanking entity) {
        distributionCustomerRankingRepository.save(entity);
        return entity;
    }

    /**
     * 单个查询用户分销排行榜
     * @author lq
     */
    public DistributionCustomerRanking getById(String id) {
        return distributionCustomerRankingRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询用户分销排行榜
     * @author lq
     */
    public Page<DistributionCustomerRanking> page(DistributionCustomerRankingQueryRequest queryReq) {
        return distributionCustomerRankingRepository.findAll(
                DistributionCustomerRankingWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }


    /**
     * 将实体包装成VO
     * @author lq
     */
    public DistributionCustomerRankingVO wrapperVo(DistributionCustomerRanking distributionCustomerRanking) {
        if (distributionCustomerRanking != null) {
            DistributionCustomerRankingVO distributionCustomerRankingVO = new DistributionCustomerRankingVO();
            KsBeanUtil.copyPropertiesThird(distributionCustomerRanking, distributionCustomerRankingVO);
            return distributionCustomerRankingVO;
        }
        return null;
    }


    /**
     * 初始化排行榜数据
     * --可以扩展存放历史数据
     */
    @Transactional
    public List<DistributionCustomerRanking> initRankingData(DistributionCustomerRankingInitRequest request) {
        LocalDate targetDate = request.getTargetDate();
        //聚合结果数据<customerId,排行信息>
        Map<String, DistributionCustomerRanking> rankingMap = new HashMap<>();
        //统计最近七天邀新人数
        countWeekInviteCount(rankingMap, targetDate);
        //统计最近七天有效邀新人数
        countWeekInviteAvailableCount(rankingMap, targetDate);
        //统计销售额
        countWeekSaleAmount(rankingMap, targetDate);
        //统计预估收益
        countWeekCommission(rankingMap, targetDate);
        // 删除当天数据
        distributionCustomerRankingRepository.deleteByDate(targetDate);
        //批量保存
        List<DistributionCustomerRanking> result = distributionCustomerRankingRepository.saveAll(rankingMap.values());
        //批量删除-删除10天之前的数据
        distributionCustomerRankingRepository.deleteFromDate(LocalDate.now().minusDays(10));
        return result;
    }


    /**
     * 统计客户统计最近七天邀新人数
     */
    public Map<String, DistributionCustomerRanking> countWeekInviteCount(Map<String, DistributionCustomerRanking> rankingMap, LocalDate targetDate) {

        Pageable pageable =  PageRequest.of(0, 100);

        Date start = Date.from(targetDate.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(targetDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<DistributionCustomerRankingBase> resultList = distributionCustomerRankingRepository.countWeekInviteCount(start, end, pageable);
        List<DistributionCustomerRanking> voList = convertFromNativeSQLResult(resultList, RankingType.INVITECOUNT);
        voList.forEach(v -> {
            // 判断集合中是否已有该用户
            if (!rankingMap.containsKey(v.getCustomerId())) {
                DistributionCustomerRanking vo = initDistributionCustomerRanking(targetDate);
                vo.setCustomerId(v.getCustomerId());
                rankingMap.put(v.getCustomerId(), vo);
            }
            rankingMap.get(v.getCustomerId()).setInviteCount(v.getInviteCount());
        });
        return rankingMap;
    }

    /**
     * 统计客户统计最近七天有效邀新人数
     */
    public Map<String, DistributionCustomerRanking> countWeekInviteAvailableCount(Map<String, DistributionCustomerRanking> rankingMap, LocalDate targetDate) {
        Pageable pageable =  PageRequest.of(0, 100);
        Date start = Date.from(targetDate.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(targetDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<DistributionCustomerRankingBase> resultList = distributionCustomerRankingRepository.countWeekInviteAvailableCount(start, end, pageable);
        List<DistributionCustomerRanking> voList = convertFromNativeSQLResult(resultList, RankingType.INVITEAVAILABLECOUNT);
        voList.forEach(v -> {
            if (!rankingMap.containsKey(v.getCustomerId())) {
                DistributionCustomerRanking vo = initDistributionCustomerRanking(targetDate);
                vo.setCustomerId(v.getCustomerId());
                rankingMap.put(v.getCustomerId(), vo);
            }
            rankingMap.get(v.getCustomerId()).setInviteAvailableCount(v.getInviteAvailableCount());
        });
        return rankingMap;
    }

    /**
     * 统计客户统计最近七天销售额
     */
    public Map<String, DistributionCustomerRanking> countWeekSaleAmount(Map<String, DistributionCustomerRanking> rankingMap, LocalDate targetDate) {
        Pageable pageable =  PageRequest.of(0, 100);
        Date start = Date.from(targetDate.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(targetDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<DistributionCustomerRankingBase> resultList = distributionCustomerRankingRepository.countWeekSaleAmount(start, end, pageable);
        List<DistributionCustomerRanking> voList = convertFromNativeSQLResult(resultList, RankingType.SALEAMOUNT);
        voList.forEach(v -> {
            if (!rankingMap.containsKey(v.getCustomerId())) {
                DistributionCustomerRanking vo = initDistributionCustomerRanking(targetDate);
                vo.setCustomerId(v.getCustomerId());
                rankingMap.put(v.getCustomerId(), vo);
            }
            rankingMap.get(v.getCustomerId()).setSaleAmount(v.getSaleAmount());
        });
        return rankingMap;
    }


    /**
     * 统计客户统计最近七天预估收益
     */
    public Map<String, DistributionCustomerRanking> countWeekCommission(Map<String, DistributionCustomerRanking> rankingMap, LocalDate targetDate) {
        Pageable pageable =  PageRequest.of(0, 100);
        Date start = Date.from(targetDate.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(targetDate.minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<DistributionCustomerRankingBase> resultList = distributionCustomerRankingRepository.countWeekCommission(start, end, pageable);
        List<DistributionCustomerRanking> voList = convertFromNativeSQLResult(resultList, RankingType.COMMISSION);
        voList.forEach(v -> {
            if (!rankingMap.containsKey(v.getCustomerId())) {
                DistributionCustomerRanking vo = initDistributionCustomerRanking(targetDate);
                vo.setCustomerId(v.getCustomerId());
                rankingMap.put(v.getCustomerId(), vo);
            }
            rankingMap.get(v.getCustomerId()).setCommission(v.getCommission());
        });
        return rankingMap;
    }


    private List<DistributionCustomerRanking> convertFromNativeSQLResult(List<DistributionCustomerRankingBase> resultList, RankingType type) {
        List<DistributionCustomerRanking> voList = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultList)) {
            return voList;
        }
        for (DistributionCustomerRankingBase obj : resultList) {
            DistributionCustomerRanking vo = initDistributionCustomerRanking(LocalDate.now());
            vo.setCustomerId(obj.getCustomerId());
            switch (type) {
                case INVITECOUNT:
                    vo.setInviteCount(obj.getNum().intValue());
                    break;
                case INVITEAVAILABLECOUNT:
                    vo.setInviteAvailableCount(obj.getNum().intValue());
                    break;
                case SALEAMOUNT:
                    vo.setSaleAmount(obj.getTotalAmount());
                    break;
                case COMMISSION:
                    vo.setCommission(obj.getTotalAmount());
                    break;
            }
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 初始化一个排行数据
     */
    private DistributionCustomerRanking initDistributionCustomerRanking(LocalDate targetDate) {
        DistributionCustomerRanking vo = new DistributionCustomerRanking();
        vo.setInviteCount(0);
        vo.setInviteAvailableCount(0);
        vo.setSaleAmount(BigDecimal.ZERO);
        vo.setCommission(BigDecimal.ZERO);
        vo.setCreateTime(LocalDateTime.now());
        vo.setTargetDate(targetDate);
        return vo;
    }


}
