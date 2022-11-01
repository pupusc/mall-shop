package com.wanmi.sbc.goods.goodstobeevaluate.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.goodstobeevaluate.model.root.GoodsTobeEvaluate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>订单商品待评价动态查询条件构建器</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
public class GoodsTobeEvaluateWhereCriteriaBuilder {
    public static Specification<GoodsTobeEvaluate> build(GoodsTobeEvaluateQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // id
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 店铺Id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 店铺名称
            if (StringUtils.isNotEmpty(queryRequest.getStoreName())) {
                predicates.add(cbuild.like(root.get("storeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getStoreName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 商品id(spuId)
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
//                predicates.add(cbuild.like(root.get("goodsId"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsId()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
                predicates.add(cbuild.equal(root.get("goodsId"), queryRequest.getGoodsId()));
            }

            // 模糊查询 - 商品图片
            if (StringUtils.isNotEmpty(queryRequest.getGoodsImg())) {
                predicates.add(cbuild.like(root.get("goodsImg"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsImg()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 货品id(skuId)
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
//                predicates.add(cbuild.like(root.get("goodsInfoId"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoId()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
                predicates.add(cbuild.equal(root.get("goodsInfoId"), queryRequest.getGoodsInfoId()));
            }

            // 模糊查询 - 商品名称
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoName())) {
                predicates.add(cbuild.like(root.get("goodsInfoName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsInfoName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 规格值名称
            if (StringUtils.isNotEmpty(queryRequest.getGoodsSpecDetail())) {
                predicates.add(cbuild.like(root.get("goodsSpecDetail"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getGoodsSpecDetail()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 订单号
            if (StringUtils.isNotEmpty(queryRequest.getOrderNo())) {
//                predicates.add(cbuild.like(root.get("orderNo"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getOrderNo()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
                predicates.add(cbuild.equal(root.get("orderNo"), queryRequest.getOrderNo()));
            }

            // 模糊查询 - 会员Id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
//                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
//                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
//                        .concat(StringUtil.SQL_LIKE_CHAR)));
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            // 模糊查询 - 会员名称
            if (StringUtils.isNotEmpty(queryRequest.getCustomerName())) {
                predicates.add(cbuild.like(root.get("customerName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 会员登录账号|手机号
            if (StringUtils.isNotEmpty(queryRequest.getCustomerAccount())) {
                predicates.add(cbuild.like(root.get("customerAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否评价 0：未评价，1：已评价
            if (queryRequest.getEvaluateStatus() != null) {
                predicates.add(cbuild.equal(root.get("evaluateStatus"), queryRequest.getEvaluateStatus()));
            }

            // 是否晒单 0：未晒单，1：已晒单
            if (queryRequest.getEvaluateImgStatus() != null) {
                predicates.add(cbuild.equal(root.get("evaluateImgStatus"), queryRequest.getEvaluateImgStatus()));
            }

            //商品自动评价日期
            if(queryRequest.getAutoGoodsEvaluateDate() != null){
                predicates.add(cbuild.lessThanOrEqualTo(root.get("autoGoodsEvaluateDate"), queryRequest.getAutoGoodsEvaluateDate()));
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
            cquery.orderBy(cbuild.desc(root.get("buyTime")));

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
