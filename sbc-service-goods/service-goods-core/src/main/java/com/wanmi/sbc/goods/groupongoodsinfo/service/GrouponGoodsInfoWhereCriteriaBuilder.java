package com.wanmi.sbc.goods.groupongoodsinfo.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.YesOrNo;
import com.wanmi.sbc.goods.groupongoodsinfo.model.root.GrouponGoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>拼团活动商品信息表动态查询条件构建器</p>
 *
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
public class GrouponGoodsInfoWhereCriteriaBuilder {
    public static Specification<GrouponGoodsInfo> build(GrouponGoodsInfoQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<GrouponGoodsInfo, GoodsInfo> goodsInfosJoin = root.join("goodsInfo");
            // 批量查询-拼团商品IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getGrouponGoodsIdList())) {
                predicates.add(root.get("grouponGoodsId").in(queryRequest.getGrouponGoodsIdList()));
            }
            // 批量查询-SKU编号List
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsInfoIdList())) {
                predicates.add(root.get("goodsInfoId").in(queryRequest.getGoodsInfoIdList()));
            }
            // 批量查询-拼团商品IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsIdList())) {
                predicates.add(root.get("goodsId").in(queryRequest.getGoodsIdList()));
            }
            // 批量查询-拼团商品IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getGrouponActivityIdList())) {
                predicates.add(root.get("grouponActivityId").in(queryRequest.getGrouponActivityIdList()));
            }

            // 拼团商品ID
            if (StringUtils.isNotEmpty(queryRequest.getGrouponGoodsId())) {
                predicates.add(cbuild.equal(root.get("grouponGoodsId"), queryRequest.getGrouponGoodsId()));
            }

            // 模糊查询 - SKU编号
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 拼团价格
            if (queryRequest.getGrouponPrice() != null) {
                predicates.add(cbuild.equal(root.get("grouponPrice"), queryRequest.getGrouponPrice()));
            }

            // 起售数量
            if (queryRequest.getStartSellingNum() != null) {
                predicates.add(cbuild.equal(root.get("startSellingNum"), queryRequest.getStartSellingNum()));
            }

            // 限购数量
            if (queryRequest.getLimitSellingNum() != null) {
                predicates.add(cbuild.equal(root.get("limitSellingNum"), queryRequest.getLimitSellingNum()));
            }

            // 拼团活动ID
            if (StringUtils.isNotEmpty(queryRequest.getGrouponActivityId())) {
                predicates.add(cbuild.equal(root.get("grouponActivityId"), queryRequest.getGrouponActivityId()));
            }

            // 模糊查询 - 拼团分类ID
            if (StringUtils.isNotEmpty(queryRequest.getGrouponCateId())) {
                predicates.add(cbuild.like(root.get("grouponCateId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGrouponCateId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 店铺ID
            if (StringUtils.isNotEmpty(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 批量查询 - 店铺ID
            if (CollectionUtils.isNotEmpty(queryRequest.getStoreIds())) {
                predicates.add(root.get("storeId").in(queryRequest.getStoreIds()));
            }

            // 模糊查询 - SPU编号
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 商品销售数量
            if (queryRequest.getGoodsSalesNum() != null) {
                predicates.add(cbuild.equal(root.get("goodsSalesNum"), queryRequest.getGoodsSalesNum()));
            }

            // 订单数量
            if (queryRequest.getOrderSalesNum() != null) {
                predicates.add(cbuild.equal(root.get("orderSalesNum"), queryRequest.getOrderSalesNum()));
            }

            // 交易额
            if (queryRequest.getTradeAmount() != null) {
                predicates.add(cbuild.equal(root.get("tradeAmount"), queryRequest.getTradeAmount()));
            }

            // 成团后退单数量
            if (queryRequest.getRefundNum() != null) {
                predicates.add(cbuild.equal(root.get("refundNum"), queryRequest.getRefundNum()));
            }

            // 成团后退单金额
            if (queryRequest.getRefundAmount() != null) {
                predicates.add(cbuild.equal(root.get("refundAmount"), queryRequest.getRefundAmount()));
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 大于或等于 搜索条件:更新时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:更新时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }


            if (Objects.nonNull(queryRequest.getStarted())) {
                predicates.add(cbuild.lessThan(root.get("startTime"), LocalDateTime.now()));
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                predicates.add(cbuild.equal(root.get("auditStatus"), AuditStatus.CHECKED));

            }

            if (Objects.nonNull(queryRequest.getYesOrNo()) && queryRequest.getYesOrNo().equals(YesOrNo.YES)) {
                Join<GoodsInfo, GrouponGoodsInfo> join = root.join("goodsInfo");
                if (Objects.nonNull(queryRequest.getGoodsInfoName())) {
                    predicates.add(cbuild.like(join.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR
                            .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoName()))
                            .concat(StringUtil.SQL_LIKE_CHAR)));
                }
            }

            if (Objects.nonNull(queryRequest.getAuditStatus())) {
                predicates.add(cbuild.equal(root.get("auditStatus"), queryRequest.getAuditStatus()));
            }

            // 0：即将开始 1：进行中 2：已结束 3: 1&2
            if (Objects.nonNull(queryRequest.getStatus())) {
                switch (queryRequest.getStatus()) {
                    case 0:
                        predicates.add(cbuild.greaterThan(root.get("startTime"), LocalDateTime.now()));
                        break;
                    case 1:
                        predicates.add(cbuild.lessThan(root.get("startTime"), LocalDateTime.now()));
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                        break;
                    case 2:
                        predicates.add(cbuild.lessThan(root.get("endTime"), LocalDateTime.now()));
                        break;
                    case 3:
                        predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                        break;
                }
            }
            //过滤商品状态
            predicates.add(cbuild.equal(goodsInfosJoin.get("auditStatus"), CheckStatus.CHECKED));
            predicates.add(cbuild.equal(goodsInfosJoin.get("delFlag"), DeleteFlag.NO));
//            if(Objects.nonNull(queryRequest.getDistributionGoodsAudit())){
//                predicates.add(cbuild.notEqual(goodsInfosJoin.get("distributionGoodsAudit"), queryRequest.getDistributionGoodsAudit()));
//            }

//            predicates.add(cbuild.equal(goodsInfosJoin.get("addedFlag"), AddedFlag.YES.toValue()));
//            predicates.add(cbuild.greaterThan(goodsInfosJoin.get("stock"), NumberUtils.INTEGER_ZERO));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
