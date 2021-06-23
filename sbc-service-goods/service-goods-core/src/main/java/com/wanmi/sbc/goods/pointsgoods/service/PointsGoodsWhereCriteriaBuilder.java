package com.wanmi.sbc.goods.pointsgoods.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>积分商品表动态查询条件构建器</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
public class PointsGoodsWhereCriteriaBuilder {
    public static Specification<PointsGoods> build(PointsGoodsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<PointsGoods, Goods> pointsGoodsGoodsJoin = root.join("goods");
            Join<PointsGoods, GoodsInfo> pointsGoodsGoodsInfoJoin = root.join("goodsInfo");

            if(Objects.nonNull(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(pointsGoodsGoodsJoin.get("storeId"), queryRequest.getStoreId()));
                predicates.add(cbuild.equal(pointsGoodsGoodsInfoJoin.get("storeId"), queryRequest.getStoreId()));
            }

            // 批量查询-积分商品idList
            if (CollectionUtils.isNotEmpty(queryRequest.getPointsGoodsIdList())) {
                predicates.add(root.get("pointsGoodsId").in(queryRequest.getPointsGoodsIdList()));
            }

            // 积分商品id
            if (StringUtils.isNotEmpty(queryRequest.getPointsGoodsId())) {
                predicates.add(cbuild.equal(root.get("pointsGoodsId"), queryRequest.getPointsGoodsId()));
            }

            // 模糊查询 - SpuId
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - SkuId
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 分类id
            if (queryRequest.getCateId() != null) {
                predicates.add(cbuild.equal(root.get("cateId"), queryRequest.getCateId()));
            }

            // 库存
            if (queryRequest.getStock() != null) {
                predicates.add(cbuild.equal(root.get("stock"), queryRequest.getStock()));
            }

            // 最小库存
            if (queryRequest.getMinStock() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("stock"), queryRequest.getMinStock()));
            }

            // 结算价格
            if (queryRequest.getSettlementPrice() != null) {
                predicates.add(cbuild.equal(root.get("settlementPrice"), queryRequest.getSettlementPrice()));
            }

            // 兑换积分
            if (queryRequest.getPoints() != null) {
                predicates.add(cbuild.equal(root.get("points"), queryRequest.getPoints()));
            }

            // 是否启用 0：停用，1：启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 推荐标价, 0: 未推荐 1: 已推荐
            if (queryRequest.getRecommendFlag() != null) {
                predicates.add(cbuild.equal(root.get("recommendFlag"), queryRequest.getRecommendFlag()));
            }

            // 商品名称模糊查询
            if (StringUtils.isNotEmpty(queryRequest.getGoodsName())) {
                predicates.add(cbuild.like(pointsGoodsGoodsJoin.get("goodsName"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(queryRequest.getGoodsName().trim())).append("%").toString()));
            }

            // SPU编号查询
            if (StringUtils.isNotEmpty(queryRequest.getGoodsNo())) {
                predicates.add(cbuild.like(pointsGoodsGoodsJoin.get("goodsNo"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(queryRequest.getGoodsNo().trim())).append("%").toString()));
            }

            // SKU编号查询
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoNo())) {
                predicates.add(cbuild.like(pointsGoodsGoodsInfoJoin.get("goodsInfoNo"), new StringBuffer().append("%")
                        .append(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoNo().trim())).append("%").toString()));
            }

            // 小于或等于 最大积分
            if (queryRequest.getMaxPoints() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("points"), queryRequest.getMaxPoints()));
            }

            // 大于或等于积分区间查询开始积分
            if (queryRequest.getPointsSectionStart() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("points"), queryRequest.getPointsSectionStart()));
            }

            // 小于或等于积分区间查询截止时间
            if (queryRequest.getPointsSectionEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("points"), queryRequest.getPointsSectionEnd()));
            }
            // 大于或等于 搜索条件:兑换开始时间开始
            if (queryRequest.getBeginTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("beginTime"),
                        queryRequest.getBeginTimeBegin()));
            }
            // 小于或等于 搜索条件:兑换开始时间截止
            if (queryRequest.getBeginTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("beginTime"),
                        queryRequest.getBeginTimeEnd()));
            }

            // 大于或等于 搜索条件:兑换结束时间开始
            if (queryRequest.getEndTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeBegin()));
            }
            // 小于或等于 搜索条件:兑换结束时间截止
            if (queryRequest.getEndTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("endTime"),
                        queryRequest.getEndTimeEnd()));
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

            // 大于或等于 搜索条件:修改时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:修改时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 修改人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 删除标识,0: 未删除 1: 已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            //筛选出可售商品
            if (Boolean.TRUE == queryRequest.getSaleFlag()) {
                predicates.add(cbuild.equal(pointsGoodsGoodsInfoJoin.get("addedFlag"), AddedFlag.YES.toValue()));
                predicates.add(cbuild.equal(pointsGoodsGoodsInfoJoin.get("delFlag"), DeleteFlag.NO.toValue()));
                Predicate p1 = cbuild.equal(pointsGoodsGoodsInfoJoin.get("vendibility"), DefaultFlag.YES.toValue());
                Predicate p2 = cbuild.isNull(pointsGoodsGoodsInfoJoin.get("vendibility"));
                predicates.add(cbuild.or(p1, p2));
                Predicate p3 = cbuild.equal(pointsGoodsGoodsInfoJoin.get("providerStatus"), Constants.yes);
                Predicate p4 = cbuild.isNull(pointsGoodsGoodsInfoJoin.get("providerStatus"));
                predicates.add(cbuild.or(p3, p4));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
