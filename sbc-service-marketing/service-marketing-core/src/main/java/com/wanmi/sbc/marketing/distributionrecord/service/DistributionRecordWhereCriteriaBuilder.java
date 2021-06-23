package com.wanmi.sbc.marketing.distributionrecord.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordQueryRequest;
import com.wanmi.sbc.marketing.distributionrecord.model.root.DistributionRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>DistributionRecord动态查询条件构建器</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
public class DistributionRecordWhereCriteriaBuilder {
    public static Specification<DistributionRecord> build(DistributionRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 批量查询-分销记录表主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getRecordIdList())) {
                predicates.add(root.get("recordId").in(queryRequest.getRecordIdList()));
            }

            // 订单货品Id
            if (queryRequest.getGoodsInfoId() != null && !"".equals(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.equal(root.get("goodsInfoId"), queryRequest.getGoodsInfoId()));
            }

            // 会员Id
            if (queryRequest.getCustomerId() != null && !"".equals(queryRequest.getCustomerId())) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 店铺Id
            if (queryRequest.getStoreId() != null && !"".equals(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 分销员Id
            if (queryRequest.getDistributorId() != null && !"".equals(queryRequest.getDistributorId())) {
                predicates.add(cbuild.equal(root.get("distributorId"), queryRequest.getDistributorId()));
            }



            // 分销员customerId
            if (queryRequest.getDistributorCustomerId()!= null && !"".equals(queryRequest.getDistributorCustomerId())) {
                predicates.add(cbuild.equal(root.get("distributorCustomerId"), queryRequest.getDistributorCustomerId()));
            }

            // 模糊查询 - 订单交易号
            if (StringUtils.isNotEmpty(queryRequest.getTradeId())) {
                predicates.add(cbuild.like(root.get("tradeId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTradeId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:付款时间开始
            if (queryRequest.getPayTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("payTime"),
                        queryRequest.getPayTimeBegin()));
            }
            // 小于或等于 搜索条件:付款时间截止
            if (queryRequest.getPayTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("payTime"),
                        queryRequest.getPayTimeEnd()));
            }

            // 大于或等于 搜索条件:订单完成时间开始
            if (queryRequest.getFinishTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("finishTime"),
                        queryRequest.getFinishTimeBegin()));
            }

            // 小于或等于 搜索条件:订单完成时间截止
            if (queryRequest.getFinishTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("finishTime"),
                        queryRequest.getFinishTimeEnd()));
            }

            // 大于或等于 搜索条件:佣金入账时间开始
            if (queryRequest.getMissionReceivedTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("missionReceivedTime"),
                        queryRequest.getMissionReceivedTimeBegin()));
            }
            // 小于或等于 搜索条件:佣金入账时间截止
            if (queryRequest.getMissionReceivedTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("missionReceivedTime"),
                        queryRequest.getMissionReceivedTimeEnd()));
            }

            // 订单单个商品金额
            if (queryRequest.getOrderGoodsPrice() != null) {
                predicates.add(cbuild.equal(root.get("orderGoodsPrice"), queryRequest.getOrderGoodsPrice()));
            }

            // 商品的数量
            if (queryRequest.getOrderGoodsCount() != null) {
                predicates.add(cbuild.equal(root.get("orderGoodsCount"), queryRequest.getOrderGoodsCount()));
            }

            // 单个货品的佣金
            if (queryRequest.getCommissionGoods() != null) {
                predicates.add(cbuild.equal(root.get("commissionGoods"), queryRequest.getCommissionGoods()));
            }

            // 佣金是否入账
            if (queryRequest.getCommissionState() != null) {
                predicates.add(cbuild.equal(root.get("commissionState"), queryRequest.getCommissionState()));
            }

            //是否删除
            if (queryRequest.getDeleteFlag() != null) {
                predicates.add(cbuild.equal(root.get("deleteFlag"), queryRequest.getDeleteFlag()));
            }

            //按支付时间倒序排列
            cquery.orderBy(cbuild.desc(root.get("payTime")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
