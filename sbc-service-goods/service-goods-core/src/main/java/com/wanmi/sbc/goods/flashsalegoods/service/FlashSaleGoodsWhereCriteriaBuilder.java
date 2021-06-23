package com.wanmi.sbc.goods.flashsalegoods.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.flashsalegoods.model.root.FlashSaleGoods;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>抢购商品表动态查询条件构建器</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
public class FlashSaleGoodsWhereCriteriaBuilder {
    public static Specification<FlashSaleGoods> build(FlashSaleGoodsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<FlashSaleGoods, Goods> flashSaleGoodsJoin = root.join("goods");
            Join<FlashSaleGoods, GoodsInfo> flashSaleGoodsInfoJoin = root.join("goodsInfo");
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsinfoIds())) {
                predicates.add(root.get("goodsInfoId").in(queryRequest.getGoodsinfoIds()));
            }

            // id
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 活动日期：2019-06-11
            if (StringUtils.isNotEmpty(queryRequest.getActivityDate())) {
                predicates.add(cbuild.equal(root.get("activityDate"), queryRequest.getActivityDate()));
            }

            // 模糊查询 - 活动时间：13:00 activityFullTime
            if (StringUtils.isNotEmpty(queryRequest.getActivityTime())) {
                predicates.add(cbuild.equal(root.get("activityTime"), queryRequest.getActivityTime()));
            }

            // 模糊查询 - 活动时间：13:00 activityFullTime
            if (StringUtils.isNotEmpty(queryRequest.getActivityTime())) {
                predicates.add(cbuild.like(root.get("activityTime"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getActivityTime()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }
            // 活动日期+时间(场次)
            if (queryRequest.getActivityFullTime() != null) {
                predicates.add(cbuild.equal(root.get("activityFullTime"), queryRequest.getPrice()));
            }

            // 模糊查询 - skuID
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - spuID
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 抢购价
            if (queryRequest.getPrice() != null) {
                predicates.add(cbuild.equal(root.get("price"), queryRequest.getPrice()));
            }

            // 抢购库存
            if (queryRequest.getStock() != null) {
                predicates.add(cbuild.equal(root.get("stock"), queryRequest.getStock()));
            }

            // 抢购销量
            if (queryRequest.getSalesVolume() != null) {
                predicates.add(cbuild.equal(root.get("salesVolume"), queryRequest.getSalesVolume()));
            }

            // 模糊查询 - 分类ID
            if (queryRequest.getCateId() != null) {
                predicates.add(cbuild.equal(root.get("cateId"), queryRequest.getCateId()));
            }

            // 限购数量
            if (queryRequest.getMaxNum() != null) {
                predicates.add(cbuild.equal(root.get("maxNum"), queryRequest.getMaxNum()));
            }

            // 起售数量
            if (queryRequest.getMinNum() != null) {
                predicates.add(cbuild.equal(root.get("minNum"), queryRequest.getMinNum()));
            }

            // 商家ID
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 批量查询 - 店铺ID
            if (CollectionUtils.isNotEmpty(queryRequest.getStoreIds())) {
                predicates.add(root.get("storeId").in(queryRequest.getStoreIds()));
            }

            // 包邮标志，0：不包邮 1:包邮
            if (queryRequest.getPostage() != null) {
                predicates.add(cbuild.equal(root.get("postage"), queryRequest.getPostage()));
            }

            // 删除标志，0:未删除 1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 商品名称模糊查询
            if (StringUtils.isNotEmpty(queryRequest.getGoodsName())) {
                predicates.add(cbuild.like(flashSaleGoodsJoin.get("goodsName"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(queryRequest.getGoodsName().trim())).append("%").toString()));
            }

            // SKU编号查询
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoNo())) {
                predicates.add(cbuild.like(flashSaleGoodsInfoJoin.get("goodsInfoNo"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoNo().trim())).append("%").toString()));
            }

            if (queryRequest.getQueryDataType() != null) {
                LocalDateTime begin = LocalDateTime.now();
                LocalDateTime end = begin.minus(Constants.FLASH_SALE_LAST_HOUR, ChronoUnit.HOURS);
                switch (queryRequest.getQueryDataType()) {
                    case 0:
                        //未开始
                        predicates.add(cbuild.greaterThan(root.get("activityFullTime"), begin));
                        break;
                    case 1:
                        //正在进行
                        predicates.add(cbuild.lessThanOrEqualTo(root.get("activityFullTime"), begin));
                        predicates.add(cbuild.greaterThan(root.get("activityFullTime"), end));
                        break;
                    case 2:
                        //已结束
                        predicates.add(cbuild.lessThanOrEqualTo(root.get("activityFullTime"),end));
                        break;
                    case 3:
                        //未开始与正在进行
                        predicates.add(cbuild.greaterThan(root.get("activityFullTime"), end));
                        break;
                }
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getActivityFullTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("activityFullTime"),
                        queryRequest.getActivityFullTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getActivityFullTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("activityFullTime"),
                        queryRequest.getActivityFullTimeEnd()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            if (Constants.yes.equals(queryRequest.getVendibility())) {
                Predicate p1 = cbuild.equal(flashSaleGoodsInfoJoin.get("vendibility"), DefaultFlag.YES.toValue());
                Predicate p2 = cbuild.isNull(flashSaleGoodsInfoJoin.get("vendibility"));
                predicates.add(cbuild.or(p1, p2));
                Predicate p3 = cbuild.equal(flashSaleGoodsInfoJoin.get("providerStatus"), Constants.yes);
                Predicate p4 = cbuild.isNull(flashSaleGoodsInfoJoin.get("providerStatus"));
                predicates.add(cbuild.or(p3, p4));
                predicates.add(cbuild.equal(flashSaleGoodsInfoJoin.get("addedFlag"), AddedFlag.YES.toValue()));
                predicates.add(cbuild.equal(flashSaleGoodsInfoJoin.get("delFlag"), DeleteFlag.NO.toValue()));
                predicates.add(cbuild.equal(flashSaleGoodsInfoJoin.get("auditStatus"), CheckStatus.CHECKED.toValue()));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
