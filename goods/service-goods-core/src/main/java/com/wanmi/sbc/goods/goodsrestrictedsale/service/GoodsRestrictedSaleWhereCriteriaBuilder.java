package com.wanmi.sbc.goods.goodsrestrictedsale.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedSaleQueryRequest;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import com.wanmi.sbc.goods.goodsrestrictedsale.model.root.GoodsRestrictedSale;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>限售配置动态查询条件构建器</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
public class GoodsRestrictedSaleWhereCriteriaBuilder {
    public static Specification<GoodsRestrictedSale> build(GoodsRestrictedSaleQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<GoodsRestrictedSale, GoodsInfo> pointsGoodsGoodsInfoJoin = root.join("goodsInfo");

            // 批量查询-限售主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getRestrictedIdList())) {
                predicates.add(root.get("restrictedId").in(queryRequest.getRestrictedIdList()));
            }

            // 批量查询-skuIds
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsInfoIds())) {
                predicates.add(root.get("goodsInfoId").in(queryRequest.getGoodsInfoIds()));
            }

            // 限售主键
            if (queryRequest.getRestrictedId() != null) {
                predicates.add(cbuild.equal(root.get("restrictedId"), queryRequest.getRestrictedId()));
            }

            // 店铺ID
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 货品的skuId
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.equal(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 限售方式 0: 按订单 1：按会员
            if (queryRequest.getRestrictedType() != null) {
                predicates.add(cbuild.equal(root.get("restrictedType"), queryRequest.getRestrictedType()));
            }

            // 是否每人限售标识 
            if (queryRequest.getRestrictedPrePersonFlag() != null) {
                predicates.add(cbuild.equal(root.get("restrictedPrePersonFlag"), queryRequest.getRestrictedPrePersonFlag()));
            }

            // 是否每单限售的标识
            if (queryRequest.getRestrictedPreOrderFlag() != null) {
                predicates.add(cbuild.equal(root.get("restrictedPreOrderFlag"), queryRequest.getRestrictedPreOrderFlag()));
            }

            // 是否指定会员限售的标识
            if (queryRequest.getRestrictedAssignFlag() != null) {
                predicates.add(cbuild.equal(root.get("restrictedAssignFlag"), queryRequest.getRestrictedAssignFlag()));
            }

            // 个人限售的方式(  0:终生限售  1:周期限售)
            if (queryRequest.getPersonRestrictedType() != null) {
                predicates.add(cbuild.equal(root.get("personRestrictedType"), queryRequest.getPersonRestrictedType()));
            }

            // 个人限售的周期 (0:周   1:月  2:年)
            if (queryRequest.getPersonRestrictedCycle() != null) {
                predicates.add(cbuild.equal(root.get("personRestrictedCycle"), queryRequest.getPersonRestrictedCycle()));
            }

            // 限售数量
            if (queryRequest.getRestrictedNum() != null) {
                predicates.add(cbuild.equal(root.get("restrictedNum"), queryRequest.getRestrictedNum()));
            }

            // 起售数量
            if (queryRequest.getStartSaleNum() != null) {
                predicates.add(cbuild.equal(root.get("startSaleNum"), queryRequest.getStartSaleNum()));
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

            //商品名称模糊查询
            if(StringUtils.isNotBlank(queryRequest.getGoodsInfoName())){
                predicates.add(cbuild.like(pointsGoodsGoodsInfoJoin.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - skuNo查询
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoNo())) {
                predicates.add(cbuild.like(pointsGoodsGoodsInfoJoin.get("goodsInfoNo"),StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 删除标识
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
